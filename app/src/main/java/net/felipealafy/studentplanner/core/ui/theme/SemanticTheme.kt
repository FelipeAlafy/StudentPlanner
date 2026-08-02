package net.felipealafy.studentplanner.core.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import net.felipealafy.studentplanner.core.ui.theme.colorutils.blendWith
import net.felipealafy.studentplanner.core.ui.theme.colorutils.getContrastingColorForText

data class SemanticTheme(
    val primary: Color,
    val onPrimary: Color,
    val container: Color,
    val onContainer: Color,
    val cardTop: Color,
    val onCardTop: Color,
    val surface: Color,
    val onSurface: Color,
    val error: Color = ErrorRed,
    val onError: Color = Color.White,
    val success: Color = SuccessGreen,
    val onSuccess: Color = Color.White,
    val background: Color = Color.White,
    val gray: Color = Color.DarkGray,
    val onGray: Color = Color.White,
    val yellow: Color = Color(0xFFf9c440),
    val onYellow: Color = Color.Black
)

fun generateThemeFromColor(baseColor: Long, isDarkTheme: Boolean = false): SemanticTheme {
    val primaryColor = Color(baseColor)
    val textOnPrimary = Color(baseColor.getContrastingColorForText())

    val appBackground = if (isDarkTheme) DarkBackground else LightBackground

    val containerSolid = primaryColor.blendWith(appBackground, alpha = 0.10F)
    val cardTop = primaryColor.blendWith(appBackground, alpha = 0.75F)
    val surfaceSolid = primaryColor.blendWith(appBackground, alpha = 0.2F)
    val onSurfaceSolid = surfaceSolid.getContrastingColorForText()

    val textColorBasedOnBackground = if (isDarkTheme) White else SpaceGray

    return SemanticTheme(
        primary = primaryColor,
        onPrimary = textOnPrimary,
        container = containerSolid,
        onContainer = textColorBasedOnBackground,
        cardTop = cardTop,
        onCardTop = textOnPrimary,
        surface = surfaceSolid,
        onSurface = onSurfaceSolid,
        background = Color.White
    )
}

val LocalSemanticTheme = staticCompositionLocalOf <SemanticTheme> {
    error("No theme was provided! Please wrap your view with PlannerThemeProvider.")
}

@Composable
fun PlannerThemeProvider(
    baseColor: Long,
    isDarkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val semanticTheme = generateThemeFromColor(baseColor, isDarkTheme)

    CompositionLocalProvider(
        LocalSemanticTheme provides semanticTheme
    ) {
        content()
    }
}

object PlannerTheme {
    val colors: SemanticTheme
        @Composable
        @ReadOnlyComposable
        get() = LocalSemanticTheme.current
}