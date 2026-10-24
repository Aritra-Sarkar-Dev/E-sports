package com.example.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color

val BgDark = Color(0xFF0A0A0F)
val CardSurface = Color(0xFF14141F)
val CardBorder = Color(0xFF23233A)
val ChipBg = Color(0xFF1C1C2E)

val NeonLime = Color(0xFFC8FF3D)
val FireOrange = Color(0xFFFF6A00)
val ElectricViolet = Color(0xFF7B5CFF)
val GoldPrize = Color(0xFFFFB300)
val LiveRed = Color(0xFFFF4D6D)
val TextPrimary = Color(0xFFF5F5FA)
val TextMuted = Color(0xFF9498AE)

val FFArenaColorScheme = darkColorScheme(
    primary = NeonLime,
    onPrimary = Color.Black,
    secondary = FireOrange,
    onSecondary = Color.White,
    tertiary = ElectricViolet,
    background = BgDark,
    onBackground = TextPrimary,
    surface = CardSurface,
    onSurface = TextPrimary,
    surfaceVariant = ChipBg,
    onSurfaceVariant = TextMuted,
    outline = CardBorder
)
