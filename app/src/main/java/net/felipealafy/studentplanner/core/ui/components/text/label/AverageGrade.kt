package net.felipealafy.studentplanner.core.ui.components.text.label

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import net.felipealafy.studentplanner.R
import net.felipealafy.studentplanner.core.ui.theme.PlannerTheme
import net.felipealafy.studentplanner.core.ui.theme.Typography
import net.felipealafy.studentplanner.core.ui.extensions.getValueInDisplayStyleForAverage
import net.felipealafy.studentplanner.feature_exams.domain.use_case.GradeStyle

@Composable
fun AverageGrade(
    averageGradeFormatted: String,
    @StringRes text: Int = R.string.average_grade_for_all_subjects
) {
    Row {
        Icon(
            painter = painterResource(R.drawable.grade),
            contentDescription = stringResource(R.string.grade)
        )
        Text(
            text = stringResource(text) +
                    " $averageGradeFormatted",
            style = Typography.labelLarge,
            color = PlannerTheme.colors.onSurface
        )
    }
}

@Preview
@Composable
private fun AverageGradePreview() {
    AverageGrade(
        averageGradeFormatted = GradeStyle.FROM_ZERO_TO_ONE_HUNDRED.getValueInDisplayStyleForAverage(10.0F)
    )
}