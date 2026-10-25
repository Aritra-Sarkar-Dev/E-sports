package com.example.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color

val BgDark = Color(0xFF0A0A0F)
val CardSurface = Color(0xFF14141F)
val ElevatedSurface = Color(0xFF1B1B2B)
val CardBorder = Color(0xFF262640)
val ChipBg = Color(0xFF1B1B2B)

val NeonLime = Color(0xFFC8FF3D)
val ElectricViolet = Color(0xFF7B5CFF)
val GoldCoins = Color(0xFFFFB800)
val GoldPrize = Color(0xFFFFB800)
val LiveRed = Color(0xFFFF4D6D)
val StreakOrange = Color(0xFFFF6A00)
val FireOrange = Color(0xFFFF6A00)

val TextPrimary = Color(0xFFFFFFFF)
val TextSecondary = Color(0xFFA7A7C0)
val TextMuted = Color(0xFFA7A7C0)

val FFArenaColorScheme = darkColorScheme(
    primary = NeonLime,
    onPrimary = Color.Black,
    secondary = ElectricViolet,
    onSecondary = Color.White,
    tertiary = ElectricViolet,
    background = BgDark,
    onBackground = TextPrimary,
    surface = CardSurface,
    onSurface = TextPrimary,
    surfaceVariant = ElevatedSurface,
    onSurfaceVariant = TextSecondary,
    outline = CardBorder
)
