package net.felipealafy.studentplanner.ui.views

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.collectLatest
import net.felipealafy.studentplanner.R
import net.felipealafy.studentplanner.datamodels.GradeStyle
import net.felipealafy.studentplanner.datamodels.Subject
import net.felipealafy.studentplanner.models.EditExamViewModel
import net.felipealafy.studentplanner.ui.date.time.picker.DateTimePickerDialog
import net.felipealafy.studentplanner.ui.extensions.getFormattedDateTime
import net.felipealafy.studentplanner.ui.theme.Typography
import net.felipealafy.studentplanner.ui.theme.colorPallet
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditExamView(
    viewModel: EditExamViewModel,
    onReturnAction: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val planner = uiState.planner
    if (planner == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(colorPallet[0][1])),
            contentAlignment = Alignment.Center
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else {
                Column (
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                ) {
                    Text(
                        text = "No planners available.",
                        textAlign = TextAlign.Center,
                        color = Color(colorPallet[0][1].getContrastingColorForText())
                    )
                    IconButton(
                        onClick = onReturnAction,
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            tint = Color(colorPallet[0][1].getContrastingColorForText()),
                            contentDescription = stringResource(R.string.back_to_past_view)
                        )
                    }
                }
            }
        }
        return
    }

    if (planner.subjects.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(planner.color)),
            contentAlignment = Alignment.Center
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else {
                Column (
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.no_subjects_available_error_message),
                        textAlign = TextAlign.Center,
                        color = Color(planner.color.getContrastingColorForText())
                    )

                    IconButton(
                        modifier = Modifier.width(64.dp).align(Alignment.CenterHorizontally),
                        onClick = onReturnAction
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            tint = Color(planner.color.getContrastingColorForText()),
                            contentDescription = stringResource(R.string.back_to_past_view)
                        )
                    }
                }
            }
        }
        return
    }

    val subject = viewModel.uiState.value.planner?.subjects?.firstOrNull() ?: Subject(
        plannerId = planner.id,
        name = "Error while loading subject",
        color = colorPallet[0][1],
        start = LocalDateTime.now(),
        end = LocalDateTime.now()
    )

    val context = LocalContext.current
    LaunchedEffect(key1 = Unit) {
        viewModel.toastEvent.collectLatest { messageId ->
            Toast.makeText(
                context,
                context.getString(messageId),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(subject.color.toInt())
                ),
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.edit_exam_view),
                            color = Color(subject.color.getContrastingColorForText()),
                            style = Typography.headlineMedium
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onReturnAction
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = stringResource(R.string.back_to_past_view),
                            tint = Color(subject.color.getContrastingColorForText())
                        )
                    }
                },
            )
        }
    ) { innerPadding ->
        val backgroundColor = Color(subject.color.getForBackgroundBasedOnTitleBarColor())
        val textContrastedColor = Color(subject.color.getForBackgroundBasedOnTitleBarColor()
            .getContrastingColorForText()
        )
        Column(
            modifier = Modifier
                .background(
                    backgroundColor
                )
                .padding(innerPadding)
                .fillMaxSize()
        ) {

            EditableTextEntry(
                value = uiState.currentExamEntry.name,
                onTextChanged = {
                    viewModel.updateName(it)
                },
                labelText = R.string.exam_name,
                color = subject.color
            )

            SelectSubjectCombobox(
                subjectId = uiState.currentExamEntry.subjectId,
                subjects = planner.subjects.toList(),
                onSubjectSelected = { id ->
                    viewModel.updateSubject(id)
                },
                selectedColor = subject.color
            )


            when (planner.gradeDisplayStyle) {
                GradeStyle.FROM_ZERO_TO_ONE_HUNDRED -> {
                    Column {
                        Text(
                            text = stringResource(R.string.select_your_exam_grade),
                            style = Typography.bodyMedium,
                            modifier = Modifier.padding(start = 16.dp),
                            color = textContrastedColor
                        )
                        GradeInputFromZeroToOneHundred(
                            text = uiState.currentExamEntry.grade,
                            onValueChange = { newGrade ->
                                viewModel.updateGradeFrom0to100(newGrade)
                            },
                            selectedColor = subject.color,
                            onValidate = {viewModel.validateGrade()},
                            invalidDigit = viewModel.isInvalidInputErrorForGrade.collectAsState().value
                        )
                    }

                }

                GradeStyle.FROM_ZERO_TO_TEN -> {

                    Column {
                        Text(
                            text = stringResource(R.string.select_your_exam_grade),
                            style = Typography.bodyMedium,
                            color = textContrastedColor
                        )
                        GradeInputFromZeroToTen(
                            text = uiState.currentExamEntry.grade,
                            onValueChange = { newGrade ->
                                viewModel.updateGradeFrom0to10(newGrade)
                            },
                            selectedColor = subject.color,
                            onValidate = {viewModel.validateGrade()},
                            invalidDigit = viewModel.isInvalidInputErrorForGrade.collectAsState().value
                        )
                    }

                }

                GradeStyle.FROM_A_TO_F -> {
                    Text(
                        text = stringResource(R.string.select_your_exam_grade),
                        style = Typography.bodyMedium,
                        modifier = Modifier.padding(start = 16.dp, top = 8.dp),
                        color = textContrastedColor
                    )
                    GradeInputFromAToF(
                        selectedColor = subject.color,
                        onSelectItem = { grade ->
                            viewModel.updateGradeFromAToF(grade)
                        }
                    )

                }

                GradeStyle.FROM_A_TO_F_WITH_E -> {
                    Text(
                        text = stringResource(R.string.select_your_exam_grade),
                        style = Typography.bodyMedium,
                        modifier = Modifier.padding(start = 16.dp, top = 8.dp),
                        color = textContrastedColor
                    )
                    GradeInputFromAToFWithE(
                        selectedColor = subject.color,
                        onSelectItem = { grade ->
                            viewModel.updateGradeFromAToFWithE(grade)
                        }
                    )
                }
            }

            GradeWeightInput(
                text = uiState.currentExamEntry.gradeWeight,
                onValueChange = {
                    viewModel.updateGradeWeight(it)
                },
                onValidate = {
                    viewModel.validateGradeWeight()
                },
                invalidDigit = viewModel.isInvalidInputErrorForGradeWeight.collectAsState().value,
                selectedColor = subject.color
            )

            Spacer(modifier = Modifier.padding(top = 16.dp))

            var showStartDateTimeSelectorDialog by rememberSaveable { mutableStateOf(false) }
            var showEndDateTimeSelectorDialog by rememberSaveable { mutableStateOf(false) }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp)
                    .height(256.dp)
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
                    color = textContrastedColor
                )

                DateTimeSelector(
                    onClick = {
                        showStartDateTimeSelectorDialog = true
                    },
                    dateTime = uiState.currentExamEntry.start.getFormattedDateTime(),
                    selectedColor = subject.color
                )

                if (showStartDateTimeSelectorDialog) {
                    DateTimePickerDialog(
                        initialDateTime = uiState.currentExamEntry.start,
                        onDismissRequest = {
                            showStartDateTimeSelectorDialog = false
                        },
                        onDateTimeSelected = { dateMillis, hour, minute ->
                            viewModel.updateStartDate(dateMillis, hour, minute)
                        },
                        backgroundColor = subject.color
                    )
                }

                Spacer(modifier = Modifier.padding(top = 16.dp))
                Text(
                    text = stringResource(R.string.exam_date_time_end),
                    style = Typography.bodyMedium,
                    modifier = Modifier.padding(start = 16.dp),
                    color = textContrastedColor
                )
                DateTimeSelector(
                    onClick = {
                        showEndDateTimeSelectorDialog = true
                    },
                    dateTime = uiState.currentExamEntry.end.getFormattedDateTime(),
                    selectedColor = subject.color
                )

                if (showEndDateTimeSelectorDialog) {
                    DateTimePickerDialog(
                        initialDateTime = uiState.currentExamEntry.end,
                        onDismissRequest = {
                            showEndDateTimeSelectorDialog = false
                        },
                        onDateTimeSelected = { dateMillis, hour, minute ->
                            viewModel.updateEndDate(dateMillis, hour, minute)
                        },
                        backgroundColor = subject.color
                    )
                }
            }


            if (showStartDateTimeSelectorDialog) {
                DateTimePickerDialog(
                    onDismissRequest = {
                        showStartDateTimeSelectorDialog = false
                    },
                    onDateTimeSelected = { dateMillis, hour, minute ->
                        viewModel.updateStartDate(dateMillis, hour, minute)
                    },
                    backgroundColor = subject.color
                )
            }

            if (showEndDateTimeSelectorDialog) {
                DateTimePickerDialog(
                    onDismissRequest = {
                        showEndDateTimeSelectorDialog = false
                    },
                    onDateTimeSelected = { dateMillis, hour, minute ->
                        viewModel.updateEndDate(dateMillis, hour, minute)
                    },
                    backgroundColor = subject.color
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth()
            ) {
                ButtonWithBackgroundColor(
                    onClick = {
                        viewModel.updateExam()
                        onReturnAction()
                    },
                    selectedColor = subject.color,
                    placeholderTextPath = R.string.update_button
                )
            }
        }
    }
}