package net.felipealafy.studentplanner.ui.views

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import net.felipealafy.studentplanner.R
import net.felipealafy.studentplanner.viewmodels.StudentClassCreationViewModel
import net.felipealafy.studentplanner.ui.date.time.picker.DateTimePickerDialog
import net.felipealafy.studentplanner.ui.extensions.getFormattedDateTime
import net.felipealafy.studentplanner.ui.theme.DarkGray
import net.felipealafy.studentplanner.ui.theme.LightGray
import net.felipealafy.studentplanner.ui.theme.Red
import net.felipealafy.studentplanner.ui.theme.Typography
import net.felipealafy.studentplanner.ui.theme.colorPallet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentClassCreationView(
    studentClassCreationViewModel: StudentClassCreationViewModel,
    onReturnAction: () -> Unit
) {
    val uiState = studentClassCreationViewModel.uiState.collectAsState()
    val planner = uiState.value.planner
    var showStartDateTimeSelectorDialog by rememberSaveable { mutableStateOf(false) }
    var showEndDateTimeSelectorDialog by rememberSaveable { mutableStateOf(false) }

    if (planner == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(colorPallet[0][1])),
            contentAlignment = Alignment.Center
        ) {
            if (uiState.value.isLoading) {
                CircularProgressIndicator()
            } else {
                Text(text = stringResource(R.string.no_planners_available))
            }
        }
        return
    }

    val color = planner.color.getForBackgroundBasedOnTitleBarColor()
    val textColor = planner.color.getForBackgroundBasedOnTitleBarColor().getContrastingColorForText()

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
                            tint = Color(textColor)
                        )
                    }
                },
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.student_class_creation_view_title),
                            style = Typography.bodyMedium,
                            color = Color(textColor)
                        )
                    }
                },
                actions = {
                    if (uiState.value.isEntryValid) {
                        Icon(
                            painter = painterResource(id = R.drawable.check_icon),
                            contentDescription = stringResource(R.string.class_is_able_to_save),
                        )
                    } else {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_close),
                            contentDescription = stringResource(R.string.class_is_not_able_to_save)
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
                    color = Color(color)
                )
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(start = 8.dp, end = 8.dp)
            ) {
                item {
                    NewClassTitle(color = color)
                }
                item {
                    Spacer(modifier = Modifier.padding(top = 16.dp))
                }
                item {
                    EditableTextEntry(
                        value = uiState.value.currentClassEntry.title,
                        onTextChanged = {
                            studentClassCreationViewModel.updateTitle(newTitle = it)
                        },
                        labelText = R.string.title,
                        color = color
                    )
                }
                item {
                    Spacer(modifier = Modifier.padding(top = 16.dp))
                }
                item {
                    SelectSubjectCombobox(
                        onSubjectSelected = { id ->
                            studentClassCreationViewModel.updateAssociatedSubject(id)
                        },
                        subjectId = uiState.value.currentClassEntry.subjectId,
                        subjects = planner.subjects.toList(),
                        selectedColor = color
                    )
                }


                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .border(
                                width = 2.dp,
                                color = Gray,
                                shape = RoundedCornerShape(32.dp)
                            ),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(R.string.exam_date_time_start),
                            style = Typography.bodyMedium,
                            modifier = Modifier.padding(start = 16.dp),
                            color = Color(textColor)
                        )

                        DateTimeSelector(
                            onClick = {
                                showStartDateTimeSelectorDialog = true
                            },
                            dateTime = uiState.value.currentClassEntry.start.getFormattedDateTime(),
                            selectedColor = color
                        )

                        if (showStartDateTimeSelectorDialog) {
                            DateTimePickerDialog(
                                initialDateTime = uiState.value.currentClassEntry.start,
                                onDismissRequest = {
                                    showStartDateTimeSelectorDialog = false
                                },
                                onDateTimeSelected = { dateMillis, hour, minute ->
                                    studentClassCreationViewModel.updateStartDate(dateMillis, hour, minute)
                                },
                                backgroundColor = color
                            )
                        }

                        Spacer(modifier = Modifier.padding(top = 16.dp))
                        Text(
                            text = stringResource(R.string.exam_date_time_end),
                            style = Typography.bodyMedium,
                            modifier = Modifier.padding(start = 16.dp),
                            color = Color(color.getContrastingColorForText())
                        )
                        DateTimeSelector(
                            onClick = {
                                showEndDateTimeSelectorDialog = true
                            },
                            dateTime = uiState.value.currentClassEntry.end.getFormattedDateTime(),
                            selectedColor = color
                        )

                        if (showEndDateTimeSelectorDialog) {
                            DateTimePickerDialog(
                                initialDateTime = uiState.value.currentClassEntry.end,
                                onDismissRequest = {
                                    showEndDateTimeSelectorDialog = false
                                },
                                onDateTimeSelected = { dateMillis, hour, minute ->
                                    studentClassCreationViewModel.updateEndDate(dateMillis, hour, minute)
                                },
                                backgroundColor = color
                            )
                        }
                    }

                    if (showStartDateTimeSelectorDialog) {
                        DateTimePickerDialog(
                            onDismissRequest = {
                                showStartDateTimeSelectorDialog = false
                            },
                            onDateTimeSelected = { dateMillis, hour, minute ->
                                studentClassCreationViewModel.updateStartDate(dateMillis, hour, minute)
                            },
                            backgroundColor = color
                        )
                    }

                    if (showEndDateTimeSelectorDialog) {
                        DateTimePickerDialog(
                            onDismissRequest = {
                                showEndDateTimeSelectorDialog = false
                            },
                            onDateTimeSelected = { dateMillis, hour, minute ->
                                studentClassCreationViewModel.updateEndDate(dateMillis, hour, minute)
                            },
                            backgroundColor = color
                        )
                    }
                }

                item {
                    EditableTextEntry(
                        value = uiState.value.currentClassEntry.noteTakingLink,
                        onTextChanged = {
                            studentClassCreationViewModel.updateNoteTakingLink(it)
                        },
                        labelText = R.string.notetaking_link,
                        color = color
                    )
                }
                item {
                    EditableTextEntry(
                        value = uiState.value.currentClassEntry.observation,
                        onTextChanged = {
                            studentClassCreationViewModel.updateObservation(it)
                        },
                        labelText = R.string.observation_field,
                        singleLine = false,
                        color = color
                    )
                }
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        ButtonWithBackgroundColor(
                            onClick = {
                                studentClassCreationViewModel.saveStudentClass()
                                onReturnAction()
                            },
                            selectedColor = color.getContrastingButtonColor(),
                            placeholderTextPath = R.string.create_button,
                            isButtonEnabled = uiState.value.isEntryValid
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NewClassTitle(color: Long) {
    Text(
        text = stringResource(R.string.lets_create_a_class),
        style = Typography.headlineLarge,
        color = Color(color.getContrastingColorForText())
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
                color = Color(color.getContrastingColorForText())
            )
        },
        shape = RoundedCornerShape(25.dp),
    )
}