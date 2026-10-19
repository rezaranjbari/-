package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MultiSourcePrice
import com.example.ui.localization.AppLanguage
import com.example.ui.localization.PersianUtils
import com.example.ui.localization.Strings
import com.example.ui.theme.BearishRed
import com.example.ui.theme.BearishRedGlow
import com.example.ui.theme.BullishGreen
import com.example.ui.theme.NeutralAmber
import com.example.ui.theme.RadarBorder
import com.example.ui.theme.RadarCyan
import com.example.ui.theme.RadarSurface
import com.example.ui.theme.RadarSurfaceElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.util.Locale

@Composable
fun MultiSourceCard(
    multiSource: MultiSourcePrice,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    val discFormatted = String.format(Locale.US, "%.2f%%", multiSource.discrepancyPercent)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(RadarSurface)
            .border(1.dp, RadarBorder, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        // Card Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = RadarCyan,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = Strings.multiSourcePriceComparison(language),
                    color = TextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleSmall
                )
            }

            // Discrepancy Badge
            val badgeColor = if (multiSource.hasDiscrepancyWarning) BearishRed else BullishGreen
            val badgeBg = if (multiSource.hasDiscrepancyWarning) BearishRedGlow else BullishGreen.copy(alpha = 0.15f)

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(badgeBg)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = if (language == AppLanguage.PERSIAN)
                        "مغایرت: ${PersianUtils.toPersianDigits(discFormatted)}"
                    else
                        "Spread: $discFormatted",
                    color = badgeColor,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 3 Independent Source Rows
        SourceRow(
            sourceName = Strings.sourceBinance(language),
            sourceType = Strings.exchangePrice(language),
            price = multiSource.binancePrice,
            change = multiSource.binanceChange24h,
            timestamp = multiSource.binanceTimestamp,
            language = language
        )

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 10.dp),
            thickness = 0.5.dp,
            color = RadarBorder
        )

        SourceRow(
            sourceName = Strings.sourceCoinGecko(language),
            sourceType = Strings.aggregatePrice(language),
            price = multiSource.coingeckoPrice,
            change = multiSource.coingeckoChange24h,
            timestamp = multiSource.coingeckoTimestamp,
            language = language
        )

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 10.dp),
            thickness = 0.5.dp,
            color = RadarBorder
        )

        SourceRow(
            sourceName = Strings.sourceCoinCap(language),
            sourceType = Strings.benchmarkPrice(language),
            price = multiSource.coincapPrice,
            change = multiSource.coincapChange24h,
            timestamp = multiSource.coincapTimestamp,
            language = language
        )

        // Warnings if single source or severe discrepancy
        if (multiSource.hasDiscrepancyWarning) {
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(BearishRedGlow)
                    .border(1.dp, BearishRed.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = BearishRed,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = Strings.discrepancyWarning(
                        if (language == AppLanguage.PERSIAN) PersianUtils.toPersianDigits(discFormatted) else discFormatted,
                        language
                    ),
                    color = TextPrimary,
                    fontSize = 11.sp,
                    lineHeight = 16.sp
                )
            }
        } else if (multiSource.activeSourceCount <= 1) {
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(NeutralAmber.copy(alpha = 0.15f))
                    .border(1.dp, NeutralAmber.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = NeutralAmber,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = Strings.singleSourceWarning(language),
                    color = TextPrimary,
                    fontSize = 11.sp,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

@Composable
private fun SourceRow(
    sourceName: String,
    sourceType: String,
    price: Double?,
    change: Double?,
    timestamp: Long?,
    language: AppLanguage
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = sourceName,
                color = TextPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "$sourceType • ${timestamp?.let { PersianUtils.formatTimeAgo(it, language) } ?: "—"}",
                color = TextSecondary,
                fontSize = 10.sp
            )
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = price?.let { PersianUtils.formatPrice(it, language) } ?: "در دسترس نیست",
                color = TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            change?.let {
                val changeText = PersianUtils.formatPercentage(it, language)
                val color = if (it >= 0) BullishGreen else BearishRed
                Text(
                    text = changeText,
                    color = color,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
