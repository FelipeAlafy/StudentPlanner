package net.felipealafy.studentplanner.feature_subject.presentation.views

import net.felipealafy.studentplanner.core.ui.components.text.label.SubjectTitleForAnyCard
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import net.felipealafy.studentplanner.R
import net.felipealafy.studentplanner.feature_exams.data.local.Exam
import net.felipealafy.studentplanner.feature_exams.domain.use_case.GradeStyle
import net.felipealafy.studentplanner.feature_class.domain.model.StudentClass
import net.felipealafy.studentplanner.feature_subject.presentation.viewmodels.DetailedSubjectViewModel
import net.felipealafy.studentplanner.feature_subject.presentation.viewmodels.SubjectDetailsUiState
import net.felipealafy.studentplanner.core.ui.components.text.label.AverageGrade
import net.felipealafy.studentplanner.core.ui.components.text.label.ClassesTaken
import net.felipealafy.studentplanner.core.ui.components.text.label.TopAppBarTitle
import net.felipealafy.studentplanner.core.ui.theme.PlannerTheme
import net.felipealafy.studentplanner.core.ui.theme.PlannerThemeProvider
import net.felipealafy.studentplanner.core.ui.theme.Typography
import net.felipealafy.studentplanner.feature_planner.presentation.views.SubjectTitle
import net.felipealafy.studentplanner.core.ui.extensions.formattedValue
import net.felipealafy.studentplanner.core.ui.extensions.getValueInDisplayStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailedSubjectView(detailedSubjectViewModel: DetailedSubjectViewModel) {
    val uiState by detailedSubjectViewModel.uiState.collectAsState()

    when (val state = uiState) {
        is SubjectDetailsUiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Erro: ${state.exception}", color = Color.Red)
            }
        }
        is SubjectDetailsUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = PlannerTheme.colors.primary)
            }
        }
        is SubjectDetailsUiState.Success -> {
            val detailedSubject = state.data
            val subject = detailedSubject.subject
            val classes = detailedSubject.studentClasses
            val exams = detailedSubject.exams

            PlannerThemeProvider(
                baseColor = subject.color
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
                                        text = subject.name
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = PlannerTheme.colors.primary
                            ),
                            navigationIcon = {
                                IconButton(
                                    onClick = {}
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.back_arrow),
                                        contentDescription = stringResource(R.string.back_to_past_view),
                                        tint = PlannerTheme.colors.onPrimary
                                    )
                                }
                            },
                            actions = {
                                IconButton(
                                    onClick = {}
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.edit_document),
                                        contentDescription = stringResource(R.string.go_on_edit_mode_for_subject),
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
                        SubjectBox(detailedSubject.countClassesTaken, detailedSubject.averageGrade.toString())

                        LazyColumn {
                            items(classes) {
                                ClassCard(studentClass = it)
                            }


                            items(exams) {
                                ExamCard(exam = it)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SubjectBox(countClassesTaken: Int, averageGradeFormatted: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = PlannerTheme.colors.surface,
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
        SubjectTitleForAnyCard(stringResource(R.string.about_this_subject))
        ClassesTaken(countClassesTaken)
        SubjectGradeIndicator(averageGradeFormatted)
        Spacer(modifier = Modifier.padding(bottom = 8.dp))
    }
}

@Composable
fun SubjectGradeIndicator(averageGradeFormatted: String) {
    Row {
        AverageGrade(averageGradeFormatted = averageGradeFormatted, text =  R.string.subject_grade_indicator)
    }
}

@Composable
fun ClassCard(studentClass: StudentClass) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = PlannerTheme.colors.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        )
    ) {
        ClassComponents(
            classTitle = studentClass.title,
            startDate = studentClass.start.formattedValue(),
            endDate = studentClass.end.formattedValue(),
            noteTakingLink = studentClass.noteTakingLink
        )
    }
}

