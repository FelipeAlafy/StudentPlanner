package net.felipealafy.studentplanner.core.ui.components.text.label

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import net.felipealafy.studentplanner.core.ui.theme.PlannerTheme
import net.felipealafy.studentplanner.core.ui.theme.Typography

@Composable
fun TopAppBarTitle(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Center
) {
    var textStyle by remember { mutableStateOf(Typography.headlineMedium) }
    Text(
        text = text,
        color = PlannerTheme.colors.onSurface,
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