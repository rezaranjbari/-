package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Moving
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import com.example.data.model.CryptoAsset
import com.example.data.model.MarketOverviewData
import com.example.data.model.RadarOpportunity
import com.example.ui.components.RiskWarningBanner
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
fun MarketDashboardScreen(
    overview: MarketOverviewData,
    assets: List<CryptoAsset>,
    radarPicks: List<RadarOpportunity>,
    language: AppLanguage,
    onAssetClick: (CryptoAsset) -> Unit,
    onNavigateToScanner: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .testTag("market_dashboard_screen")
            .fillMaxSize()
            .background(RadarBg),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Prominent Risk Warning Banner
        item {
            RiskWarningBanner(language = language)
        }

        // 2. Global Market Pulse Card
        item {
            MarketPulseCard(overview = overview, language = language)
        }

        // 3. BTC & ETH Spotlight Cards
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val btcAsset = assets.firstOrNull { it.symbol == "BTC" }
                val ethAsset = assets.firstOrNull { it.symbol == "ETH" }

                SpotlightCard(
                    symbol = "BTC",
                    nameFa = "بیت‌کوین",
                    price = overview.btcPrice,
                    change = overview.btcChange24h,
                    language = language,
                    modifier = Modifier.weight(1f),
                    onClick = { btcAsset?.let(onAssetClick) }
                )

                SpotlightCard(
                    symbol = "ETH",
                    nameFa = "اتریوم",
                    price = overview.ethPrice,
                    change = overview.ethChange24h,
                    language = language,
                    modifier = Modifier.weight(1f),
                    onClick = { ethAsset?.let(onAssetClick) }
                )
            }
        }

        // 4. Multi-Source Connection Health
        item {
            SourceHealthStatusCard(overview = overview, language = language)
        }

        // 5. Radar Top Opportunities Shortcut
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Radar,
                        contentDescription = null,
                        tint = RadarCyan,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = Strings.radarPicksQuick(language),
                        color = TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                Text(
                    text = if (language == AppLanguage.PERSIAN) "مشاهده همه رادار ›" else "View All Radar ›",
                    color = RadarCyan,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clickable { onNavigateToScanner() }
                        .padding(4.dp)
                )
            }
        }

        items(radarPicks.take(3)) { pick ->
            RadarPickCard(
                opportunity = pick,
                language = language,
                onClick = { onAssetClick(pick.asset) }
            )
        }

        // 6. Top Movers (Gainers / Losers)
        item {
            Text(
                text = Strings.topGainers(language),
                color = TextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
        }

        item {
            val gainers = assets.filter { it.change24h > 0 }.sortedByDescending { it.change24h }.take(6)
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(gainers) { asset ->
                    MiniMoverCard(asset = asset, language = language, onClick = { onAssetClick(asset) })
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun MarketPulseCard(
    overview: MarketOverviewData,
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
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Bolt,
                    contentDescription = null,
                    tint = RadarCyan,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = Strings.marketPulse(language),
                    color = TextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(RadarCyan.copy(alpha = 0.15f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = overview.marketRegimeFa,
                    color = RadarCyan,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = Strings.totalMarketCap(language), color = TextSecondary, fontSize = 11.sp)
                Text(
                    text = PersianUtils.formatCompactUsd(overview.totalMarketCapUsd, language),
                    color = TextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = Strings.volume24h(language), color = TextSecondary, fontSize = 11.sp)
                Text(
                    text = PersianUtils.formatCompactUsd(overview.totalVolume24hUsd, language),
                    color = TextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(text = Strings.btcDominance(language), color = TextSecondary, fontSize = 11.sp)
                val domStr = String.format(Locale.US, "%.1f%%", overview.btcDominance)
                Text(
                    text = if (language == AppLanguage.PERSIAN) PersianUtils.toPersianDigits(domStr) else domStr,
                    color = RadarCyan,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun SpotlightCard(
    symbol: String,
    nameFa: String,
    price: Double,
    change: Double,
    language: AppLanguage,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isPositive = change >= 0
    val trendColor = if (isPositive) BullishGreen else BearishRed

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(RadarSurface)
            .border(1.dp, RadarBorder, RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = symbol, color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(text = nameFa, color = TextSecondary, fontSize = 11.sp)
            }
            Icon(
                imageVector = if (isPositive) Icons.Default.TrendingUp else Icons.Default.TrendingDown,
                contentDescription = null,
                tint = trendColor,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = PersianUtils.formatPrice(price, language),
            color = TextPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = PersianUtils.formatPercentage(change, language),
            color = trendColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun SourceHealthStatusCard(
    overview: MarketOverviewData,
    language: AppLanguage
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(RadarSurface)
            .border(1.dp, RadarBorder, RoundedCornerShape(14.dp))
            .padding(14.dp)
    ) {
        Text(
            text = Strings.sourcesHealth(language),
            color = TextPrimary,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SourceStatusItem(name = "Binance API", isHealthy = overview.binanceHealthy, language = language)
            SourceStatusItem(name = "CoinGecko API", isHealthy = overview.coingeckoHealthy, language = language)
            SourceStatusItem(name = "CoinCap API", isHealthy = overview.coincapHealthy, language = language)
        }
    }
}

@Composable
private fun SourceStatusItem(name: String, isHealthy: Boolean, language: AppLanguage) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(if (isHealthy) BullishGreen else BearishRed)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = name,
            color = TextSecondary,
            fontSize = 11.sp
        )
    }
}

@Composable
private fun RadarPickCard(
    opportunity: RadarOpportunity,
    language: AppLanguage,
    onClick: () -> Unit
) {
    val asset = opportunity.asset
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = RadarSurface),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, RadarBorder)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(RadarCyan.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = asset.symbol.take(3),
                            color = RadarCyan,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = "${asset.nameFa} (${asset.symbol})",
                            color = TextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = PersianUtils.formatPrice(asset.priceUsd, language),
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    }
                }

                // Score pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(BullishGreen.copy(alpha = 0.2f))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    val scoreText = if (language == AppLanguage.PERSIAN)
                        "امتیاز: ${PersianUtils.toPersianDigits(opportunity.score.toString())}"
                    else
                        "Score: ${opportunity.score}"
                    Text(
                        text = scoreText,
                        color = BullishGreen,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = opportunity.justificationFa,
                color = TextSecondary,
                fontSize = 11.sp,
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
private fun MiniMoverCard(
    asset: CryptoAsset,
    language: AppLanguage,
    onClick: () -> Unit
) {
    val isPositive = asset.change24h >= 0
    val trendColor = if (isPositive) BullishGreen else BearishRed

    Column(
        modifier = Modifier
            .width(115.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(RadarSurface)
            .border(1.dp, RadarBorder, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(10.dp)
    ) {
        Text(
            text = asset.symbol,
            color = TextPrimary,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = PersianUtils.formatPrice(asset.priceUsd, language),
            color = TextSecondary,
            fontSize = 11.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = PersianUtils.formatPercentage(asset.change24h, language),
            color = trendColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
