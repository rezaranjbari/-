package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Candle
import com.example.data.model.TechnicalIndicators
import com.example.ui.localization.AppLanguage
import com.example.ui.localization.PersianUtils
import com.example.ui.theme.BearishRed
import com.example.ui.theme.BullishGreen
import com.example.ui.theme.NeutralAmber
import com.example.ui.theme.RadarBorder
import com.example.ui.theme.RadarCyan
import com.example.ui.theme.RadarSurface
import com.example.ui.theme.RadarSurfaceVariant
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

@Composable
fun CandleStickChart(
    candles: List<Candle>,
    indicators: TechnicalIndicators,
    selectedSubChart: String,
    onSubChartSelected: (String) -> Unit,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(RadarSurface)
            .border(1.dp, RadarBorder, RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        // Chart Header with Sub-chart Toggle Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (language == AppLanguage.PERSIAN) "نمودار کندل‌استیک و اندیکاتور" else "Candlestick Chart & Sub-Indicator",
                color = TextPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )

            Row {
                listOf("RSI", "MACD").forEach { type ->
                    val isSelected = selectedSubChart == type
                    Box(
                        modifier = Modifier
                            .padding(start = 6.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isSelected) RadarCyan else RadarSurfaceVariant)
                            .clickable { onSubChartSelected(type) }
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = type,
                            color = if (isSelected) Color(0xFF090D16) else TextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (candles.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (language == AppLanguage.PERSIAN) "در حال بارگذاری داده‌های قیمتی..." else "Loading chart data...",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }
            return
        }

        // Main Price & Volume Canvas
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        ) {
            val w = size.width
            val h = size.height
            val priceHeight = h * 0.75f
            val volumeHeight = h * 0.20f
            val volumeTop = h - volumeHeight

            val minPrice = candles.minOf { it.low }
            val maxPrice = candles.maxOf { it.high }
            val priceSpan = (maxPrice - minPrice).coerceAtLeast(0.0001)
            val maxVol = candles.maxOf { it.volume }.coerceAtLeast(1.0)

            val count = candles.size
            val candleSlotWidth = w / count
            val candleBodyWidth = (candleSlotWidth * 0.65f).coerceAtLeast(3f)

            // Draw horizontal price grid lines
            val gridLines = 3
            for (i in 0..gridLines) {
                val y = priceHeight * (i / gridLines.toFloat())
                drawLine(
                    color = RadarBorder.copy(alpha = 0.5f),
                    start = Offset(0f, y),
                    end = Offset(w, y),
                    strokeWidth = 1f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 8f))
                )
            }

            // Draw candles and volume bars
            candles.forEachIndexed { i, candle ->
                val x = (i * candleSlotWidth) + (candleSlotWidth / 2f)
                val isBullish = candle.close >= candle.open
                val candleColor = if (isBullish) BullishGreen else BearishRed

                // Y coordinates for Price
                val highY = priceHeight - (((candle.high - minPrice) / priceSpan) * priceHeight).toFloat()
                val lowY = priceHeight - (((candle.low - minPrice) / priceSpan) * priceHeight).toFloat()
                val openY = priceHeight - (((candle.open - minPrice) / priceSpan) * priceHeight).toFloat()
                val closeY = priceHeight - (((candle.close - minPrice) / priceSpan) * priceHeight).toFloat()

                // Draw Wick
                drawLine(
                    color = candleColor,
                    start = Offset(x, highY),
                    end = Offset(x, lowY),
                    strokeWidth = 1.5f
                )

                // Draw Body
                val bodyTop = min(openY, closeY)
                val bodyHeight = max(abs(closeY - openY), 2f)
                drawRect(
                    color = candleColor,
                    topLeft = Offset(x - (candleBodyWidth / 2f), bodyTop),
                    size = Size(candleBodyWidth, bodyHeight)
                )

                // Draw Volume Bar
                val volRatio = (candle.volume / maxVol).toFloat().coerceIn(0.05f, 1f)
                val barH = volumeHeight * volRatio
                val barTop = h - barH
                drawRect(
                    color = candleColor.copy(alpha = 0.45f),
                    topLeft = Offset(x - (candleBodyWidth / 2f), barTop),
                    size = Size(candleBodyWidth, barH)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Sub-chart Canvas (RSI or MACD)
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(65.dp)
        ) {
            val w = size.width
            val h = size.height

            if (selectedSubChart == "RSI") {
                // Draw 70 and 30 levels
                val y70 = h * 0.3f
                val y30 = h * 0.7f

                drawLine(
                    color = BearishRed.copy(alpha = 0.6f),
                    start = Offset(0f, y70),
                    end = Offset(w, y70),
                    strokeWidth = 1f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f))
                )
                drawLine(
                    color = BullishGreen.copy(alpha = 0.6f),
                    start = Offset(0f, y30),
                    end = Offset(w, y30),
                    strokeWidth = 1f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f))
                )

                // Approximate RSI trend path
                val count = candles.size
                val path = Path()
                candles.forEachIndexed { i, _ ->
                    val x = (i / (count - 1f).coerceAtLeast(1f)) * w
                    val rsiVal = (indicators.rsi + ((i - count / 2f) * 0.8)).coerceIn(15.0, 85.0)
                    val y = h - ((rsiVal / 100f) * h).toFloat()
                    if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
                }

                drawPath(
                    path = path,
                    color = RadarCyan,
                    style = Stroke(width = 2f)
                )
            } else {
                // MACD Zero Line
                val zeroY = h * 0.5f
                drawLine(
                    color = RadarBorder,
                    start = Offset(0f, zeroY),
                    end = Offset(w, zeroY),
                    strokeWidth = 1f
                )

                val count = candles.size
                val slotW = w / count
                candles.forEachIndexed { i, _ ->
                    val x = (i * slotW) + (slotW / 2f)
                    val histVal = (indicators.macdHistogram * 10f).toFloat()
                    val barH = (abs(histVal) * 8f).coerceIn(2f, h * 0.45f)
                    val isPositive = indicators.macdHistogram >= 0
                    val top = if (isPositive) zeroY - barH else zeroY
                    drawRect(
                        color = if (isPositive) BullishGreen else BearishRed,
                        topLeft = Offset(x - 2f, top),
                        size = Size(4f, barH)
                    )
                }
            }
        }
    }
}
