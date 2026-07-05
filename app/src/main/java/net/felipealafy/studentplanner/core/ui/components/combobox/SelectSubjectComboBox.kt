package net.felipealafy.studentplanner.core.ui.components.combobox

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import net.felipealafy.studentplanner.R
import net.felipealafy.studentplanner.core.ui.theme.DarkGray
import net.felipealafy.studentplanner.core.ui.theme.PlannerTheme
import net.felipealafy.studentplanner.core.ui.theme.Typography
import net.felipealafy.studentplanner.feature_subject.domain.model.Subject
import java.time.LocalDateTime
import kotlin.collections.forEach

@Composable
fun SelectSubjectComboBox(
    subjectId: String,
    subjects: List<Subject>,
    onSubjectSelected: (id: String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val selectASubjectText = stringResource(R.string.select_a_subject)
    val subject = remember(subjectId, subjects) {
        subjects.find { it.id == subjectId }
            ?: Subject(
                name = selectASubjectText,
                id = "",
                plannerId = "",
                color = 0x00000,
                start = LocalDateTime.now(),
                end = LocalDateTime.now(),
            )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .border(
                width = 2.dp,
                color = DarkGray,
                shape = RoundedCornerShape(30.dp)
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    expanded = true
                }
            ) {
                Icon(
                    painter = painterResource(R.drawable.dropdown),
                    contentDescription = stringResource(R.string.select_subject)
                )
            }
            Spacer(Modifier.padding(start = 5.dp))
            Text(
                text = subject.name,
                style = Typography.labelMedium,
                color = PlannerTheme.colors.onSurface,
                modifier = Modifier.padding(end = 5.dp)
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = !expanded
                },
                containerColor = PlannerTheme.colors.container,
                shape = RoundedCornerShape(30.dp)
            ) {
                subjects.forEach {
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = it.name,
                                style = Typography.labelSmall,
                                color = PlannerTheme.colors.onContainer
                            )
                        },
                        onClick = {
                            onSubjectSelected(it.id)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}