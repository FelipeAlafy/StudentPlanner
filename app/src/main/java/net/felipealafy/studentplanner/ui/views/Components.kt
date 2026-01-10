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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import net.felipealafy.studentplanner.R
import net.felipealafy.studentplanner.datamodels.GradeStyle
import net.felipealafy.studentplanner.datamodels.Planner
import net.felipealafy.studentplanner.datamodels.Subject
import net.felipealafy.studentplanner.datamodels.getResourceLocation
import net.felipealafy.studentplanner.ui.theme.Black
import net.felipealafy.studentplanner.ui.theme.DarkGray
import net.felipealafy.studentplanner.ui.theme.LightGray
import net.felipealafy.studentplanner.ui.theme.Red
import net.felipealafy.studentplanner.ui.theme.Transparent
import net.felipealafy.studentplanner.ui.theme.Typography
import net.felipealafy.studentplanner.ui.theme.colorPallet


@Composable
fun ButtonOpenColorSelectionDialog(
    modifier: Modifier = Modifier,
    selectedColor: Long,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .size(35.dp)
            .border(width = 2.dp, color = DarkGray, shape = RoundedCornerShape(40.dp)),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(selectedColor)
        )
    ) {}
}

@Composable
fun ButtonWithBackgroundColor(
    onClick: () -> Unit,
    selectedColor: Long,
    @StringRes placeholderTextPath: Int
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(selectedColor).copy(alpha = 0.8F)
        )
    ) {
        Text(
            stringResource(placeholderTextPath),
            style = Typography.labelSmall,
            color = Color(selectedColor.getContrastingColorForText())
        )
    }
}

@Composable
fun ColorSelectionDialog(
    selectedColor: Long,
    onColorSelected: (Long) -> Unit,
    onDismissRequest: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Transparent
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(selectedColor).copy(alpha = 0.7F)
                ),
                elevation = CardDefaults.cardElevation(20.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.color_chooser_dialog),
                        modifier = Modifier.fillMaxWidth(),
                        style = Typography.headlineLarge,
                        textAlign = TextAlign.Center,
                        color = Color(selectedColor.getContrastingColorForText())
                    )
                    Row(
                        modifier = Modifier.padding(5.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        colorPallet.forEach { column ->
                            Column(
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                column.forEach { color ->
                                    IconButton(
                                        onClick = {
                                            onColorSelected(color)
                                            onDismissRequest()
                                        },
                                        colors = IconButtonDefaults.iconButtonColors(
                                            containerColor = Color(color)
                                        ),
                                        modifier = Modifier
                                            .size(35.dp)
                                            .border(
                                                width = 0.dp,
                                                color = Transparent,
                                                shape = RoundedCornerShape(40.dp)
                                            )
                                    ) {
                                        if (color == selectedColor) {
                                            Icon(
                                                Icons.Default.Check,
                                                contentDescription = stringResource(R.string.check_color)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GradeStyleCombobox(selectedColor: Long, onSelectItem: (GradeStyle) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .padding(16.dp)
            .border(
                width = 30.dp,
                color = Color(selectedColor).copy(alpha = 0.8F),
                shape = RoundedCornerShape(30.dp)
            ),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            var selectedItem by remember {
                mutableStateOf(
                    GradeStyle.FROM_ZERO_TO_ONE_HUNDRED
                )
            }
            val textToDisplay = stringResource(selectedItem.getResourceLocation())
            IconButton(
                { expanded = !expanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = stringResource(R.string.grade_style_combobox)
                    )
                    Spacer(Modifier.padding(start = 5.dp))
                    Text(
                        text = textToDisplay,
                        style = Typography.labelMedium,
                        color = Color(selectedColor.getContrastingColorForText()),
                        modifier = Modifier.padding(end = 5.dp)
                    )
                }
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                containerColor = Color(selectedColor).copy(alpha = 0.8F),
                shape = RoundedCornerShape(30.dp)
            ) {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(GradeStyle.FROM_ZERO_TO_ONE_HUNDRED.getResourceLocation()),
                            color = Color(selectedColor.getContrastingColorForText())
                        ) },
                    onClick = {
                        selectedItem = GradeStyle.FROM_ZERO_TO_ONE_HUNDRED
                        onSelectItem(GradeStyle.FROM_ZERO_TO_ONE_HUNDRED)
                        expanded = false
                    }
                )
                DropdownMenuItem(
                    text = {
                        Text(
                            stringResource(GradeStyle.FROM_ZERO_TO_TEN.getResourceLocation()),
                            color = Color(selectedColor.getContrastingColorForText())
                        )
                    },
                    onClick = {
                        selectedItem = GradeStyle.FROM_ZERO_TO_TEN
                        onSelectItem(GradeStyle.FROM_ZERO_TO_TEN)
                        expanded = false
                    }
                )
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(GradeStyle.FROM_A_TO_F.getResourceLocation()),
                            color = Color(selectedColor.getContrastingColorForText())
                        )
                    },
                    onClick = {
                        selectedItem = GradeStyle.FROM_A_TO_F
                        onSelectItem(GradeStyle.FROM_A_TO_F)
                        expanded = false
                    }
                )
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(GradeStyle.FROM_A_TO_F_WITH_E.getResourceLocation()),
                            color = Color(selectedColor.getContrastingColorForText())
                        )
                    },
                    onClick = {
                        selectedItem = GradeStyle.FROM_A_TO_F_WITH_E
                        onSelectItem(GradeStyle.FROM_A_TO_F_WITH_E)
                        expanded = false
                    }
                )
            }
        }
    }
}


