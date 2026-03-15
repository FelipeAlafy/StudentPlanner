package net.felipealafy.studentplanner.ui.components.grade

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
import net.felipealafy.studentplanner.ui.theme.Red
import net.felipealafy.studentplanner.ui.theme.Typography
import net.felipealafy.studentplanner.ui.theme.colorPallet
import net.felipealafy.studentplanner.ui.views.getContrastingColorForText

@Composable
fun MinimumGradeToPassInput(text: String, onValueChange: (String) -> Unit, selectedColor: Long) {
    OutlinedTextField(
        onValueChange = onValueChange,
        value = text,
        textStyle = Typography.bodyMedium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = LightGray,
            disabledContainerColor = LightGray,
            errorContainerColor = Red,
            unfocusedTextColor = Color(selectedColor.getContrastingColorForText()),
            focusedTextColor = Color(selectedColor.getContrastingColorForText()),
            errorTextColor = Red,
            focusedLabelColor = DarkGray,
            focusedBorderColor = Color(selectedColor),
            focusedPlaceholderColor = LightGray,
        ),
        label = {
            Text(
                text = stringResource(R.string.minimum_grade_to_pass),
                style = Typography.labelSmall,
                color = Color(selectedColor.getContrastingColorForText())
            )
        },
        shape = RoundedCornerShape(25.dp),
    )
}

@Preview
@Composable
private fun MinimumGradeToPassInputPreview() {
    MinimumGradeToPassInput(
        text = "Example",
        onValueChange = { println(it) },
        selectedColor = colorPallet[0][1]
    )
}