package net.felipealafy.studentplanner.core.ui.components.grade

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import net.felipealafy.studentplanner.R
import net.felipealafy.studentplanner.core.ui.theme.LightGray
import net.felipealafy.studentplanner.core.ui.theme.PlannerTheme
import net.felipealafy.studentplanner.core.ui.theme.Typography

@Composable
fun GradeWeightInput(
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
        colors = OutlinedTextFieldDefaults.colors(
            disabledContainerColor = LightGray,
            unfocusedTextColor = PlannerTheme.colors.onCardTop,
            focusedTextColor = PlannerTheme.colors.onCardTop,
            errorTextColor = PlannerTheme.colors.onError,
            focusedLabelColor = PlannerTheme.colors.onCardTop,
            focusedBorderColor = PlannerTheme.colors.primary,
            focusedPlaceholderColor = LightGray,
        ),
        label = {
            Text(
                text = stringResource(R.string.insert_grade_weight),
                style = Typography.labelSmall,
                color = PlannerTheme.colors.onContainer
            )
        },
        shape = RoundedCornerShape(25.dp),
    )
}