package net.felipealafy.studentplanner.ui.components.color.chooser

import androidx.annotation.StringRes
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import net.felipealafy.studentplanner.ui.theme.Typography
import net.felipealafy.studentplanner.ui.views.getContrastingColorForText

@Composable
fun ButtonWithBackgroundColor(
    onClick: () -> Unit,
    selectedColor: Long,
    @StringRes placeholderTextPath: Int,
    isButtonEnabled: Boolean = true
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(selectedColor).copy(alpha = 0.8F)
        ),
        enabled = isButtonEnabled,
    ) {
        Text(
            stringResource(placeholderTextPath),
            style = Typography.labelSmall,
            color = Color(selectedColor.getContrastingColorForText())
        )
    }
}