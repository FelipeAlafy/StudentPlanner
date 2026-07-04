package net.felipealafy.studentplanner.core.ui.theme.colorutils

import androidx.compose.ui.graphics.Color

fun Long.getContrastingButtonColor(): Long {
    val color = Color(this)
    val luminance = 0.2126 * color.red + 0.7152 * color.green + 0.0722 * color.blue

    return if (luminance > 0.5) {
        this.darken(0.6f)
    } else {
        this.lighten(1.4f)
    }
}