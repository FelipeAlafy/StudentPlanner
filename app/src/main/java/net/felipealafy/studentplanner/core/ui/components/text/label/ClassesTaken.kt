package net.felipealafy.studentplanner.core.ui.components.text.label

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import net.felipealafy.studentplanner.R
import net.felipealafy.studentplanner.core.ui.theme.PlannerTheme
import net.felipealafy.studentplanner.core.ui.theme.Typography
import net.felipealafy.studentplanner.feature_subject.domain.model.DetailedSubject

@Composable
fun ClassesTaken(subject: DetailedSubject) {
    Row {
        Icon(
            painter = painterResource(R.drawable.resource_class),
            contentDescription = stringResource(R.string.class_icon)
        )
        Text(
            text = "${stringResource(R.string.classes_taken)} ${subject.countClassesTaken} ${
                stringResource(
                    R.string.classes
                )
            }.",
            style = Typography.labelLarge,
            color = PlannerTheme.colors.onPrimary
        )
    }
}