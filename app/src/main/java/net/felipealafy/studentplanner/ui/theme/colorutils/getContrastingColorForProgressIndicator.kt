package net.felipealafy.studentplanner.ui.theme.colorutils

import androidx.compose.ui.graphics.Color
import net.felipealafy.studentplanner.ui.theme.bluePallet
import net.felipealafy.studentplanner.ui.theme.greenPallet

fun Long.getContrastingColorForProgressIndicator(): Long {
    val color = Color(this)
    val luminance = (0.2126 * color.red + 0.7152 * color.green + 0.0722 * color.blue)
    return if (luminance > 0.5) {
        bluePallet[0]
    } else {
        greenPallet[4]
    }
}