package com.example.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// Shapes specification:
// Cards 22px (22.dp) radius, hero cards 28px (28.dp), buttons 18px (18.dp), chips fully rounded
val CardShape = RoundedCornerShape(22.dp)
val HeroCardShape = RoundedCornerShape(28.dp)
val ButtonShape = RoundedCornerShape(18.dp)
val ChipShape = RoundedCornerShape(50.dp)
val InputShape = RoundedCornerShape(16.dp)

val FFArenaShapes = Shapes(
    small = RoundedCornerShape(12.dp),
    medium = CardShape,
    large = HeroCardShape,
    extraLarge = RoundedCornerShape(32.dp)
)

val FFArenaDarkColorScheme = darkColorScheme(
    primary = AcidLime,
    onPrimary = Color(0xFF0A0A0F),
    secondary = SecondaryViolet,
    onSecondary = Color.White,
    background = BgDark,
    onBackground = TextPrimary,
    surface = CardSurface,
    onSurface = TextPrimary,
    surfaceVariant = Color(0xFF1B1B2A),
    onSurfaceVariant = TextMuted,
    outline = CardBorder,
    outlineVariant = InputBorder,
    error = ErrorDanger,
    onError = Color.Black
)

@Composable
fun FFArenaTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = FFArenaDarkColorScheme,
        typography = AppTypography,
        shapes = FFArenaShapes
    ) {
        // Dark theme background #0A0A0F with a soft violet radial glow at the top-right (#2B1B6B fading out)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BgDark)
                .drawBehind {
                    val radius = size.maxDimension * 0.75f
                    val center = Offset(x = size.width * 0.95f, y = size.height * 0.05f)
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                VioletRadialGlow.copy(alpha = 0.55f),
                                VioletRadialGlow.copy(alpha = 0.18f),
                                Color.Transparent
                            ),
                            center = center,
                            radius = radius
                        ),
                        radius = radius,
                        center = center
                    )
                }
        ) {
            content()
        }
    }
}
