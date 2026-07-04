package net.felipealafy.studentplanner.feature_class.presentation.views

import android.widget.Toast
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.collectLatest
import net.felipealafy.studentplanner.R
import net.felipealafy.studentplanner.feature_class.presentation.viewmodels.EditStudentClassEvents
import net.felipealafy.studentplanner.feature_class.presentation.viewmodels.EditStudentClassUiState
import net.felipealafy.studentplanner.feature_class.presentation.viewmodels.EditStudentClassViewModel
import net.felipealafy.studentplanner.feature_subject.presentation.views.DateTimeSelector
import net.felipealafy.studentplanner.ui.components.color.chooser.ButtonWithBackgroundColor
import net.felipealafy.studentplanner.ui.components.combobox.SelectSubjectComboBox
import net.felipealafy.studentplanner.ui.components.text.label.TopAppBarTitle
import net.felipealafy.studentplanner.ui.date.time.picker.DateTimePickerDialog
import net.felipealafy.studentplanner.ui.extensions.getFormattedDateTime
import net.felipealafy.studentplanner.ui.theme.PlannerTheme
import net.felipealafy.studentplanner.ui.theme.StudentPlannerTheme
import net.felipealafy.studentplanner.ui.theme.Typography
import net.felipealafy.studentplanner.ui.theme.colorPallet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditStudentClassView(
    viewModel: EditStudentClassViewModel,
    onReturnAction: () -> Unit
) {
    var showStartDateTimeSelectorDialog by rememberSaveable { mutableStateOf(false) }
    var showEndDateTimeSelectorDialog by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is EditStudentClassEvents.ShowError -> {
                    Toast.makeText(context, event.messageResId, Toast.LENGTH_SHORT).show()
                }

                is EditStudentClassEvents.ClassUpdatedSuccessfully -> {
                    Toast.makeText(context, R.string.update_successfully, Toast.LENGTH_SHORT).show()
                    onReturnAction()
                }
            }
        }
    }


    val uiState by viewModel.uiState.collectAsState()

    when (val state = uiState) {
        is EditStudentClassUiState.Error -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(colorPallet[0][1])),
                contentAlignment = Alignment.Center
            ) {
                Text(text = stringResource(state.messageResId))
            }
        }

        EditStudentClassUiState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(colorPallet[0][1])),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is EditStudentClassUiState.Success -> {
            StudentPlannerTheme {
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
                                        tint = PlannerTheme.colors.onPrimary
                                    )
                                }
                            },
                            title = {
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    TopAppBarTitle(
                                        text = stringResource(R.string.edit_student_class_view)
                                    )
                                }
                            },
                            actions = {
                                if (state.formState.isValid) {
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
                                containerColor = PlannerTheme.colors.primary
                            )
                        )
                    }
                ) { innerPadding ->

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                color = PlannerTheme.colors.surface
                            )
                            .padding(innerPadding),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(start = 8.dp, end = 8.dp)
                        ) {
                            item {
                                NewClassTitle()
                            }
                            item {
                                Spacer(modifier = Modifier.padding(top = 16.dp))
                            }
                            item {
                                EditableTextEntry(
                                    value = state.formState.title,
                                    onTextChanged = {
                                        viewModel.updateTitle(newTitle = it)
                                    },
                                    labelText = R.string.title
                                )
                            }
                            item {
                                Spacer(modifier = Modifier.padding(top = 16.dp))
                            }
                            item {
                                SelectSubjectComboBox(
                                    onSubjectSelected = { id ->
                                        viewModel.updateAssociatedSubject(id)
                                    },
                                    subjectId = state.formState.subjectId,
                                    subjects = state.detailedPlanner.subjects.map { it.subject }
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
                                        color = PlannerTheme.colors.onSurface
                                    )

                                    DateTimeSelector(
                                        onClick = {
                                            showStartDateTimeSelectorDialog = true
                                        },
                                        dateTime = state.formState.start.getFormattedDateTime(),
                                    )

                                    if (showStartDateTimeSelectorDialog) {
                                        DateTimePickerDialog(
                                            initialDateTime = state.formState.start,
                                            onDismissRequest = {
                                                showStartDateTimeSelectorDialog = false
                                            },
                                            onDateTimeSelected = { dateMillis, hour, minute ->
                                                viewModel.updateStartDate(dateMillis, hour, minute)
                                            },
                                        )
                                    }

                                    Spacer(modifier = Modifier.padding(top = 16.dp))
                                    Text(
                                        text = stringResource(R.string.exam_date_time_end),
                                        style = Typography.bodyMedium,
                                        modifier = Modifier.padding(start = 16.dp),
                                        color = PlannerTheme.colors.onSurface
                                    )
                                    DateTimeSelector(
                                        onClick = {
                                            showEndDateTimeSelectorDialog = true
                                        },
                                        dateTime = state.formState.end.getFormattedDateTime(),
                                    )

                                    if (showEndDateTimeSelectorDialog) {
                                        DateTimePickerDialog(
                                            initialDateTime = state.formState.end,
                                            onDismissRequest = {
                                                showEndDateTimeSelectorDialog = false
                                            },
                                            onDateTimeSelected = { dateMillis, hour, minute ->
                                                viewModel.updateEndDate(dateMillis, hour, minute)
                                            },
                                        )
                                    }
                                }
                            }

                            item {
                                EditableTextEntry(
                                    value = state.formState.noteTakingLink,
                                    onTextChanged = {
                                        viewModel.updateNoteTakingLink(it)
                                    },
                                    labelText = R.string.notetaking_link,
                                )
                            }
                            item {
                                EditableTextEntry(
                                    value = state.formState.observation,
                                    onTextChanged = {
                                        viewModel.updateObservation(it)
                                    },
                                    labelText = R.string.observation_field,
                                    singleLine = false,
                                )
                            }
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    ButtonWithBackgroundColor(
                                        onClick = {
                                            viewModel.saveStudentClass()
                                        },
                                        placeholderTextPath = R.string.update_button,
                                        isButtonEnabled = state.formState.isValid
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