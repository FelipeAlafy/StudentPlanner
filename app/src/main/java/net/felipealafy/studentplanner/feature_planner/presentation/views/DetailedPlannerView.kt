package net.felipealafy.studentplanner.feature_planner.presentation.views

import PlannerTitleForAnyCard
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import net.felipealafy.studentplanner.R
import net.felipealafy.studentplanner.core.ui.components.text.label.ClassesTaken
import net.felipealafy.studentplanner.feature_exams.domain.use_case.GradeStyle
import net.felipealafy.studentplanner.feature_planner.domain.model.Planner
import net.felipealafy.studentplanner.feature_planner.presentation.viewmodels.DetailedPlannerUiState
import net.felipealafy.studentplanner.feature_subject.domain.model.Subject
import net.felipealafy.studentplanner.feature_subject.presentation.views.Date
import net.felipealafy.studentplanner.core.ui.components.text.label.TopAppBarTitle
import net.felipealafy.studentplanner.core.ui.theme.PlannerTheme
import net.felipealafy.studentplanner.core.ui.theme.PlannerThemeProvider
import net.felipealafy.studentplanner.feature_planner.presentation.viewmodels.DetailedPlannerViewModel
import net.felipealafy.studentplanner.core.ui.theme.Typography
import net.felipealafy.studentplanner.feature_subject.domain.model.DetailedSubject
import net.felipealafy.studentplanner.core.ui.extensions.formattedValue
import net.felipealafy.studentplanner.core.ui.extensions.getPercentageFromZeroToOneHundred
import net.felipealafy.studentplanner.core.ui.extensions.getValueInDisplayStyleForAverage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailedPlannerView(
    viewModel: DetailedPlannerViewModel,
    onReturnToPreviousView: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    when (val state = uiState) {
        is DetailedPlannerUiState.Error -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "No planners available.")
            }
        }
        is DetailedPlannerUiState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is DetailedPlannerUiState.Success -> {
            val detailedPlanner = state.planner
            val planner = detailedPlanner.planner
            PlannerThemeProvider(
                baseColor = planner.color
            ) {
                Scaffold(
                    containerColor = PlannerTheme.colors.container,
                    topBar = {
                        TopAppBar(
                            title = {
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    TopAppBarTitle(
                                        text = planner.name,
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = PlannerTheme.colors.primary
                            ),
                            navigationIcon = {
                                IconButton(onClick = { onReturnToPreviousView() }) {
                                    Icon(
                                        painter = painterResource(R.drawable.back_arrow),
                                        contentDescription = stringResource(R.string.back_to_past_view),
                                        tint = PlannerTheme.colors.onPrimary
                                    )
                                }
                            },
                            actions = {
                                IconButton(onClick = {}) {
                                    Icon(
                                        painter = painterResource(R.drawable.edit_document),
                                        contentDescription = stringResource(R.string.go_on_edit_mode_for_planner),
                                        tint = PlannerTheme.colors.onPrimary
                                    )
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        TopPlannerCard(
                            planner,
                            plannerProgress = detailedPlanner.plannerProgress,
                            passedSubjectCount = detailedPlanner.passedSubjectsCount,
                            globalAverage = detailedPlanner.globalAverage
                        )
                        SubjectsColumn(
                            subjects = detailedPlanner.subjects,
                            minimumGradeToPass = planner.minimumGradeToPass
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TopPlannerCard(planner: Planner, plannerProgress: Float, passedSubjectCount: Int, globalAverage: Float) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = PlannerTheme.colors.cardTop,
                shape = RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = 0.dp,
                    bottomStart = 20.dp,
                    bottomEnd = 20.dp
                )
            )
            .padding(start = 12.dp, top = 12.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        PlannerTitleForAnyCard(
            title = stringResource(R.string.on_going_planner_progress)
        )
        ProgressIndicator(percentage = plannerProgress)
        SubjectsFinished(countOfFinishedSubjects = passedSubjectCount)
        Spacer(modifier = Modifier.padding(bottom = 4.dp))
        AverageGradeForAllSubjects(
            average = globalAverage,
            displayStyle = planner.gradeDisplayStyle,
            plannerProgress = plannerProgress
        )
        Spacer(modifier = Modifier.padding(bottom = 8.dp))
    }
}

@Composable
fun AverageGradeForAllSubjects(
    average: Float,
    displayStyle: GradeStyle = GradeStyle.FROM_ZERO_TO_ONE_HUNDRED,
    plannerProgress: Float
) {
    Text(
        text = "${
            if (plannerProgress.getPercentageFromZeroToOneHundred() == 100F) {
                stringResource(R.string.average_grade_for_all_subjects_done)
            } else {
                stringResource(R.string.average_grade_for_all_subjects)
            }
        } ${displayStyle.getValueInDisplayStyleForAverage(average)}",
        style = Typography.labelLarge,
        color = PlannerTheme.colors.onCardTop
    )
}

@Composable
private fun SubjectsFinished(countOfFinishedSubjects: Int) {
    Text(
        text = stringResource(R.string.subjects_done) +
                " $countOfFinishedSubjects ${stringResource(R.string.subjects)}.",
        style = Typography.labelLarge,
        color = PlannerTheme.colors.onCardTop
    )
}

@Composable
fun ProgressIndicator(percentage: Float) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProgressIndicatorWithPercentage(percentage)
    }
}

@Composable
private fun ProgressIndicatorWithPercentage(percentage: Float) {
    Row {
        ProgressBar(percentage = percentage)
        PercentageText(percentage = percentage)
    }
}

@Composable
fun ProgressBar(percentage: Float) {
    LinearProgressIndicator(
        progress = { percentage },
        color = PlannerTheme.colors.primary,
        trackColor = PlannerTheme.colors.container,
        modifier = Modifier
            .fillMaxWidth(.7F)
            .height(20.dp),
        strokeCap = StrokeCap.Round
    )
}

@Composable
fun PercentageText(percentage: Float) {
    Text(
        text = "${percentage.getPercentageFromZeroToOneHundred()}%",
        style = Typography.labelLarge,
        color = PlannerTheme.colors.onPrimary,
        modifier = Modifier.padding(start = 8.dp)
    )
}

@Composable
fun SubjectsColumn(subjects: List<DetailedSubject>, minimumGradeToPass: Float) {
    LazyColumn {
        items(subjects) { detailedSubject ->
            SubjectCard(
                subjectName = detailedSubject.subject.name,
                startDate = detailedSubject.subject.start.formattedValue(),
                endDate = detailedSubject.subject.end.formattedValue(),
                formattedAverage = detailedSubject.averageGrade.toString(),
                countClassTaken = detailedSubject.countClassesTaken,
                isSubjectApproved = detailedSubject.isApproved(minimumGradeToPass),
                subjectColor = detailedSubject.subject.color
            )
        }
    }
}

@Composable
fun SubjectCard(
    subjectName: String,
    startDate: String,
    endDate: String,
    formattedAverage: String,
    countClassTaken: Int,
    isSubjectApproved: Boolean,
    subjectColor: Long
) {
    PlannerThemeProvider(baseColor = subjectColor) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = PlannerTheme.colors.cardTop
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 5.dp
            )
        ) {
            SubjectCardComponents(
                subjectName = subjectName,
                startDate = startDate,
                endDate = endDate,
                formattedAverage = formattedAverage,
                countClassTaken = countClassTaken,
                isSubjectApproved = isSubjectApproved
            )
        }
    }
}

