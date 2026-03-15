package net.felipealafy.studentplanner.ui.components.text.label

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import net.felipealafy.studentplanner.datamodels.Subject
import net.felipealafy.studentplanner.ui.theme.Typography
import net.felipealafy.studentplanner.ui.theme.colorPallet
import net.felipealafy.studentplanner.ui.views.getContrastingColorForText
import java.time.LocalDateTime

@Composable
fun SubjectTitleForAnyCard(title: String, subject: Subject) {
    Text(
        text = title,
        style = Typography.bodyLarge,
        color = Color(subject.color.getContrastingColorForText())
    )
}

@Preview
@Composable
private fun SubjectTitleForAnyCardPreview() {
    SubjectTitleForAnyCard(
        title = "",
        subject = Subject(
            plannerId = "",
            name = "Exemple",
            color = colorPallet[0][1],
            start = LocalDateTime.now(),
            end = LocalDateTime.now()
        )
    )
}