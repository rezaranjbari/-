package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SignalConfidence
import com.example.ui.localization.AppLanguage
import com.example.ui.localization.PersianUtils
import com.example.ui.localization.Strings
import com.example.ui.theme.BullishGreen
import com.example.ui.theme.NeutralAmber
import com.example.ui.theme.RadarCyan
import com.example.ui.theme.RadarSurfaceElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun ScoreRadarMeter(
    score: Int,
    confidence: SignalConfidence,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    val meterColor = when {
        score >= 75 -> BullishGreen
        score >= 55 -> RadarCyan
        else -> NeutralAmber
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(110.dp)
    ) {
        Canvas(modifier = Modifier.size(100.dp)) {
            val strokeWidth = 10.dp.toPx()
            // Track arc
            drawArc(
                color = RadarSurfaceElevated,
                startAngle = 135f,
                sweepAngle = 270f,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // Progress arc
            val sweep = (score / 100f) * 270f
            drawArc(
                brush = Brush.sweepGradient(
                    colors = listOf(RadarCyan, meterColor, meterColor)
                ),
                startAngle = 135f,
                sweepAngle = sweep,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val scoreStr = if (language == AppLanguage.PERSIAN)
                PersianUtils.toPersianDigits(score.toString())
            else
                score.toString()

            Text(
                text = scoreStr,
                color = TextPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = Strings.radarScore(language),
                color = TextSecondary,
                fontSize = 9.sp
            )

            Spacer(modifier = Modifier.height(2.dp))

            val (confLabel, confColor) = when (confidence) {
                SignalConfidence.HIGH -> Pair(Strings.confHigh(language), BullishGreen)
                SignalConfidence.MEDIUM -> Pair(Strings.confMedium(language), RadarCyan)
                SignalConfidence.CAUTION -> Pair(Strings.confCaution(language), NeutralAmber)
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(confColor.copy(alpha = 0.2f))
                    .padding(horizontal = 4.dp, vertical = 1.dp)
            ) {
                Text(
                    text = confLabel,
                    color = confColor,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
