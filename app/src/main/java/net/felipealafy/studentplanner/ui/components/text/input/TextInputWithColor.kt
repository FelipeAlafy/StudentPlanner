package net.felipealafy.studentplanner.ui.components.text.input

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.felipealafy.studentplanner.R
import net.felipealafy.studentplanner.ui.theme.DarkGray
import net.felipealafy.studentplanner.ui.theme.LightGray
import net.felipealafy.studentplanner.ui.theme.PlannerTheme
import net.felipealafy.studentplanner.ui.theme.Red
import net.felipealafy.studentplanner.ui.theme.Typography
import net.felipealafy.studentplanner.ui.theme.colorPallet
import net.felipealafy.studentplanner.ui.theme.colorutils.getContrastingColorForText

@Composable
fun TextInputWithColor(
    text: String,
    onValueChange: (String) -> Unit,
    hint: Int
) {
    OutlinedTextField(
        onValueChange = onValueChange,
        value = text,
        textStyle = Typography.bodyMedium,
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = LightGray,
            disabledContainerColor = LightGray,
            errorContainerColor = Red,
            unfocusedTextColor = DarkGray,
            focusedTextColor = DarkGray,
            errorTextColor = Red,
            focusedLabelColor = DarkGray,
            focusedBorderColor = PlannerTheme.colors.container,
            focusedPlaceholderColor = LightGray,
        ),
        label = {
            Text(
                text = stringResource(id = hint),
                color = PlannerTheme.colors.onSurface,
                style = Typography.labelSmall
            )
        },
        shape = RoundedCornerShape(25.dp),
    )
}

@Preview
@Composable
private fun TextInputWithColorPreview() {
    TextInputWithColor(
        text = "",
        onValueChange = {},
        hint = R.string.planner_name_input
    )
}