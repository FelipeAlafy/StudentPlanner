package net.felipealafy.studentplanner.ui.components.text.label

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import net.felipealafy.studentplanner.ui.theme.PlannerTheme
import net.felipealafy.studentplanner.ui.theme.Typography

@Composable
fun SubjectTitleForAnyCard(title: String) {
    Text(
        text = title,
        style = Typography.bodyLarge,
        color = PlannerTheme.colors.onSurface
    )
}

@Preview
@Composable
private fun SubjectTitleForAnyCardPreview() {
    SubjectTitleForAnyCard(
        title = "",
    )
}