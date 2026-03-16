package net.felipealafy.studentplanner.ui.theme

import androidx.compose.ui.graphics.Color
import net.felipealafy.studentplanner.ui.theme.colorutils.blendWith
import net.felipealafy.studentplanner.ui.theme.colorutils.getContrastingColorForText

data class SemanticTheme(
    val primary: Color,
    val onPrimary: Color,
    val container: Color,
    val onContainer: Color,
    val surface: Color,
    val onSurface: Color
)

fun generateThemeFromColor(baseColor: Long, isDarkTheme: Boolean = false): SemanticTheme {
    val primaryColor = Color(baseColor)
    val textOnPrimary = Color(baseColor.getContrastingColorForText())

    val appBackground = if (isDarkTheme) DarkBackground else LightBackground

    val containerSolid = primaryColor.blendWith(appBackground, alpha = 0.10F)
    val surfaceSolid = primaryColor.blendWith(appBackground, alpha = 0.25F)

    val textColorBasedOnBackground = if (isDarkTheme) White else SpaceGray

    return SemanticTheme(
        primary = primaryColor,
        onPrimary = textOnPrimary,
        container = containerSolid,
        onContainer = textColorBasedOnBackground,
        surface = surfaceSolid,
        onSurface = textOnPrimary
    )
}