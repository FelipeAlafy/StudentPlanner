package net.felipealafy.studentplanner.ui.components.text.label

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import net.felipealafy.studentplanner.datamodels.Planner
import net.felipealafy.studentplanner.ui.theme.Typography
import net.felipealafy.studentplanner.ui.theme.colorPallet
import net.felipealafy.studentplanner.ui.views.getContrastingColorForText

@Composable
fun PlannerTitleForAnyCard(title: String, planner: Planner) {
    Text(
        text = title,
        style = Typography.bodyLarge,
        color = Color(planner.color.getContrastingColorForText())
    )
}

@Preview
@Composable
private fun PlannerTitleForAnyCardPreview() {
    PlannerTitleForAnyCard(
        title = "Example",
        planner = Planner(
            name = "Example",
            color = colorPallet[0][1],
            minimumGradeToPass = 70F
        )
    )
}