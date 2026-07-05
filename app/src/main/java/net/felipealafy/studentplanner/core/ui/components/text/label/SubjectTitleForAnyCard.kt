package net.felipealafy.studentplanner.core.ui.components.text.label

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import net.felipealafy.studentplanner.core.ui.theme.PlannerTheme
import net.felipealafy.studentplanner.core.ui.theme.Typography

@Composable
fun SubjectTitleForAnyCard(title: String) {
    Text(
        text = title,
        style = Typography.bodyLarge,
        color = PlannerTheme.colors.onPrimary
    )
}