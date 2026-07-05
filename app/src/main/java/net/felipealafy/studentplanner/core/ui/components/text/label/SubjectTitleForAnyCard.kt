import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import net.felipealafy.studentplanner.core.ui.theme.PlannerTheme
import net.felipealafy.studentplanner.core.ui.theme.Typography
import net.felipealafy.studentplanner.feature_subject.domain.model.Subject

@Composable
fun SubjectTitleForAnyCard(title: String, subject: Subject) {
    Text(
        text = title,
        style = Typography.bodyLarge,
        color = PlannerTheme.colors.onPrimary
    )
}