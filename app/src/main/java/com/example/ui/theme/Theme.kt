package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val RadarDarkColorScheme = darkColorScheme(
    primary = RadarCyan,
    onPrimary = Color(0xFF00363D),
    primaryContainer = Color(0xFF004F58),
    onPrimaryContainer = Color(0xFF97F0FF),
    secondary = RadarBlue,
    onSecondary = Color(0xFF003354),
    secondaryContainer = Color(0xFF004B76),
    onSecondaryContainer = Color(0xFFCAE6FF),
    tertiary = RadarPurple,
    onTertiary = Color(0xFF4A0080),
    tertiaryContainer = Color(0xFF6B18A5),
    onTertiaryContainer = Color(0xFFEEDCFF),
    background = RadarBg,
    onBackground = TextPrimary,
    surface = RadarSurface,
    onSurface = TextPrimary,
    surfaceVariant = RadarSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = RadarBorder,
    outlineVariant = RadarBorderLight,
    error = BearishRed,
    onError = Color.White
)

@Composable
fun CryptoRadarTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = RadarDarkColorScheme,
        typography = AppTypography,
        content = content
    )
}
