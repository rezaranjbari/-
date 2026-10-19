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
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.example.data.model.RadarOpportunity
import com.example.data.model.RadarSignalType
import com.example.ui.components.ScoreRadarMeter
import com.example.ui.localization.AppLanguage
import com.example.ui.localization.PersianUtils
import com.example.ui.localization.Strings
import com.example.ui.theme.BearishRed
import com.example.ui.theme.BullishGreen
import com.example.ui.theme.NeutralAmber
import com.example.ui.theme.RadarBg
import com.example.ui.theme.RadarBorder
import com.example.ui.theme.RadarCyan
import com.example.ui.theme.RadarPurple
import com.example.ui.theme.RadarSurface
import com.example.ui.theme.RadarSurfaceVariant
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.util.Locale

@Composable
fun OpportunityScannerScreen(
    opportunities: List<RadarOpportunity>,
    selectedTimeframe: String,
    selectedStrategy: String,
    searchQuery: String,
    language: AppLanguage,
    onTimeframeChange: (String) -> Unit,
    onStrategyChange: (String) -> Unit,
    onSearchChange: (String) -> Unit,
    onAssetClick: (CryptoAsset) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .testTag("opportunity_scanner_screen")
            .fillMaxSize()
            .background(RadarBg)
    ) {
        // Sticky Header with Search and Filter Bars
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(RadarSurface)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Search Input Field
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchChange,
                placeholder = {
                    Text(
                        text = Strings.searchPlaceholder(language),
                        color = TextSecondary,
                        fontSize = 13.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = TextSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchChange("") }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear",
                                tint = TextSecondary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = RadarSurfaceVariant,
                    unfocusedContainerColor = RadarSurfaceVariant,
                    focusedBorderColor = RadarCyan,
                    unfocusedBorderColor = RadarBorder,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Timeframe Filter Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${Strings.timeframeFilter(language)}:",
                    color = TextSecondary,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(end = 8.dp)
                )

                val timeframes = listOf("15m", "1h", "4h", "1D")
                timeframes.forEach { tf ->
                    val isSelected = selectedTimeframe == tf
                    val label = when (tf) {
                        "15m" -> Strings.tf15m(language)
                        "1h" -> Strings.tf1h(language)
                        "4h" -> Strings.tf4h(language)
                        "1D" -> Strings.tf1d(language)
                        else -> tf
                    }

                    Box(
                        modifier = Modifier
                            .padding(end = 6.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) RadarCyan else RadarSurfaceVariant)
                            .border(1.dp, if (isSelected) RadarCyan else RadarBorder, RoundedCornerShape(8.dp))
                            .clickable { onTimeframeChange(tf) }
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = label,
                            color = if (isSelected) Color(0xFF090D16) else TextPrimary,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Strategy Filter Chips
            val strategies = listOf(
                Pair("ALL", Strings.allStrategies(language)),
                Pair("BREAKOUT", Strings.stratBreakout(language)),
                Pair("VOLUME_SPIKE", Strings.stratVolumeSpike(language)),
                Pair("REVERSAL", Strings.stratReversal(language)),
                Pair("OVERSOLD", Strings.stratOversold(language)),
                Pair("SQUEEZE", Strings.stratSqueeze(language))
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(strategies) { (key, label) ->
                    val isSelected = selectedStrategy == key
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) RadarPurple else RadarSurfaceVariant)
                            .border(1.dp, if (isSelected) RadarPurple else RadarBorder, RoundedCornerShape(8.dp))
                            .clickable { onStrategyChange(key) }
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = label,
                            color = if (isSelected) Color.White else TextSecondary,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
        }

        // List of Ranked Opportunities
        if (opportunities.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = Strings.noCandidatesFound(language),
                        color = TextSecondary,
                        fontSize = 13.sp
                    )
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    Text(
                        text = if (language == AppLanguage.PERSIAN)
                            "تعداد ${PersianUtils.toPersianDigits(opportunities.size.toString())} دارایی مستعد بر اساس داده‌های زنده و تحلیل چندمنبعی رتبه‌بندی شد:"
                        else
                            "${opportunities.size} assets ranked based on live multi-source signals:",
                        color = TextSecondary,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }

                items(opportunities) { opp ->
                    ScannerCandidateCard(
                        opportunity = opp,
                        language = language,
                        onClick = { onAssetClick(opp.asset) }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(30.dp))
                }
            }
        }
    }
}

@Composable
private fun ScannerCandidateCard(
    opportunity: RadarOpportunity,
    language: AppLanguage,
    onClick: () -> Unit
) {
    val asset = opportunity.asset
    val isPositive = asset.change24h >= 0
    val trendColor = if (isPositive) BullishGreen else BearishRed

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = RadarSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, RadarBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row: Symbol, Name, Price, and Score Gauge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = asset.symbol,
                            color = TextPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        // Pattern Badge
                        val (patternName, patternColor) = when (opportunity.signalType) {
                            RadarSignalType.BREAKOUT -> Pair(Strings.stratBreakout(language), BullishGreen)
                            RadarSignalType.VOLUME_SPIKE -> Pair(Strings.stratVolumeSpike(language), RadarCyan)
                            RadarSignalType.REVERSAL -> Pair(Strings.stratReversal(language), RadarPurple)
                            RadarSignalType.OVERSOLD_BOUNCE -> Pair(Strings.stratOversold(language), NeutralAmber)
                            RadarSignalType.SQUEEZE -> Pair(Strings.stratSqueeze(language), Color(0xFFEC4899))
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(patternColor.copy(alpha = 0.15f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = patternName,
                                color = patternColor,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Text(
                        text = asset.nameFa,
                        color = TextSecondary,
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = PersianUtils.formatPrice(asset.priceUsd, language),
                            color = TextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = PersianUtils.formatPercentage(asset.change24h, language),
                            color = trendColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                // Radar Score Circular Meter
                ScoreRadarMeter(
                    score = opportunity.score,
                    confidence = opportunity.confidence,
                    language = language
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Transparent Justification in Persian
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(RadarSurfaceVariant)
                    .padding(10.dp)
            ) {
                Text(
                    text = if (language == AppLanguage.PERSIAN) opportunity.justificationFa else opportunity.justificationEn,
                    color = TextPrimary,
                    fontSize = 11.sp,
                    lineHeight = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Key Levels Pill Row (Entry Zone, Stop Loss, Risk/Reward)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = Strings.entryZone(language), color = TextSecondary, fontSize = 10.sp)
                    Text(
                        text = PersianUtils.formatPrice(opportunity.entryMin, language),
                        color = TextPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = Strings.stopLoss(language), color = TextSecondary, fontSize = 10.sp)
                    Text(
                        text = PersianUtils.formatPrice(opportunity.stopLoss, language),
                        color = BearishRed,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(text = Strings.riskReward(language), color = TextSecondary, fontSize = 10.sp)
                    val rr = String.format(Locale.US, "1:%.1f", opportunity.riskRewardRatio)
                    Text(
                        text = if (language == AppLanguage.PERSIAN) PersianUtils.toPersianDigits(rr) else rr,
                        color = RadarCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
