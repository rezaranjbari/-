package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.WatchlistEntity
import com.example.data.model.CryptoAsset
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
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun WatchlistScreen(
    watchlist: List<WatchlistEntity>,
    assets: List<CryptoAsset>,
    language: AppLanguage,
    onAssetClick: (CryptoAsset) -> Unit,
    onRemoveFromWatchlist: (CryptoAsset) -> Unit,
    modifier: Modifier = Modifier
) {
    val assetsMap = assets.associateBy { it.symbol }

    Column(
        modifier = modifier
            .testTag("watchlist_screen")
            .fillMaxSize()
            .background(RadarBg)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(RadarSurface)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = NeutralAmber,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = Strings.watchlistHeader(language),
                color = TextPrimary,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
        }

        if (watchlist.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = Strings.emptyWatchlist(language),
                    color = TextSecondary,
                    fontSize = 13.sp,
                    lineHeight = 22.sp
                )
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(watchlist) { item ->
                    val asset = assetsMap[item.symbol] ?: CryptoAsset(
                        symbol = item.symbol,
                        name = item.name,
                        nameFa = item.nameFa,
                        priceUsd = 0.0,
                        change24h = 0.0,
                        high24h = 0.0,
                        low24h = 0.0,
                        volume24hUsd = 0.0,
                        marketCapUsd = 0.0,
                        marketCapRank = 99
                    )

                    WatchlistItemCard(
                        item = item,
                        asset = asset,
                        language = language,
                        onClick = { onAssetClick(asset) },
                        onRemove = { onRemoveFromWatchlist(asset) }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
private fun WatchlistItemCard(
    item: WatchlistEntity,
    asset: CryptoAsset,
    language: AppLanguage,
    onClick: () -> Unit,
    onRemove: () -> Unit
) {
    val isPositive = asset.change24h >= 0
    val trendColor = if (isPositive) BullishGreen else BearishRed

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = RadarSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, RadarBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(RadarCyan.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item.symbol.take(3),
                        color = RadarCyan,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "${item.nameFa} (${item.symbol})",
                        color = TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = PersianUtils.formatPrice(asset.priceUsd, language),
                        color = TextSecondary,
                        fontSize = 13.sp
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = PersianUtils.formatPercentage(asset.change24h, language),
                        color = trendColor,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "ثبت: ${PersianUtils.formatTimeAgo(item.addedAt, language)}",
                        color = TextSecondary,
                        fontSize = 10.sp
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(onClick = onRemove) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = "Remove from watchlist",
                        tint = BearishRed,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
