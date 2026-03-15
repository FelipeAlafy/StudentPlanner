package net.felipealafy.studentplanner.ui.views

import androidx.annotation.StringRes
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import net.felipealafy.studentplanner.R
import net.felipealafy.studentplanner.datamodels.GradeAToF
import net.felipealafy.studentplanner.datamodels.GradeAToFWithE
import net.felipealafy.studentplanner.datamodels.GradeStyle
import net.felipealafy.studentplanner.datamodels.Planner
import net.felipealafy.studentplanner.datamodels.Subject
import net.felipealafy.studentplanner.datamodels.getResourceLocation
import net.felipealafy.studentplanner.ui.theme.DarkGray
import net.felipealafy.studentplanner.ui.theme.LightGray
import net.felipealafy.studentplanner.ui.theme.Red
import net.felipealafy.studentplanner.ui.theme.Transparent
import net.felipealafy.studentplanner.ui.theme.Typography
import net.felipealafy.studentplanner.ui.theme.colorPallet
import java.time.LocalDateTime

@Composable
fun SelectSubjectCombobox(
    subjectId: String,
    subjects: List<Subject>,
    selectedColor: Long,
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
                color = selectedColor,
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
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = stringResource(R.string.select_subject)
                )
            }
            Spacer(Modifier.padding(start = 5.dp))
            Text(
                text = subject.name,
                style = Typography.labelMedium,
                color = Color(selectedColor.getContrastingColorForText()),
                modifier = Modifier.padding(end = 5.dp)
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = !expanded
                },
                containerColor = Color(selectedColor),
                shape = RoundedCornerShape(30.dp)
            ) {
                subjects.forEach {
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = it.name,
                                style = Typography.labelSmall,
                                color = Color(selectedColor.getContrastingColorForText())
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