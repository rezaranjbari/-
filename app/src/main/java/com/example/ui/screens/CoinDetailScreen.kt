package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Candle
import com.example.data.model.CryptoAsset
import com.example.data.model.MultiSourcePrice
import com.example.data.model.RadarOpportunity
import com.example.data.model.TrendDirection
import com.example.ui.components.CandleStickChart
import com.example.ui.components.MultiSourceCard
import com.example.ui.components.RiskWarningBanner
import com.example.ui.components.ScenarioCard
import com.example.ui.localization.AppLanguage
import com.example.ui.localization.PersianUtils
import com.example.ui.localization.Strings
import com.example.ui.theme.BearishRed
import com.example.ui.theme.BullishGreen
import com.example.ui.theme.NeutralAmber
import com.example.ui.theme.RadarBg
import com.example.ui.theme.RadarBorder
import com.example.ui.theme.RadarCyan
import com.example.ui.theme.RadarSurface
import com.example.ui.theme.RadarSurfaceVariant
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.util.Locale

@Composable
fun CoinDetailScreen(
    asset: CryptoAsset,
    multiSourcePrice: MultiSourcePrice?,
    candles: List<Candle>,
    opportunity: RadarOpportunity?,
    selectedSubChart: String,
    isWatchlisted: Boolean,
    isLoading: Boolean,
    language: AppLanguage,
    onBack: () -> Unit,
    onToggleWatchlist: () -> Unit,
    onSubChartSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    val isPositive = asset.change24h >= 0
    val trendColor = if (isPositive) BullishGreen else BearishRed

    Column(
        modifier = modifier
            .testTag("coin_detail_screen")
            .fillMaxSize()
            .background(RadarBg)
    ) {
        // App Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(RadarSurface)
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = TextPrimary
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = asset.symbol,
                            color = TextPrimary,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = asset.nameFa,
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    }
                    Text(
                        text = "${asset.tradingPair} • ${asset.name}",
                        color = TextSecondary,
                        fontSize = 10.sp
                    )
                }
            }

            IconButton(onClick = onToggleWatchlist) {
                Icon(
                    imageVector = if (isWatchlisted) Icons.Default.Star else Icons.Default.StarBorder,
                    contentDescription = "Watchlist toggle",
                    tint = if (isWatchlisted) NeutralAmber else TextSecondary
                )
            }
        }

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = RadarCyan)
            }
            return
        }

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            // Price & 24h stats header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = PersianUtils.formatPrice(asset.priceUsd, language),
                            color = TextPrimary,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = PersianUtils.formatPercentage(asset.change24h, language),
                                color = trendColor,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "حجم ۲۴س: ${PersianUtils.formatCompactUsd(asset.volume24hUsd, language)}",
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }

                    // Rank Pill
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(RadarSurfaceVariant)
                            .border(1.dp, RadarBorder, RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        val rankStr = if (language == AppLanguage.PERSIAN)
                            "رتبه ${PersianUtils.toPersianDigits(asset.marketCapRank.toString())}"
                        else
                            "Rank #${asset.marketCapRank}"
                        Text(
                            text = rankStr,
                            color = RadarCyan,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Multi-Source Cross-Verification Card
            multiSourcePrice?.let { ms ->
                item {
                    MultiSourceCard(multiSource = ms, language = language)
                }
            }

            // Interactive Candlestick Chart with Volume & Sub-Chart
            item {
                val indicators = opportunity?.indicators ?: com.example.data.model.TechnicalIndicators()
                CandleStickChart(
                    candles = candles,
                    indicators = indicators,
                    selectedSubChart = selectedSubChart,
                    onSubChartSelected = onSubChartSelected,
                    language = language
                )
            }

            // Calculated Technical Indicators Matrix
            opportunity?.let { opp ->
                item {
                    TechnicalIndicatorsGrid(indicators = opp.indicators, language = language)
                }

                // 3 Scenario Analysis (Bullish, Bearish, Neutral)
                item {
                    ScenarioCard(opportunity = opp, language = language)
                }

                // Detailed Comprehensive Persian Narrative Report
                item {
                    DetailedReportCard(reportText = opp.detailedReportFa, language = language)
                }
            }

            // Bottom Mandatory Risk Warning
            item {
                RiskWarningBanner(language = language)
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun TechnicalIndicatorsGrid(
    indicators: com.example.data.model.TechnicalIndicators,
    language: AppLanguage
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(RadarSurface)
            .border(1.dp, RadarBorder, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Assessment,
                contentDescription = null,
                tint = RadarCyan,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = Strings.technicalIndicatorsTitle(language),
                color = TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Grid of 4 key indicators
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            IndicatorMetricBox(
                label = Strings.rsiLabel(language),
                value = String.format(Locale.US, "%.1f", indicators.rsi),
                sub = if (indicators.rsi > 70) "اشباع خرید" else if (indicators.rsi < 30) "اشباع فروش" else "ناحیه معتدل",
                accent = if (indicators.rsi > 70) BearishRed else if (indicators.rsi < 30) BullishGreen else RadarCyan,
                language = language,
                modifier = Modifier.weight(1f)
            )

            IndicatorMetricBox(
                label = Strings.macdLabel(language),
                value = String.format(Locale.US, "%.3f", indicators.macdHistogram),
                sub = if (indicators.macdHistogram >= 0) "سیگنال تقاضا" else "سیگنال عرضه",
                accent = if (indicators.macdHistogram >= 0) BullishGreen else BearishRed,
                language = language,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            IndicatorMetricBox(
                label = Strings.atrLabel(language),
                value = String.format(Locale.US, "$%.4f", indicators.atr),
                sub = "دامنه نوسان میانگین",
                accent = NeutralAmber,
                language = language,
                modifier = Modifier.weight(1f)
            )

            IndicatorMetricBox(
                label = Strings.volumeRatioLabel(language),
                value = String.format(Locale.US, "%.2fx", indicators.volumeRatio),
                sub = if (indicators.volumeRatio > 1.8) "جهش غیرعادی حجم" else "حجم نرمال",
                accent = if (indicators.volumeRatio > 1.8) BullishGreen else TextSecondary,
                language = language,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Multi-timeframe trend alignment badges
        Text(
            text = Strings.emaTrends(language),
            color = TextSecondary,
            fontSize = 11.sp
        )

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TimeframeTrendPill("15m", indicators.trend15m, language)
            TimeframeTrendPill("1h", indicators.trend1h, language)
            TimeframeTrendPill("4h", indicators.trend4h, language)
            TimeframeTrendPill("1D", indicators.trend1d, language)
        }
    }
}

@Composable
private fun IndicatorMetricBox(
    label: String,
    value: String,
    sub: String,
    accent: Color,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(RadarSurfaceVariant)
            .padding(10.dp)
    ) {
        Text(text = label, color = TextSecondary, fontSize = 10.sp)
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = if (language == AppLanguage.PERSIAN) PersianUtils.toPersianDigits(value) else value,
            color = accent,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        Text(text = sub, color = TextSecondary, fontSize = 9.sp)
    }
}

@Composable
private fun TimeframeTrendPill(tf: String, trend: TrendDirection, language: AppLanguage) {
    val (labelFa, color) = when (trend) {
        TrendDirection.BULLISH -> Pair("صعودی", BullishGreen)
        TrendDirection.BEARISH -> Pair("نزولی", BearishRed)
        TrendDirection.NEUTRAL -> Pair("رنج", NeutralAmber)
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(color.copy(alpha = 0.15f))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = "$tf: ${if (language == AppLanguage.PERSIAN) labelFa else trend.name}",
            color = color,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun DetailedReportCard(
    reportText: String,
    language: AppLanguage
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(RadarSurface)
            .border(1.dp, RadarBorder, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Text(
            text = Strings.deepReportHeader(language),
            color = TextPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleSmall
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = reportText,
            color = TextSecondary,
            fontSize = 12.sp,
            lineHeight = 20.sp
        )
    }
}