@Composable
private fun ClassComponents(classTitle: String, startDate: String, endDate: String, noteTakingLink: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 8.dp)
    ) {
        SubjectTitle(title = classTitle)
        Spacer(modifier = Modifier.padding(top = 16.dp))
        Date(startDate = startDate, endDate = endDate)
        Spacer(modifier = Modifier.padding(top = 4.dp))
        if (noteTakingLink != "") {
            NoteTakingLink(classTitle = classTitle, noteTakingLink = noteTakingLink)
        }
        Spacer(Modifier.padding(bottom = 8.dp))
    }
}

@Composable
fun NoteTakingLink(classTitle: String, noteTakingLink: String) {
    val annotatedLink = buildAnnotatedString {
        withLink(
            LinkAnnotation.Url(
                url = noteTakingLink,
                styles = TextLinkStyles(
                    style = SpanStyle(color = Color.Blue, textDecoration = TextDecoration.Underline)
                )
            )
        ) {
            append("${stringResource(R.string.notetaking_text)} $classTitle ")
        }
    }
    Row {
        Icon(
            painter = painterResource(R.drawable.link),
            contentDescription = stringResource(R.string.notetaking_link),
            tint = PlannerTheme.colors.onSurface
        )
        Text(annotatedLink, modifier = Modifier.padding(start = 8.dp, top = 8.dp), style = Typography.labelMedium)
    }
}

@Composable
fun Date(startDate: String, endDate: String) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.calendar),
                contentDescription = stringResource(R.string.date_icon),
                tint = PlannerTheme.colors.onSurface
            )
            Spacer(modifier = Modifier.padding(end= 8.dp))
            Column {
                Text(
                    text = "${stringResource(R.string.subject_period)}:",
                    style = Typography.labelMedium,
                    color = PlannerTheme.colors.onSurface
                )
                Spacer(modifier = Modifier.padding(start = 16.dp))
                Text(
                    text = "${stringResource(R.string.from)} $startDate → ${
                        stringResource(
                            R.string.to
                        )
                    } $endDate",
                    style = Typography.labelMedium,
                    color = PlannerTheme.colors.onSurface
                )
            }
        }
    }
}

@Composable
fun ExamCard(exam: Exam) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = PlannerTheme.colors.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        )
    ) {
        ExamComponents(exam = exam)
    }
}

@Composable
fun ExamComponents(exam: Exam) {
    Column(
        modifier = Modifier.padding(start = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SubjectTitle(title = exam.name)
        ExamDate(startDate = exam.start.formattedValue(), endDate = exam.end.formattedValue())
        ExamGrade(grade = exam.grade, displayStyle = GradeStyle.FROM_ZERO_TO_ONE_HUNDRED)
    }
}

@Composable
fun ExamDate(startDate: String, endDate: String) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.calendar),
                contentDescription = stringResource(R.string.date_icon),
                tint = PlannerTheme.colors.onSurface
            )
            Column (
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "${stringResource(R.string.subject_period)}:",
                    style = Typography.labelMedium,
                    color = PlannerTheme.colors.onSurface
                )
                Text(
                    text = "${stringResource(R.string.from)} $startDate → ${
                        stringResource(
                            R.string.to
                        )
                    } $endDate",
                    style = Typography.labelMedium,
                    color = PlannerTheme.colors.onSurface
                )
            }
        }
    }
}

@Composable
fun ExamGrade(
    displayStyle: GradeStyle = GradeStyle.FROM_ZERO_TO_ONE_HUNDRED,
    grade: Float,
    @StringRes text: Int = R.string.grade_took
) {
    Row {
        Icon(
            painter = painterResource(R.drawable.grade),
            contentDescription = stringResource(R.string.grade)
        )
        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = stringResource(text) +
                    " ${displayStyle.getValueInDisplayStyle(value = grade)} " +
            stringResource(R.string.on_this_exam),
            style = Typography.labelLarge,
            color = PlannerTheme.colors.onSurface
        )
    }
}