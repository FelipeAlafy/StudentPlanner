package net.felipealafy.studentplanner.ui.components.text.label

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import net.felipealafy.studentplanner.R
import net.felipealafy.studentplanner.ui.theme.PlannerTheme
import net.felipealafy.studentplanner.ui.theme.Typography

@Composable
fun ClassesTaken(count: Int) {
    Row {
        Icon(
            painter = painterResource(R.drawable.resource_class),
            contentDescription = stringResource(R.string.class_icon)
        )
        Text(
            text = "${stringResource(R.string.classes_taken)} ${count} ${
                stringResource(
                    R.string.classes
                )
            }.",
            style = Typography.labelLarge,
            color = PlannerTheme.colors.onContainer
        )
    }
}


@Preview
@Composable
private fun ClassesTakenPreview() {
    ClassesTaken(
        count = 10
    )
}