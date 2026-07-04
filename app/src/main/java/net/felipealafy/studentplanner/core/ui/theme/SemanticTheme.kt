package net.felipealafy.studentplanner.core.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import net.felipealafy.studentplanner.core.ui.theme.colorutils.getContrastingColorForText
import net.felipealafy.studentplanner.ui.theme.colorutils.blendWith

data class SemanticTheme(
    val primary: Color,
    val onPrimary: Color,
    val container: Color,
    val onContainer: Color,
    val surface: Color,
    val onSurface: Color,
    val error: Color = ErrorRed,
    val onError: Color = Color.White,
    val success: Color = SuccessGreen,
    val onSuccess: Color = Color.White
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