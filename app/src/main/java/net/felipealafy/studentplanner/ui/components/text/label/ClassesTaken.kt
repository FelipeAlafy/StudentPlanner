package net.felipealafy.studentplanner.ui.components.text.label

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import net.felipealafy.studentplanner.R
import net.felipealafy.studentplanner.datamodels.Subject
import net.felipealafy.studentplanner.ui.theme.Typography
import net.felipealafy.studentplanner.ui.theme.colorPallet
import net.felipealafy.studentplanner.ui.views.getContrastingColorForText
import java.time.LocalDateTime

@Composable
fun ClassesTaken(subject: Subject) {
    Row {
        Icon(
            painter = painterResource(R.drawable.resource_class),
            contentDescription = stringResource(R.string.class_icon)
        )
        Text(
            text = "${stringResource(R.string.classes_taken)} ${subject.studentClasses.count()} ${
                stringResource(
                    R.string.classes
                )
            }.",
            style = Typography.labelLarge,
            color = Color(subject.color.getContrastingColorForText())
        )
    }
}


@Preview
@Composable
private fun ClassesTakenPreview() {
    ClassesTaken(
        Subject(
            plannerId = "",
            name = "Example",
            color = colorPallet[0][1],
            start = LocalDateTime.now(),
            end = LocalDateTime.now()
        )
    )
}