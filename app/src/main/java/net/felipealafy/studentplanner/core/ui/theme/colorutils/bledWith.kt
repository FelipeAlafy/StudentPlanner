package net.felipealafy.studentplanner.core.ui.theme.colorutils

import androidx.compose.ui.graphics.Color

fun Color.blendWith(background: Color, alpha: Float): Color {
    val safeAlpha = alpha.coerceIn(0f, 1f)
    val inverseAlpha = 1f - safeAlpha
    val r = (this.red * safeAlpha) + (background.red * inverseAlpha)
    val g = (this.green * safeAlpha) + (background.green * inverseAlpha)
    val b = (this.blue * safeAlpha) + (background.blue * inverseAlpha)

    return Color(red = r, green = g, blue = b, alpha = 1f)
}