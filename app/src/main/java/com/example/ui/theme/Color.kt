package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// STEP 1 - DESIGN SYSTEM COLORS
val BgDark = Color(0xFF0A0A0F)
val VioletRadialGlow = Color(0xFF2B1B6B)
val CardSurface = Color(0xFF14141F)
val CardBorder = Color(0xFF23233A)
val InputBorder = Color(0xFF2E2E4A)

// Primary accent: acid lime #C8FF3D (buttons, active states, prize amounts)
val AcidLime = Color(0xFFC8FF3D)

// Secondary: violet #7B5CFF
val SecondaryViolet = Color(0xFF7B5CFF)

// Deep purple gradient #3A24A8 to #17113D for hero cards
val DeepPurpleStart = Color(0xFF3A24A8)
val DeepPurpleEnd = Color(0xFF17113D)

// Text: primary #F5F5FA, muted #9498AE, error/danger #FF7A8A
val TextPrimary = Color(0xFFF5F5FA)
val TextMuted = Color(0xFF9498AE)
val ErrorDanger = Color(0xFFFF7A8A)

// Podiums & status accents
val GoldPodium = Color(0xFFFFD700)
val SilverPodium = Color(0xFFC0C0C0)
val BronzePodium = Color(0xFFCD7F32)
val LivePulseRed = Color(0xFFFF385C)
val VerifiedBadge = Color(0xFF3897F0)
val ChipSelectedBg = AcidLime
val ChipSelectedText = Color(0xFF0A0A0F)

val HeroCardGradient = Brush.verticalGradient(
    colors = listOf(DeepPurpleStart, DeepPurpleEnd)
)
