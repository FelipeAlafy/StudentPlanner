package net.felipealafy.studentplanner.ui.theme.colorutils

import androidx.compose.ui.graphics.Color

fun Long.getContrastingColorForText(): Long {
    val color = Color(this)
    val luminance = (0.2126 * color.red + 0.7152 * color.green + 0.0722 * color.blue)
    return if (luminance > 0.5) {
        0xFF373737
    } else {
        0xFFFFFFFF
    }
}