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
import net.felipealafy.studentplanner.core.ui.theme.DarkGray
import net.felipealafy.studentplanner.core.ui.theme.LightGray
import net.felipealafy.studentplanner.core.ui.theme.PlannerTheme
import net.felipealafy.studentplanner.core.ui.theme.Red
import net.felipealafy.studentplanner.core.ui.theme.Typography

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
            focusedContainerColor = PlannerTheme.colors.container,
            disabledContainerColor = LightGray,
            errorContainerColor = Red,
            unfocusedTextColor = DarkGray,
            focusedTextColor = DarkGray,
            errorTextColor = Red,
            focusedLabelColor = DarkGray,
            focusedBorderColor = PlannerTheme.colors.primary,
            focusedPlaceholderColor = LightGray,
        ),
        label = {
            Text(
                text = stringResource(id = hint),
                color = PlannerTheme.colors.onContainer,
                style = Typography.labelSmall
            )
        },
        shape = RoundedCornerShape(25.dp),
    )
}