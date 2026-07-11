package net.felipealafy.studentplanner.core.ui.theme.colorutils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance

fun Long.getContrastingColorForText(): Long {
    val backgroundColor = Color(this)

    val contrastWithWhite = calculateContrastRatio(backgroundColor, Color.White)
    val contrastWithBlack = calculateContrastRatio(backgroundColor, Color.Black)

    return if (contrastWithBlack > contrastWithWhite) {
        0xFF1A1A1A
    } else {
        0xFFFFFFFF
    }
}

fun Color.getContrastingColorForText(): Color {
    val contrastWithWhite = calculateContrastRatio(this, Color.White)
    val contrastWithBlack = calculateContrastRatio(this, Color.Black)

    return if (contrastWithBlack > contrastWithWhite) {
        Color(0xFF1A1A1A) // Cinza Escuro
    } else {
        Color(0xFFFFFFFF) // Branco
    }
}

fun calculateContrastRatio(color1: Color, color2: Color): Float {
    val l1 = color1.luminance()
    val l2 = color2.luminance()

    val lighter = maxOf(l1, l2)
    val darker = minOf(l1, l2)

    return (lighter + 0.05f) / (darker + 0.05f)
}