@Composable
private fun SubjectCardComponents(
    subjectName: String,
    startDate: String,
    endDate: String,
    formattedAverage: String,
    countClassTaken: Int,
    isSubjectApproved: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 8.dp)
    ) {
            SubjectTitle(subjectName)
            Spacer(modifier = Modifier.padding(top = 16.dp))
            Date(startDate, endDate)
            Spacer(modifier = Modifier.padding(top = 4.dp))
            AverageGrade(
                formattedAverage,
                isSubjectApproved
            )
            Spacer(modifier = Modifier.padding(top = 8.dp))
            ClassesTaken(countClassTaken)
            Spacer(modifier = Modifier.padding(bottom = 8.dp))
        }
}

@Composable
fun AverageGrade(
    formattedAverage: String,
    isSubjectApproved: Boolean
) {
    val borderColor = if (isSubjectApproved) {
        PlannerTheme.colors.success
    } else {
        PlannerTheme.colors.error
    }

    val isApproved = if (isSubjectApproved) stringResource(R.string.approved) else stringResource(R.string.failed)

    Row {
        Icon(
            painter = painterResource(R.drawable.grade),
            contentDescription = stringResource(R.string.grade)
        )
        Text(
            text = "${stringResource(R.string.average_grade_for_subject)} $formattedAverage",
            style = Typography.labelLarge,
            color = PlannerTheme.colors.onCardTop
        )
        Spacer(modifier = Modifier.padding(start = 8.dp))
        Box(
            modifier = Modifier.border(
                width = 2.dp,
                color = borderColor,
                shape = RoundedCornerShape(20.dp)
            ).padding(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = isApproved,
                style = Typography.labelSmall
            )
        }
    }
}

@Composable
fun Date(subject: Subject) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.calendar),
                contentDescription = stringResource(R.string.date_icon),
                tint = PlannerTheme.colors.onCardTop
            )
            Text(
                text = "${stringResource(R.string.subject_period)}:",
                style = Typography.labelMedium,
                modifier = Modifier.padding(start = 8.dp),
                color = PlannerTheme.colors.onCardTop
            )
        }
        Row {
            Spacer(modifier = Modifier.padding(start = 16.dp))
            Text(
                text = "${stringResource(R.string.from)} ${subject.start.formattedValue()} → ${
                    stringResource(
                        R.string.to
                    )
                } ${subject.end.formattedValue()}",
                style = Typography.labelMedium,
                color = PlannerTheme.colors.onCardTop
            )
        }
    }
}

@Composable
fun SubjectTitle(title: String) {
    Text(
        modifier = Modifier.padding(top = 8.dp),
        text = title,
        style = Typography.bodyMedium,
        color = PlannerTheme.colors.onCardTop,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
    )
}