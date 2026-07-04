package net.felipealafy.studentplanner.core.ui.components.grade

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.felipealafy.studentplanner.R
import net.felipealafy.studentplanner.core.ui.theme.PlannerTheme
import net.felipealafy.studentplanner.core.ui.theme.Typography
import net.felipealafy.studentplanner.feature_exams.domain.use_case.GradeStyle
import net.felipealafy.studentplanner.feature_exams.domain.use_case.getResourceLocation

@Composable
fun GradeStyleComboBox(onSelectItem: (GradeStyle) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf(GradeStyle.FROM_ZERO_TO_ONE_HUNDRED) }

    val textColor = PlannerTheme.colors.onSurface
    val backgroundColor = PlannerTheme.colors.container
    val textToDisplay = stringResource(selectedItem.getResourceLocation())
    var textStyle by remember { mutableStateOf(Typography.labelMedium) }

    Box(modifier = Modifier.padding(16.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(30.dp))
                .background(backgroundColor)
                .clickable { expanded = !expanded }
                .padding(horizontal = 20.dp, vertical = 14.dp)
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                tint = textColor,
                contentDescription = stringResource(R.string.grade_style_combobox)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = textToDisplay,
                style = textStyle,
                color = textColor,
                onTextLayout = { it ->
                    if(it.hasVisualOverflow) {
                        textStyle = textStyle.copy(fontSize = textStyle.fontSize * 0.9F)
                    }
                },
                maxLines = 1
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = backgroundColor,
            shape = RoundedCornerShape(20.dp)
        ) {
            val options = listOf(
                GradeStyle.FROM_ZERO_TO_ONE_HUNDRED,
                GradeStyle.FROM_ZERO_TO_TEN,
                GradeStyle.FROM_A_TO_F,
                GradeStyle.FROM_A_TO_F_WITH_E
            )

            options.forEach { style ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(style.getResourceLocation()),
                            color = textColor
                        )
                    },
                    onClick = {
                        selectedItem = style
                        onSelectItem(style)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun GradeStyleComboBoxPreview() {
    GradeStyleComboBox { }
}