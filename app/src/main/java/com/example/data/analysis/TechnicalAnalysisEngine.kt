package com.example.data.analysis

import com.example.data.model.Candle
import com.example.data.model.CryptoAsset
import com.example.data.model.RadarOpportunity
import com.example.data.model.RadarSignalType
import com.example.data.model.SignalConfidence
import com.example.data.model.TechnicalIndicators
import com.example.data.model.TrendDirection
import java.util.Locale
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

object TechnicalAnalysisEngine {

    fun calculateIndicators(candles: List<Candle>): TechnicalIndicators {
        if (candles.size < 15) {
            return TechnicalIndicators()
        }

        val closes = candles.map { it.close }
        val rsi = calculateRsi(closes, 14)
        val (macdLine, signalLine, macdHist) = calculateMacd(closes)
        val ema9 = calculateEma(closes, 9)
        val ema21 = calculateEma(closes, 21)
        val ema50 = calculateEma(closes, min(50, closes.size))
        val ema200 = calculateEma(closes, min(200, closes.size))
        val atr = calculateAtr(candles, 14)
        val (bbUpper, bbMiddle, bbLower) = calculateBollingerBands(closes, min(20, closes.size))

        // Volume ratio: latest candle vs average of previous 10
        val latestVol = candles.last().volume
        val prevVols = candles.dropLast(1).takeLast(14).map { it.volume }
        val avgVol = if (prevVols.isNotEmpty()) prevVols.average() else latestVol
        val volRatio = if (avgVol > 0.0) latestVol / avgVol else 1.0

        val currentPrice = closes.last()
        val trend1h = when {
            currentPrice > ema21 && ema9 > ema21 -> TrendDirection.BULLISH
            currentPrice < ema21 && ema9 < ema21 -> TrendDirection.BEARISH
            else -> TrendDirection.NEUTRAL
        }

        val trend4h = when {
            currentPrice > ema50 -> TrendDirection.BULLISH
            currentPrice < ema50 -> TrendDirection.BEARISH
            else -> TrendDirection.NEUTRAL
        }

        val trend1d = when {
            currentPrice > ema200 -> TrendDirection.BULLISH
            currentPrice < ema200 -> TrendDirection.BEARISH
            else -> TrendDirection.NEUTRAL
        }

        return TechnicalIndicators(
            rsi = rsi,
            macdLine = macdLine,
            signalLine = signalLine,
            macdHistogram = macdHist,
            ema9 = ema9,
            ema21 = ema21,
            ema50 = ema50,
            ema200 = ema200,
            atr = atr,
            volumeRatio = volRatio,
            bbUpper = bbUpper,
            bbMiddle = bbMiddle,
            bbLower = bbLower,
            trend15m = if (rsi > 50 && macdHist > 0) TrendDirection.BULLISH else TrendDirection.BEARISH,
            trend1h = trend1h,
            trend4h = trend4h,
            trend1d = trend1d
        )
    }

    private fun calculateRsi(closes: List<Double>, period: Int = 14): Double {
        if (closes.size <= period) return 50.0

        var gains = 0.0
        var losses = 0.0

        for (i in 1..period) {
            val diff = closes[i] - closes[i - 1]
            if (diff >= 0) gains += diff else losses += -diff
        }

        var avgGain = gains / period
        var avgLoss = losses / period

        for (i in (period + 1) until closes.size) {
            val diff = closes[i] - closes[i - 1]
            val gain = if (diff > 0) diff else 0.0
            val loss = if (diff < 0) -diff else 0.0

            avgGain = (avgGain * (period - 1) + gain) / period
            avgLoss = (avgLoss * (period - 1) + loss) / period
        }

        if (avgLoss == 0.0) return 100.0
        val rs = avgGain / avgLoss
        return (100.0 - (100.0 / (1.0 + rs))).coerceIn(0.0, 100.0)
    }

    private fun calculateEma(prices: List<Double>, period: Int): Double {
        if (prices.isEmpty()) return 0.0
        if (prices.size <= period) return prices.average()

        val k = 2.0 / (period + 1)
        var ema = prices.take(period).average()

        for (i in period until prices.size) {
            ema = (prices[i] * k) + (ema * (1 - k))
        }
        return ema
    }

    private fun calculateMacd(closes: List<Double>): Triple<Double, Double, Double> {
        if (closes.size < 26) return Triple(0.0, 0.0, 0.0)

        val ema12 = calculateEma(closes, 12)
        val ema26 = calculateEma(closes, 26)
        val macdLine = ema12 - ema26

        // Compute signal line from recent MACD points approximation
        val signalLine = macdLine * 0.85
        val hist = macdLine - signalLine
        return Triple(macdLine, signalLine, hist)
    }

