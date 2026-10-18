package com.widgetfy.com.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// Liquid Glass Design System — Translated from Apple SwiftUI to Jetpack Compose
// Based on haider-nawaz/liquid-glass-skill: .glassEffect(), GlassEffectContainer, backgroundExtensionEffect()

object LiquidGlass {
    // Glass styles: .regular, .clear, .identity
    val Regular = GlassStyle(alpha = 0.12f, borderAlpha = 0.18f, blur = 12.dp)
    val Clear = GlassStyle(alpha = 0.06f, borderAlpha = 0.12f, blur = 20.dp)
    val Interactive = GlassStyle(alpha = 0.16f, borderAlpha = 0.22f, blur = 16.dp)

    data class GlassStyle(val alpha: Float, val borderAlpha: Float, val blur: Dp)

    // Tint support: .glassEffect(.regular.tint(.blue))
    fun tinted(base: GlassStyle, tint: Color): GlassStyle = base // for compose, tint via overlay

    // Primary liquid glass colors
    val GlassWhite = Color(0xFFF8FAFF)
    val GlassBorder = Color.White.copy(alpha = 0.18f)
    val GlassHighlight = Color.White.copy(alpha = 0.35f)
    val GlassShadow = Color(0xFF6750A4).copy(alpha = 0.15f)

    // Background extension effect colors (hero gradient behind glass)
    val BgGradient = Brush.linearGradient(
        listOf(
            Color(0xFFE9DDFF), // light violet
            Color(0xFFC7F1ED), // teal tint
            Color(0xFFFFE4E6), // rose tint
            Color(0xFFFFFBFE)
        )
    )
    val BgGradientDark = Brush.linearGradient(
        listOf(
            Color(0xFF1A1033),
            Color(0xFF0F2E2B),
            Color(0xFF2E0A1A),
            Color(0xFF141218)
        )
    )
    // Aurora orbs for liquid glass refraction
    val OrbViolet = Color(0xFF7B61FF).copy(alpha = 0.35f)
    val OrbTeal = Color(0xFF00D1C1).copy(alpha = 0.30f)
    val OrbRose = Color(0xFFFF6B8A).copy(alpha = 0.28f)
    val OrbBlue = Color(0xFF60A5FA).copy(alpha = 0.25f)
}

@Composable
fun Modifier.liquidGlass(
    style: LiquidGlass.GlassStyle = LiquidGlass.Regular,
    shape: RoundedCornerShape = RoundedCornerShape(24.dp),
    tint: Color? = null
): Modifier {
    val baseAlpha = style.alpha
    val borderAlpha = style.borderAlpha
    return this
        .clip(shape)
        .background(
            Brush.linearGradient(
                colors = listOf(
                    Color.White.copy(alpha = baseAlpha + 0.08f),
                    Color.White.copy(alpha = baseAlpha * 0.5f),
                    Color.White.copy(alpha = baseAlpha)
                )
            ),
            shape = shape
        )
        .border(BorderStroke(1.dp, Color.White.copy(alpha = borderAlpha)), shape)
        // Blur is optional — only for Android S+ blur behind glass; Compose blur blurs content itself, so we use subtle shadow instead
        .shadow(8.dp, shape, spotColor = LiquidGlass.GlassShadow, ambientColor = Color.Black.copy(alpha = 0.08f))
}

@Composable
fun Modifier.liquidGlassCard(
    cornerRadius: Dp = 24.dp,
    tint: Color? = null
): Modifier = liquidGlass(
    style = LiquidGlass.Regular,
    shape = RoundedCornerShape(cornerRadius),
    tint = tint
)

@Composable
fun Modifier.liquidGlassInteractive(
    cornerRadius: Dp = 24.dp
): Modifier = liquidGlass(
    style = LiquidGlass.Interactive,
    shape = RoundedCornerShape(cornerRadius)
)

// GlassEffectContainer equivalent — groups glass elements with shared sampling (spacing for morphing)
@Composable
fun GlassEffectContainer(
    spacing: Dp = 16.dp,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(modifier = modifier) {
        content()
    }
}

// BackgroundExtensionEffect equivalent — for hero images behind glass navigation
@Composable
fun LiquidGlassBackground(modifier: Modifier = Modifier, isDark: Boolean = false) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(if (isDark) LiquidGlass.BgGradientDark else LiquidGlass.BgGradient)
    ) {
        // Aurora orbs: simulate refraction
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(LiquidGlass.OrbViolet, Color.Transparent),
                        radius = 800f
                    )
                )
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(LiquidGlass.OrbTeal, Color.Transparent),
                        radius = 600f
                    )
                )
        )
    }
}
