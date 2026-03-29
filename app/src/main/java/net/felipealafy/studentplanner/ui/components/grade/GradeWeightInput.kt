package net.felipealafy.studentplanner.ui.components.grade

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import net.felipealafy.studentplanner.R
import net.felipealafy.studentplanner.ui.theme.LightGray
import net.felipealafy.studentplanner.ui.theme.Typography
import net.felipealafy.studentplanner.ui.theme.colorutils.getContrastingColorForText
import net.felipealafy.studentplanner.ui.theme.colorutils.getForBackgroundBasedOnTitleBarColor

@Composable
fun GradeWeightInput(
    text: String,
    onValueChange: (String) -> Unit,
    onValidate: () -> Unit,
    invalidDigit: Boolean,
    selectedColor: Long
) {
    OutlinedTextField(
        onValueChange = onValueChange,
        value = text,
        textStyle = Typography.bodyMedium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp)
            .onFocusChanged {
                if (!it.isFocused) {
                    onValidate()
                }
            },
        singleLine = true,
        isError = invalidDigit,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = LightGray,
            disabledContainerColor = LightGray,
            unfocusedTextColor = Color(selectedColor.getContrastingColorForText()),
            focusedTextColor = Color(selectedColor.getContrastingColorForText()),
            errorTextColor = Color(
                selectedColor.getForBackgroundBasedOnTitleBarColor().getContrastingColorForText()
            ),
            focusedLabelColor = Color(
                selectedColor.getForBackgroundBasedOnTitleBarColor().getContrastingColorForText()
            ),
            focusedBorderColor = Color(selectedColor),
            focusedPlaceholderColor = LightGray,
        ),
        label = {
            Text(
                text = stringResource(R.string.insert_grade_weight),
                style = Typography.labelSmall,
                color = Color(selectedColor.getContrastingColorForText())
            )
        },
        shape = RoundedCornerShape(25.dp),
    )
}