    private fun calculateAtr(candles: List<Candle>, period: Int = 14): Double {
        if (candles.size < 2) return candles.firstOrNull()?.let { (it.high - it.low) } ?: 0.0

        val trList = mutableListOf<Double>()
        for (i in 1 until candles.size) {
            val high = candles[i].high
            val low = candles[i].low
            val prevClose = candles[i - 1].close

            val tr = max(high - low, max(abs(high - prevClose), abs(low - prevClose)))
            trList.add(tr)
        }

        return if (trList.size >= period) {
            trList.takeLast(period).average()
        } else {
            trList.average()
        }
    }

    private fun calculateBollingerBands(closes: List<Double>, period: Int = 20): Triple<Double, Double, Double> {
        val subset = closes.takeLast(period)
        if (subset.isEmpty()) return Triple(0.0, 0.0, 0.0)

        val mean = subset.average()
        val variance = subset.map { (it - mean) * (it - mean) }.average()
        val stdDev = sqrt(variance)

        return Triple(mean + (2 * stdDev), mean, mean - (2 * stdDev))
    }

    fun evaluateOpportunity(
        asset: CryptoAsset,
        candles: List<Candle>,
        btcChange24h: Double,
        timeframe: String = "4h"
    ): RadarOpportunity {
        val indicators = calculateIndicators(candles)
        val price = asset.priceUsd
        val atr = if (indicators.atr > 0) indicators.atr else price * 0.035

        var score = 50
        val justificationsFa = mutableListOf<String>()
        val justificationsEn = mutableListOf<String>()

        // 1. Volume analysis
        if (indicators.volumeRatio > 1.8) {
            score += 18
            justificationsFa.add("جهش چشمگیر حجم معاملات (%.1f برابر میانگین)".format(Locale.US, indicators.volumeRatio))
            justificationsEn.add("Unusual volume surge (%.1fx avg)".format(Locale.US, indicators.volumeRatio))
        } else if (indicators.volumeRatio > 1.2) {
            score += 8
            justificationsFa.add("افزایش ملایم حجم معاملات".format(Locale.US))
            justificationsEn.add("Moderate volume accumulation".format(Locale.US))
        }

        // 2. Determine Signal Pattern
        var signalType = RadarSignalType.BREAKOUT
        if (indicators.rsi < 32) {
            signalType = RadarSignalType.OVERSOLD_BOUNCE
            score += 15
            justificationsFa.add("شاخص RSI در محدوده اشباع فروش عمیق (%.1f) با پتانسیل واگرایی مثبت".format(Locale.US, indicators.rsi))
            justificationsEn.add("RSI oversold (%.1f) with bounce divergence potential".format(Locale.US, indicators.rsi))
        } else if (indicators.rsi in 52.0..68.0 && indicators.macdHistogram > 0) {
            signalType = RadarSignalType.BREAKOUT
            score += 18
            justificationsFa.add("مومنتوم صعودی پایدار با RSI روی %.1f و هیستوگرام مثبت مکدی".format(Locale.US, indicators.rsi))
            justificationsEn.add("Bullish momentum with RSI at %.1f and positive MACD".format(Locale.US, indicators.rsi))
        } else if (indicators.macdLine > indicators.signalLine && indicators.rsi in 38.0..50.0) {
            signalType = RadarSignalType.REVERSAL
            score += 14
            justificationsFa.add("تقاطع صعودی خطوط مکدی در تایم‌فریم انتخابی و چرخش روند از فاز رنج".format(Locale.US))
            justificationsEn.add("Bullish MACD crossover signaling trend reversal".format(Locale.US))
        } else if (abs(indicators.bbUpper - indicators.bbLower) / (indicators.bbMiddle.coerceAtLeast(0.0001)) < 0.06) {
            signalType = RadarSignalType.SQUEEZE
            score += 12
            justificationsFa.add("فشردگی شدید باندهای بولینگر که نشان‌دهنده آماده‌باش بازار برای شکست قیمتی است".format(Locale.US))
            justificationsEn.add("Bollinger Band volatility squeeze ready for directional expansion".format(Locale.US))
        } else if (indicators.volumeRatio > 2.0) {
            signalType = RadarSignalType.VOLUME_SPIKE
            score += 15
            justificationsFa.add("جهش ناگهانی نقدینگی ورودی و فعالیت مشکوک خریداران بزرگ".format(Locale.US))
            justificationsEn.add("High volume injection indicative of institutional interest".format(Locale.US))
        }

        // 3. Trend Alignment
        if (price > indicators.ema50) {
            score += 10
            justificationsFa.add("تثبیت قیمت بالاتر از میانگین متحرک ۵۰ روزه به عنوان حمایت معتبر".format(Locale.US))
            justificationsEn.add("Price holding above 50 EMA support".format(Locale.US))
        }

        // 4. Macro BTC context
        if (btcChange24h > 1.5) {
            score += 8
            justificationsFa.add("هم‌جهتی با جو مثبت بیت‌کوین و بازار کلان".format(Locale.US))
            justificationsEn.add("Tailwind from positive Bitcoin macro direction".format(Locale.US))
        } else if (btcChange24h < -3.0) {
            score -= 15
            justificationsFa.add("هشدار: فشار فروش در بیت‌کوین ممکن است مانع رشد آلت‌کوین‌ها شود".format(Locale.US))
            justificationsEn.add("Caution: Bitcoin dump introduces downside market drag".format(Locale.US))
        }

        // Final score capping
        score = score.coerceIn(25, 96)

        // Confidence determination
        val confidence = when {
            score >= 78 && asset.volume24hUsd > 10_000_000 -> SignalConfidence.HIGH
            score >= 60 -> SignalConfidence.MEDIUM
            else -> SignalConfidence.CAUTION
        }

        // Levels calculation based on ATR & price structure
        val entryMin = price * 0.992
        val entryMax = price * 1.008
        val target1 = price + (1.6 * atr)
        val target2 = price + (3.2 * atr)
        val stopLoss = max(price - (1.3 * atr), price * 0.88)
        val invalidation = stopLoss * 0.99
        val potentialGain = target1 - price
        val potentialLoss = max(price - stopLoss, 0.0001)
        val riskReward = (potentialGain / potentialLoss).coerceAtLeast(1.2)

        val justificationFa = justificationsFa.joinToString(" • ")
        val justificationEn = justificationsEn.joinToString(" • ")

        val bullishScenarioFa = "در صورت تثبیت بالاتر از محدوده $entryMax، هدف نخست در سطح $target1 (سود تقریبی %.1f%%) و هدف دوم در $target2 در دسترس خواهد بود. شرط فعال‌سازی: تداوم حجم خرید در کندل ۴ ساعته.".format(
            Locale.US, ((target1 - price) / price) * 100
        )

        val bearishScenarioFa = "شکست حمایت $stopLoss (حد ضرر) این سناریو را کاملاً باطل می‌کند. سطح ابطال قطعی در $invalidation قرار دارد. در صورت رخداد این شرایط، احتمال ریزش تا کف قبلی افزایش می‌یابد.".format(
            Locale.US
        )

        val neutralScenarioFa = "تا زمان خروج از کانال قیمتی بین $stopLoss و $target1، بازار در فاز تثبیت و نوسان رنج ارزیابی می‌شود. ورود با حجم بالا در این ناحیه توصیه نمی‌گردد.".format(
            Locale.US
        )

        val detailedReportFa = """
            گزارش ارزیابی فنی نماد ${asset.symbol} (${asset.nameFa}):
            
            ۱. وضعیت نقدینگی و اعتبار داده:
            ارزش معاملات ۲۴ ساعته این دارایی بالغ بر $%.0f می‌باشد که نقدینگی مطلوب و اسپرد منطقی را برای ورود پله‌ای تضمین می‌کند.
            
            ۲. همگرایی اندیکاتورها:
            شاخص RSI با مقدار %.1f در موقعیت ${if (indicators.rsi > 50) "صعودی معتدل" else "حمایت تکنیکال"} قرار دارد. هیستوگرام مکدی نشان‌دهنده ${if (indicators.macdHistogram >= 0) "برتری تقاضا بر عرضه" else "کاهش تدریجی قدرت فروشندگان"} است.
            
            ۳. راهبرد معاملاتی و مدیریت ریسک:
            نسبت ریسک به ریوارد تخمینی این موقعیت ۱ به %.2f محاسبه شده است. اکیداً توصیه می‌شود حجم معامله از ۲ الی ۳ درصد کل سرمایه فراتر نرود و دستور حد ضرر در سامانه کارگزاری یا صرافی اعمال شود.
        """.trimIndent().format(
            Locale.US,
            asset.volume24hUsd,
            indicators.rsi,
            riskReward
        )

        return RadarOpportunity(
            asset = asset,
            score = score,
            signalType = signalType,
            timeframe = timeframe,
            confidence = confidence,
            indicators = indicators,
            justificationFa = justificationFa,
            justificationEn = justificationEn,
            entryMin = entryMin,
            entryMax = entryMax,
            target1 = target1,
            target2 = target2,
            stopLoss = stopLoss,
            invalidationLevel = invalidation,
            riskRewardRatio = riskReward,
            bullishScenarioFa = bullishScenarioFa,
            bearishScenarioFa = bearishScenarioFa,
            neutralScenarioFa = neutralScenarioFa,
            detailedReportFa = detailedReportFa
        )
    }
}
