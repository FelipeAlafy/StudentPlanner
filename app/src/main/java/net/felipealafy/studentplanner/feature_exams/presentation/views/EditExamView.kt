package net.felipealafy.studentplanner.feature_exams.presentation.views

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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.remember
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
import net.felipealafy.studentplanner.feature_class.presentation.views.EditableTextEntry
import net.felipealafy.studentplanner.feature_exams.domain.use_case.GradeStyle
import net.felipealafy.studentplanner.feature_exams.presentation.viewmodels.EditExamEvents
import net.felipealafy.studentplanner.feature_exams.presentation.viewmodels.EditExamUiState
import net.felipealafy.studentplanner.feature_exams.presentation.viewmodels.EditExamViewModel
import net.felipealafy.studentplanner.feature_subject.presentation.views.DateTimeSelector
import net.felipealafy.studentplanner.core.ui.components.color.chooser.ButtonWithBackgroundColor
import net.felipealafy.studentplanner.core.ui.components.combobox.SelectSubjectComboBox
import net.felipealafy.studentplanner.core.ui.components.grade.GradeInputFromAToF
import net.felipealafy.studentplanner.core.ui.components.grade.GradeInputFromAToFWithE
import net.felipealafy.studentplanner.core.ui.components.grade.GradeInputFromZeroToOneHundred
import net.felipealafy.studentplanner.core.ui.components.grade.GradeInputFromZeroToTen
import net.felipealafy.studentplanner.core.ui.components.grade.GradeWeightInput
import net.felipealafy.studentplanner.core.ui.components.text.label.TopAppBarTitle
import net.felipealafy.studentplanner.core.ui.date.time.picker.DateTimePickerDialog
import net.felipealafy.studentplanner.core.ui.extensions.getFormattedDateTime
import net.felipealafy.studentplanner.core.ui.theme.PlannerTheme
import net.felipealafy.studentplanner.core.ui.theme.PlannerThemeProvider
import net.felipealafy.studentplanner.core.ui.theme.Typography
import net.felipealafy.studentplanner.core.ui.theme.colorPallet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditExamView(
    viewModel: EditExamViewModel,
    onReturnAction: () -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is EditExamEvents.ShowError -> {
                    Toast.makeText(context, event.messageResId, Toast.LENGTH_SHORT).show()
                }

                is EditExamEvents.ExamUpdatedSuccessfully -> {
                    Toast.makeText(context, R.string.update_successfully, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    val uiState by viewModel.uiState.collectAsState()

    when (val state = uiState) {
        is EditExamUiState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(colorPallet[0][1])),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is EditExamUiState.Error -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = stringResource(state.messageResId))

                Spacer(modifier = Modifier.height(16.dp))

                IconButton(onClick = onReturnAction) {
                    Icon(
                        painter = painterResource(R.drawable.back_arrow),
                        contentDescription = stringResource(R.string.back_to_past_view)
                    )
                }
            }
        }

        is EditExamUiState.Success -> {
            val planner = remember { state.detailedPlanner.planner }
            PlannerThemeProvider(planner.color) {

                Scaffold(
                    topBar = {
                        TopAppBar(
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = PlannerTheme.colors.primary
                            ),
                            title = {
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    TopAppBarTitle(
                                        text = stringResource(R.string.edit_exam_view),
                                    )
                                }
                            },
                            navigationIcon = {
                                IconButton(
                                    onClick = onReturnAction
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.back_arrow),
                                        contentDescription = stringResource(R.string.back_to_past_view),
                                        tint = PlannerTheme.colors.onPrimary
                                    )
                                }
                            },
                        )
                    }
                ) { innerPadding ->

                    Column(
                        modifier = Modifier
                            .background(
                                PlannerTheme.colors.cardTop
                            )
                            .padding(innerPadding)
                            .fillMaxSize()
                    ) {

                        EditableTextEntry(
                            value = state.examForm.name,
                            onTextChanged = {
                                viewModel.updateName(it)
                            },
                            labelText = R.string.exam_name,
                        )

                        SelectSubjectComboBox(
                            subjectId = state.examForm.subjectId,
                            subjects = state.detailedPlanner.pureSubjects,
                            onSubjectSelected = { id ->
                                viewModel.updateSubject(id)
                            },
                        )


                        when (planner.gradeDisplayStyle) {
                            GradeStyle.FROM_ZERO_TO_ONE_HUNDRED -> {
                                Column {
                                    Text(
                                        text = stringResource(R.string.select_your_exam_grade),
                                        style = Typography.bodyMedium,
                                        modifier = Modifier.padding(start = 16.dp),
                                        color = PlannerTheme.colors.onCardTop
                                    )
                                    GradeInputFromZeroToOneHundred(
                                        text = state.examForm.grade,
                                        onValueChange = { newGrade ->
                                            viewModel.updateGradeFrom0to100(newGrade)
                                        }
                                    )
                                }

                            }

                            GradeStyle.FROM_ZERO_TO_TEN -> {

                                Column {
                                    Text(
                                        text = stringResource(R.string.select_your_exam_grade),
                                        style = Typography.bodyMedium,
                                        color = PlannerTheme.colors.onCardTop
                                    )
                                    GradeInputFromZeroToTen(
                                        text = state.examForm.grade,
                                        onValueChange = { newGrade ->
                                            viewModel.updateGradeFrom0to10(newGrade)
                                        }
                                    )
                                }

                            }

                            GradeStyle.FROM_A_TO_F -> {
                                Text(
                                    text = stringResource(R.string.select_your_exam_grade),
                                    style = Typography.bodyMedium,
                                    modifier = Modifier.padding(start = 16.dp, top = 8.dp),
                                    color = PlannerTheme.colors.onCardTop
                                )
                                GradeInputFromAToF(
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
                                    color = PlannerTheme.colors.onCardTop
                                )
                                GradeInputFromAToFWithE(
                                    onSelectItem = { grade ->
                                        viewModel.updateGradeFromAToFWithE(grade)
                                    }
                                )
                            }
                        }

                        GradeWeightInput(
                            text = state.examForm.gradeWeight,
                            onValueChange = {
                                viewModel.updateGradeWeight(it)
                            }
                        )

                        Spacer(modifier = Modifier.padding(top = 16.dp))

                        var showStartDateTimeSelectorDialog by rememberSaveable {
                            mutableStateOf(
                                false
                            )
                        }
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
                                color = PlannerTheme.colors.onCardTop
                            )

                            DateTimeSelector(
                                onClick = {
                                    showStartDateTimeSelectorDialog = true
                                },
                                dateTime = state.examForm.start.getFormattedDateTime()
                            )

                            if (showStartDateTimeSelectorDialog) {
                                DateTimePickerDialog(
                                    initialDateTime = state.examForm.start,
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
                                color = PlannerTheme.colors.onCardTop
                            )
                            DateTimeSelector(
                                onClick = {
                                    showEndDateTimeSelectorDialog = true
                                },
                                dateTime = state.examForm.end.getFormattedDateTime()
                            )

                            if (showEndDateTimeSelectorDialog) {
                                DateTimePickerDialog(
                                    initialDateTime = state.examForm.end,
                                    onDismissRequest = {
                                        showEndDateTimeSelectorDialog = false
                                    },
                                    onDateTimeSelected = { dateMillis, hour, minute ->
                                        viewModel.updateEndDate(dateMillis, hour, minute)
                                    }
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
                            )
                        }

                        if (showEndDateTimeSelectorDialog) {
                            DateTimePickerDialog(
                                onDismissRequest = {
                                    showEndDateTimeSelectorDialog = false
                                },
                                onDateTimeSelected = { dateMillis, hour, minute ->
                                    viewModel.updateEndDate(dateMillis, hour, minute)
                                }
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
                                placeholderTextPath = R.string.update_button
                            )
                        }
                    }
                }
            }
        }
    }
}