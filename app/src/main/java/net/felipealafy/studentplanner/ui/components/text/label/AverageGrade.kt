package net.felipealafy.studentplanner.ui.components.text.label

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import net.felipealafy.studentplanner.R
import net.felipealafy.studentplanner.datamodels.GradeStyle
import net.felipealafy.studentplanner.datamodels.Subject
import net.felipealafy.studentplanner.ui.theme.Typography
import net.felipealafy.studentplanner.ui.theme.colorPallet
import net.felipealafy.studentplanner.ui.views.getContrastingColorForText
import net.felipealafy.studentplanner.ui.views.getExamsAverage
import net.felipealafy.studentplanner.ui.views.getValueInDisplayStyleForAverage
import java.time.LocalDateTime

@Composable
fun AverageGrade(
    displayStyle: GradeStyle = GradeStyle.FROM_ZERO_TO_ONE_HUNDRED,
    subject: Subject,
    @StringRes text: Int = R.string.average_grade_for_all_subjects
) {
    Row {
        Icon(
            painter = painterResource(R.drawable.grade),
            contentDescription = stringResource(R.string.grade)
        )
        Text(
            text = stringResource(text) +
                    " ${displayStyle.getValueInDisplayStyleForAverage(average = subject.exams.getExamsAverage())}",
            style = Typography.labelLarge,
            color = Color(subject.color.getContrastingColorForText())
        )
    }
}

@Preview
@Composable
private fun AverageGradePreview() {
    AverageGrade(
        subject = Subject(
            plannerId = "",
            name = "Example",
            color = colorPallet[0][1],
            start = LocalDateTime.now(),
            end = LocalDateTime.now()
        )
    )
}