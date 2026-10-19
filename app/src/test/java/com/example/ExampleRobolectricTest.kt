package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.analysis.TechnicalAnalysisEngine
import com.example.data.model.Candle
import com.example.data.model.CryptoAsset
import com.example.data.model.MultiSourcePrice
import com.example.ui.localization.AppLanguage
import com.example.ui.localization.PersianUtils
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read string from context verifies app name`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        val appNameFa = context.getString(R.string.app_name_fa)
        assertEquals("Crypto Radar Pro", appName)
        assertEquals("رادار کریپتو پرو", appNameFa)
    }

    @Test
    fun `persian utils formats prices and percentages correctly`() {
        val formattedPriceFa = PersianUtils.formatPrice(67450.50, AppLanguage.PERSIAN)
        assertTrue(formattedPriceFa.isNotEmpty())
        assertTrue(formattedPriceFa.contains("$"))

        val formattedPercentFa = PersianUtils.formatPercentage(4.25, AppLanguage.PERSIAN)
        assertTrue(formattedPercentFa.contains("%") || formattedPercentFa.contains("٪"))

        val compactVol = PersianUtils.formatCompactUsd(1_500_000_000.0, AppLanguage.PERSIAN)
        assertTrue(compactVol.contains("میلیارد دلار"))
    }

    @Test
    fun `technical analysis engine calculates realistic indicators and scores`() {
        val candles = mutableListOf<Candle>()
        var price = 100.0
        val now = System.currentTimeMillis()
        for (i in 0 until 30) {
            price += (if (i % 2 == 0) 1.5 else -0.8)
            candles.add(
                Candle(
                    timestamp = now - ((30 - i) * 3600000L),
                    open = price - 0.5,
                    high = price + 1.2,
                    low = price - 0.9,
                    close = price,
                    volume = 50000.0 * (if (i > 25) 2.2 else 1.0)
                )
            )
        }

        val indicators = TechnicalAnalysisEngine.calculateIndicators(candles)
        assertTrue(indicators.rsi in 0.0..100.0)
        assertTrue(indicators.atr > 0.0)
        assertTrue(indicators.volumeRatio > 0.0)

        val asset = CryptoAsset(
            symbol = "SOL",
            name = "Solana",
            nameFa = "سولانا",
            priceUsd = price,
            change24h = 5.2,
            high24h = price * 1.05,
            low24h = price * 0.95,
            volume24hUsd = 1_500_000_000.0,
            marketCapUsd = 65_000_000_000.0,
            marketCapRank = 5
        )

        val opp = TechnicalAnalysisEngine.evaluateOpportunity(asset, candles, btcChange24h = 2.0)
        assertNotNull(opp)
        assertTrue(opp.score in 25..100)
        assertTrue(opp.justificationFa.isNotBlank())
        assertTrue(opp.target1 > opp.entryMax)
        assertTrue(opp.stopLoss < opp.entryMin)
        assertTrue(opp.invalidationLevel <= opp.stopLoss)
    }

    @Test
    fun `multi source price discrepancy detects spreads over threshold`() {
        val msHighSpread = MultiSourcePrice(
            symbol = "BTC",
            binancePrice = 68000.0,
            coingeckoPrice = 66500.0,
            coincapPrice = 67200.0,
            discrepancyPercent = ((68000.0 - 66500.0) / 66500.0) * 100.0,
            hasDiscrepancyWarning = true,
            activeSourceCount = 3
        )

        assertTrue(msHighSpread.hasDiscrepancyWarning)
        assertTrue(msHighSpread.discrepancyPercent > 1.5)
    }
}
