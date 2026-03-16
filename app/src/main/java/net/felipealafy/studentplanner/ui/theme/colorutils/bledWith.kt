package net.felipealafy.studentplanner.ui.theme.colorutils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp

fun Color.blendWith(backgroundColor: Color, alpha: Float): Color {
    val alphaInRange = alpha.coerceIn(0f, 1f)
    return lerp(backgroundColor, this, alphaInRange)
}