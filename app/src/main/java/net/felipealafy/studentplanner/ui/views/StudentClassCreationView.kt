package net.felipealafy.studentplanner.ui.views

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import net.felipealafy.studentplanner.R
import net.felipealafy.studentplanner.datamodels.Planner
import net.felipealafy.studentplanner.datamodels.Subject
import net.felipealafy.studentplanner.models.StudentClassUiState
import net.felipealafy.studentplanner.models.StudentClassViewModel
import net.felipealafy.studentplanner.ui.date.time.picker.DateTimePickerDialog
import net.felipealafy.studentplanner.ui.extensions.getFormattedDateTime
import net.felipealafy.studentplanner.ui.theme.Black
import net.felipealafy.studentplanner.ui.theme.DarkGray
import net.felipealafy.studentplanner.ui.theme.LightGray
import net.felipealafy.studentplanner.ui.theme.Red
import net.felipealafy.studentplanner.ui.theme.Typography
import net.felipealafy.studentplanner.ui.theme.bluePallet
import net.felipealafy.studentplanner.ui.theme.colorPallet
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentClassCreationView(
    studentClassViewModel: StudentClassViewModel,
    onReturnAction: () -> Unit
) {
    val uiState = studentClassViewModel.uiState.collectAsState()
    val planner = uiState.value.planner

    if (planner == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White), // Cor de fundo neutra
            contentAlignment = Alignment.Center
        ) {
            if (uiState.value.isLoading) {
                CircularProgressIndicator()
            } else {
                Text(text = "No planners available.")
            }
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = onReturnAction
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = stringResource(R.string.back_to_past_view),
                            tint = Color(planner.color.getContrastingColorForText())
                        )
                    }
                },
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(R.string.student_class_creation_view_title),
                            style = Typography.bodyMedium,
                            color = Color(planner.color.getContrastingColorForText())
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(planner.color)
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = Color(planner.color).copy(alpha = 0.7F)
                )
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NewClassTitle(planner = planner)
            Spacer(modifier = Modifier.padding(top = 30.dp))
            EditableTextEntry(
                value = uiState.value.currentClassEntry.title,
                onTextChanged = {
                    studentClassViewModel.updateTitle(newTitle = it)
                },
                labelText = R.string.title,
                color = planner.color
            )
            Spacer(modifier = Modifier.padding(top = 30.dp))
            SelectSubjectCombobox(
                uiState = uiState,
                onSubjectSelected = { id ->
                    studentClassViewModel.updateAssociatedSubject(id)
                }
            )

            var startDateTime by rememberSaveable { mutableStateOf(false) }
            var endDateTime by rememberSaveable { mutableStateOf(false) }

            Row {
                IconButton(
                    onClick = {
                        startDateTime = true
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.calendar_start),
                        contentDescription = stringResource(R.string.date_start)
                    )
                }
                Text(
                    text = uiState.value.currentClassEntry.start.getFormattedDateTime()
                )
            }

            Row {
                IconButton(
                    onClick = {
                        endDateTime = true
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.calendar_start),
                        contentDescription = stringResource(R.string.date_end)
                    )
                }
                Text(
                    text = uiState.value.currentClassEntry.end.getFormattedDateTime()
                )
            }

            if (startDateTime) {
                DateTimePickerDialog(
                    onDismissRequest = {
                        startDateTime = false
                    },
                    onDateTimeSelected = { dateMillis, hour, minute ->
                        studentClassViewModel.updateStartDate(dateMillis, hour, minute)
                    },
                    backgroundColor = planner.color
                )
            }

            if (endDateTime) {
                DateTimePickerDialog(
                    onDismissRequest = {
                        endDateTime = false
                    },
                    onDateTimeSelected = { dateMillis, hour, minute ->
                        studentClassViewModel.updateEndDate(dateMillis, hour, minute)
                    },
                    backgroundColor = planner.color
                )
            }

            EditableTextEntry(
                value = uiState.value.currentClassEntry.noteTakingLink,
                onTextChanged = {
                    studentClassViewModel.updateNoteTakingLink(it)
                },
                labelText = R.string.notetaking_link,
                color = planner.color
            )
            EditableTextEntry(
                value = uiState.value.currentClassEntry.observation,
                onTextChanged = {
                    studentClassViewModel.updateObservation(it)
                },
                labelText = R.string.observation_field,
                singleLine = false,
                color = planner.color
            )

            ButtonWithBackgroundColor(
                onClick = {
                    studentClassViewModel.saveStudentClass()
                    onReturnAction()
                },
                selectedColor = planner.color,
                placeholderTextPath = R.string.create_button
            )
        }
    }
}

@Composable
fun NewClassTitle(planner: Planner) {
    Text(
        text = stringResource(R.string.lets_create_a_class),
        style = Typography.headlineLarge,
        color = Color(planner.color.getContrastingColorForText())
    )
}

@Composable
fun EditableTextEntry(
    value: String,
    color: Long,
    onTextChanged: (String) -> Unit,
    @StringRes labelText: Int,
    singleLine: Boolean = true
) {
    OutlinedTextField(
        onValueChange = onTextChanged,
        value = value,
        textStyle = Typography.bodyMedium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp),
        singleLine = singleLine,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = LightGray,
            disabledContainerColor = LightGray,
            errorContainerColor = Red,
            unfocusedTextColor = DarkGray,
            focusedTextColor = DarkGray,
            errorTextColor = Red,
            focusedLabelColor = DarkGray,
            focusedBorderColor = Color(color),
            focusedPlaceholderColor = LightGray,
        ),
        label = {
            Text(
                text = stringResource(labelText),
                style = Typography.labelSmall,
                color = DarkGray
            )
        },
        shape = RoundedCornerShape(25.dp),
    )
}

@Composable
fun SelectSubjectCombobox(
    uiState: State<StudentClassUiState>,
    onSubjectSelected: (id: String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val color = uiState.value.planner?.color ?: colorPallet[0][1]
    val subject = getCurrentSubject(uiState)

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
                color = Color(color.getContrastingColorForText()),
                modifier = Modifier.padding(end = 5.dp)
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = !expanded
                },
                containerColor = Color(color),
                shape = RoundedCornerShape(30.dp)
            ) {
                uiState.value.planner!!.subjects.forEach {
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = it.name,
                                style = Typography.labelSmall,
                                color = Color(color.getContrastingColorForText())
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

@Composable
private fun getCurrentSubject(
    uiState: State<StudentClassUiState>,
): Subject {
    return if (uiState.value.currentClassEntry.subjectId.isNotEmpty()) {
        val subjects = uiState.value.availableSubjects.filter {
            it.id == uiState.value.currentClassEntry.subjectId
        }
        subjects.first()
    } else {
        Subject(
            name = stringResource(R.string.select_a_subject),
            id = "",
            plannerId = "",
            color = bluePallet[0],
            start = LocalDateTime.now(),
            end = LocalDateTime.now(),
        )
    }
}
