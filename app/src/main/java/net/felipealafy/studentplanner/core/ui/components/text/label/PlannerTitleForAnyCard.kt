import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import net.felipealafy.studentplanner.core.ui.theme.PlannerTheme
import net.felipealafy.studentplanner.core.ui.theme.Typography

@Composable
fun PlannerTitleForAnyCard(title: String) {
    Text(
        text = title,
        style = Typography.bodyLarge,
        color = PlannerTheme.colors.onPrimary
    )
}