@Composable
fun MinimumGradeToPassInput(text: String, onValueChange: (String) -> Unit, selectedColor: Long) {
    OutlinedTextField(
        onValueChange = onValueChange,
        value = text,
        textStyle = Typography.bodyMedium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = LightGray,
            disabledContainerColor = LightGray,
            errorContainerColor = Red,
            unfocusedTextColor = DarkGray,
            focusedTextColor = DarkGray,
            errorTextColor = Red,
            focusedLabelColor = DarkGray,
            focusedBorderColor = Color(selectedColor),
            focusedPlaceholderColor = LightGray,
        ),
        label = {
            Text(
                text = stringResource(R.string.minimum_grade_to_pass),
                style = Typography.labelSmall,
                color = DarkGray
            )
        },
        shape = RoundedCornerShape(25.dp),
    )
}

@Composable
fun TextInputWithColor(
    text: String,
    onValueChange: (String) -> Unit,
    selectedColor: Long,
    hint: Int
) {
    OutlinedTextField(
        onValueChange = onValueChange,
        value = text,
        textStyle = Typography.bodyMedium,
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = LightGray,
            disabledContainerColor = LightGray,
            errorContainerColor = Red,
            unfocusedTextColor = DarkGray,
            focusedTextColor = DarkGray,
            errorTextColor = Red,
            focusedLabelColor = DarkGray,
            focusedBorderColor = Color(selectedColor),
            focusedPlaceholderColor = LightGray,
        ),
        label = {
            Text(
                text = stringResource(id = hint),
                color = Color(selectedColor.getContrastingColorForText()),
                style = Typography.labelSmall
            )
        },
        shape = RoundedCornerShape(25.dp),
    )
}

@Composable
fun PlannerTitleForAnyCard(title: String, planner: Planner) {
    Text(
        text = title,
        style = Typography.bodyLarge,
        color = Color(planner.color.getContrastingColorForText())
    )
}

@Composable
fun SubjectTitleForAnyCard(title: String, subject: Subject) {
    Text(
        text = title,
        style = Typography.bodyLarge,
        color = Color(subject.color.getContrastingColorForText())
    )
}

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

@Composable
fun AverageGrade(
    displayStyle: GradeStyle = GradeStyle.FROM_ZERO_TO_ONE_HUNDRED,
    subject: Subject,
    @StringRes text: Int = R.string.average_grade_for_all_subjects
) {
    Row {
        Icon(
            painter = painterResource(R.drawable.grade),
            contentDescription = stringResource(R.string.grade)
        )
        Text(
            text = stringResource(text) +
                    " ${displayStyle.getValueInDisplayStyleForAverage(average = subject.exams.getExamsAverage())}",
            style = Typography.labelLarge,
            color = Color(subject.color.getContrastingColorForText())
        )
    }
}