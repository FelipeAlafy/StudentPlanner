package net.felipealafy.studentplanner.core.ui.components.grade

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.felipealafy.studentplanner.R
import net.felipealafy.studentplanner.core.ui.theme.DarkGray
import net.felipealafy.studentplanner.core.ui.theme.PlannerTheme
import net.felipealafy.studentplanner.core.ui.theme.Typography
import net.felipealafy.studentplanner.feature_exams.domain.use_case.GradeAToF

@Composable
fun GradeInputFromAToF(onSelectItem: (GradeAToF) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .border(
                width = 2.dp,
                color = DarkGray,
                shape = RoundedCornerShape(30.dp)
            ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            var selectedItem by remember {
                mutableStateOf(
                    GradeAToF.C
                )
            }
            val textToDisplay = selectedItem.name
            IconButton(
                { expanded = !expanded },
            ) {
                Spacer(Modifier.padding(start = 8.dp))
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    tint = PlannerTheme.colors.onSurface,
                    contentDescription = stringResource(R.string.grade_style_combobox)
                )
            }

            Spacer(Modifier.padding(start = 10.dp))

            Text(
                text = textToDisplay,
                style = Typography.labelMedium,
                color = PlannerTheme.colors.onSurface,
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                containerColor = PlannerTheme.colors.surface,
                shape = RoundedCornerShape(30.dp)
            ) {
                GradeAToF.entries.forEach {
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = it.name,
                                color = PlannerTheme.colors.onSurface
                            )
                        },
                        onClick = {
                            selectedItem = it
                            onSelectItem(it)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun GradeInputFromAToFPreview() {
    GradeInputFromAToF { }
}