package net.felipealafy.studentplanner.core.ui.components.color.chooser

import androidx.annotation.StringRes
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import net.felipealafy.studentplanner.core.ui.theme.PlannerTheme
import net.felipealafy.studentplanner.core.ui.theme.Typography

@Composable
fun ButtonWithBackgroundColor(
    onClick: () -> Unit,
    @StringRes placeholderTextPath: Int,
    isButtonEnabled: Boolean = true
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = PlannerTheme.colors.primary,
        ),
        enabled = isButtonEnabled,
    ) {
        Text(
            stringResource(placeholderTextPath),
            style = Typography.labelLarge,
            color = PlannerTheme.colors.onPrimary
        )
    }
}