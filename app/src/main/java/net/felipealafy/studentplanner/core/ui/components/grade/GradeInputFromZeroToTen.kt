package net.felipealafy.studentplanner.core.ui.components.grade

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import net.felipealafy.studentplanner.R
import net.felipealafy.studentplanner.core.ui.theme.LightGray
import net.felipealafy.studentplanner.core.ui.theme.PlannerTheme
import net.felipealafy.studentplanner.core.ui.theme.Typography

@Composable
fun GradeInputFromZeroToTen(
    text: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        onValueChange = onValueChange,
        value = text,
        textStyle = Typography.bodyMedium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            disabledContainerColor = LightGray,
            unfocusedTextColor = PlannerTheme.colors.onSurface,
            focusedTextColor = PlannerTheme.colors.onSurface,
            errorTextColor = PlannerTheme.colors.onError,
            focusedLabelColor = PlannerTheme.colors.onSurface,
            focusedBorderColor = PlannerTheme.colors.primary,
            focusedPlaceholderColor = LightGray,
        ),
        label = {
            Text(
                text = stringResource(R.string.insert_grade_value),
                style = Typography.labelSmall,
                color = PlannerTheme.colors.onSurface
            )
        },
        shape = RoundedCornerShape(25.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}