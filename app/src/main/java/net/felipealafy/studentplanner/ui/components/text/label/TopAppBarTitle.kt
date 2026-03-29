package net.felipealafy.studentplanner.ui.components.text.label

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import net.felipealafy.studentplanner.ui.theme.PlannerTheme
import net.felipealafy.studentplanner.ui.theme.Typography
import net.felipealafy.studentplanner.ui.theme.colorPallet

@Composable
fun TopAppBarTitle(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Center
) {
    val tint = PlannerTheme.colors.onSurface
    var textStyle by rememberSaveable { mutableStateOf(Typography.headlineMedium) }
    Text(
        text = text,
        color = tint,
        modifier = modifier,
        style = Typography.headlineMedium,
        textAlign = textAlign,
        maxLines = 1,
        onTextLayout = {
            if (it.hasVisualOverflow) {
                textStyle = textStyle.copy(fontSize = textStyle.fontSize * 0.9)
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun TopAppBarBarTitlePreview() {
    TopAppBarTitle(
        text = "Example",
    )
}