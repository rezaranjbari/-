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
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.ChangeHistory
import androidx.compose.material.icons.filled.HorizontalRule
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.RadarOpportunity
import com.example.ui.localization.AppLanguage
import com.example.ui.localization.PersianUtils
import com.example.ui.localization.Strings
import com.example.ui.theme.BearishRed
import com.example.ui.theme.BearishRedGlow
import com.example.ui.theme.BullishGreen
import com.example.ui.theme.BullishGreenGlow
import com.example.ui.theme.NeutralAmber
import com.example.ui.theme.RadarBorder
import com.example.ui.theme.RadarCyan
import com.example.ui.theme.RadarSurface
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.util.Locale

@Composable
fun ScenarioCard(
    opportunity: RadarOpportunity,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(RadarSurface)
            .border(1.dp, RadarBorder, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        // Section Title
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = Strings.scenariosHeader(language),
                color = TextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )

            // Risk Reward Pill
            val rrText = String.format(Locale.US, "1:%.1f", opportunity.riskRewardRatio)
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(RadarCyan.copy(alpha = 0.15f))
                    .border(1.dp, RadarCyan.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(
                    text = if (language == AppLanguage.PERSIAN)
                        "${Strings.riskReward(language)}: ${PersianUtils.toPersianDigits(rrText)}"
                    else
                        "${Strings.riskReward(language)}: $rrText",
                    color = RadarCyan,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 1. Bullish Scenario Box
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(BullishGreenGlow)
                .border(1.dp, BullishGreen.copy(alpha = 0.35f), RoundedCornerShape(12.dp))
                .padding(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.ArrowUpward,
                    contentDescription = null,
                    tint = BullishGreen,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = Strings.bullishScenario(language),
                    color = BullishGreen,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = Strings.entryZone(language), color = TextSecondary, fontSize = 10.sp)
                    Text(
                        text = "${PersianUtils.formatPrice(opportunity.entryMin, language)} - ${PersianUtils.formatPrice(opportunity.entryMax, language)}",
                        color = TextPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(text = Strings.target1(language), color = TextSecondary, fontSize = 10.sp)
                    Text(
                        text = PersianUtils.formatPrice(opportunity.target1, language),
                        color = BullishGreen,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = opportunity.bullishScenarioFa,
                color = TextPrimary,
                fontSize = 11.sp,
                lineHeight = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 2. Bearish Scenario Box
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(BearishRedGlow)
                .border(1.dp, BearishRed.copy(alpha = 0.35f), RoundedCornerShape(12.dp))
                .padding(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.ArrowDownward,
                    contentDescription = null,
                    tint = BearishRed,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = Strings.bearishScenario(language),
                    color = BearishRed,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = Strings.stopLoss(language), color = TextSecondary, fontSize = 10.sp)
                    Text(
                        text = PersianUtils.formatPrice(opportunity.stopLoss, language),
                        color = BearishRed,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(text = Strings.invalidationLevel(language), color = TextSecondary, fontSize = 10.sp)
                    Text(
                        text = PersianUtils.formatPrice(opportunity.invalidationLevel, language),
                        color = BearishRed,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = opportunity.bearishScenarioFa,
                color = TextPrimary,
                fontSize = 11.sp,
                lineHeight = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 3. Neutral Scenario Box
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(NeutralAmber.copy(alpha = 0.1f))
                .border(1.dp, NeutralAmber.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                .padding(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.HorizontalRule,
                    contentDescription = null,
                    tint = NeutralAmber,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = Strings.neutralScenario(language),
                    color = NeutralAmber,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = opportunity.neutralScenarioFa,
                color = TextPrimary,
                fontSize = 11.sp,
                lineHeight = 16.sp
            )
        }
    }
}
