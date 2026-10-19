package com.example.data.model

enum class TrendDirection {
    BULLISH,
    BEARISH,
    NEUTRAL
}

enum class RadarSignalType {
    BREAKOUT,
    VOLUME_SPIKE,
    REVERSAL,
    OVERSOLD_BOUNCE,
    SQUEEZE
}

enum class SignalConfidence {
    HIGH,
    MEDIUM,
    CAUTION
}

data class CryptoAsset(
    val symbol: String,
    val name: String,
    val nameFa: String,
    val priceUsd: Double,
    val change24h: Double,
    val high24h: Double,
    val low24h: Double,
    val volume24hUsd: Double,
    val marketCapUsd: Double,
    val marketCapRank: Int,
    val sparkline7d: List<Double> = emptyList(),
    val tradingPair: String = "${symbol}USDT",
    val primarySource: String = "Multi-Source",
    val lastUpdateTimestamp: Long = System.currentTimeMillis()
)

data class MultiSourcePrice(
    val symbol: String,
    val binancePrice: Double? = null,
    val binanceChange24h: Double? = null,
    val binanceTimestamp: Long? = null,
    val coingeckoPrice: Double? = null,
    val coingeckoChange24h: Double? = null,
    val coingeckoTimestamp: Long? = null,
    val coincapPrice: Double? = null,
    val coincapChange24h: Double? = null,
    val coincapTimestamp: Long? = null,
    val discrepancyPercent: Double = 0.0,
    val hasDiscrepancyWarning: Boolean = false,
    val activeSourceCount: Int = 1
)

data class Candle(
    val timestamp: Long,
    val open: Double,
    val high: Double,
    val low: Double,
    val close: Double,
    val volume: Double
)

data class TechnicalIndicators(
    val rsi: Double = 50.0,
    val macdLine: Double = 0.0,
    val signalLine: Double = 0.0,
    val macdHistogram: Double = 0.0,
    val ema9: Double = 0.0,
    val ema21: Double = 0.0,
    val ema50: Double = 0.0,
    val ema200: Double = 0.0,
    val atr: Double = 0.0,
    val volumeRatio: Double = 1.0,
    val bbUpper: Double = 0.0,
    val bbMiddle: Double = 0.0,
    val bbLower: Double = 0.0,
    val trend15m: TrendDirection = TrendDirection.NEUTRAL,
    val trend1h: TrendDirection = TrendDirection.NEUTRAL,
    val trend4h: TrendDirection = TrendDirection.NEUTRAL,
    val trend1d: TrendDirection = TrendDirection.NEUTRAL
)

data class RadarOpportunity(
    val asset: CryptoAsset,
    val score: Int,
    val signalType: RadarSignalType,
    val timeframe: String,
    val confidence: SignalConfidence,
    val indicators: TechnicalIndicators,
    val justificationFa: String,
    val justificationEn: String,
    val entryMin: Double,
    val entryMax: Double,
    val target1: Double,
    val target2: Double,
    val stopLoss: Double,
    val invalidationLevel: Double,
    val riskRewardRatio: Double,
    val bullishScenarioFa: String,
    val bearishScenarioFa: String,
    val neutralScenarioFa: String,
    val detailedReportFa: String
)

data class CryptoNewsItem(
    val id: String,
    val title: String,
    val body: String,
    val source: String,
    val url: String,
    val publishedAt: Long
)

data class MarketOverviewData(
    val btcDominance: Double = 56.4,
    val totalMarketCapUsd: Double = 2_450_000_000_000.0,
    val totalVolume24hUsd: Double = 89_400_000_000.0,
    val btcPrice: Double = 67500.0,
    val btcChange24h: Double = 2.15,
    val ethPrice: Double = 3450.0,
    val ethChange24h: Double = 1.80,
    val marketRegimeFa: String = "روند صعودی آرام با ثبات بیت‌کوین",
    val binanceHealthy: Boolean = true,
    val coingeckoHealthy: Boolean = true,
    val coincapHealthy: Boolean = true,
    val lastSyncTimestamp: Long = System.currentTimeMillis()
)
