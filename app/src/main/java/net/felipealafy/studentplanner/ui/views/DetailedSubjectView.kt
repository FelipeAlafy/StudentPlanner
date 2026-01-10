package net.felipealafy.studentplanner.ui.views

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import net.felipealafy.studentplanner.datamodels.GradeStyle
import net.felipealafy.studentplanner.datamodels.Subject
import net.felipealafy.studentplanner.datamodels.StudentClass
import net.felipealafy.studentplanner.datamodels.Exam
import net.felipealafy.studentplanner.models.DetailedSubjectViewModel
import net.felipealafy.studentplanner.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailedSubjectView(detailedSubjectViewModel: DetailedSubjectViewModel, id: String) {
    val uiState by detailedSubjectViewModel.uiState.collectAsState()
    val subject = uiState.subject ?: return
    val classes = uiState.studentClasses
    val exams = uiState.exams

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = subject.name,
                            color = Color(
                                subject.color.getContrastingColorForText()
                            )
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(subject.color)
                ),
                navigationIcon = {
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_to_past_view),
                            tint = Color(subject.color.getContrastingColorForText())
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = stringResource(R.string.go_on_edit_mode_for_subject),
                            tint = Color(subject.color.getContrastingColorForText())
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
                .background(Color(subject.color).copy(0.3F))
        ) {
            SubjectBox(subject)

            LazyColumn {
                items(classes) {
                    ClassCard(subject = subject, studentClass = it)
                }


                items(exams) {
                    ExamCard(subject = subject, exam = it)
                }
            }

        }
    }
}

@Composable
fun SubjectBox(subject: Subject) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color(subject.color).copy(alpha = .75F),
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
        SubjectTitleForAnyCard(stringResource(R.string.about_this_subject), subject)
        ClassesTaken(subject = subject)
        SubjectGradeIndicator(subject)
        Spacer(modifier = Modifier.padding(bottom = 8.dp))
    }
}

@Composable
fun SubjectGradeIndicator(subject: Subject) {
    Row {
        AverageGrade(subject = subject, text =  R.string.subject_grade_indicator)
    }
}

@Composable
fun ClassCard(subject: Subject, studentClass: StudentClass) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(subject.color)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        )
    ) {
        ClassComponents(subject = subject, studentClass = studentClass)
    }
}

@Composable
private fun ClassComponents(subject: Subject, studentClass: StudentClass) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 8.dp)
    ) {
        SubjectTitle(title = studentClass.title, subject = subject)
        Spacer(modifier = Modifier.padding(top = 16.dp))
        Date(subject = subject, studentClass)
        Spacer(modifier = Modifier.padding(top = 4.dp))
        if (!studentClass.noteTakingLink.isEmpty()) {
            NoteTakingLink(studentClass = studentClass, subject = subject)
        }
        Spacer(Modifier.padding(bottom = 8.dp))
    }
}

@Composable
fun NoteTakingLink(studentClass: StudentClass, subject: Subject) {
    val annotatedLink = buildAnnotatedString {
        withLink(
            LinkAnnotation.Url(
                url = studentClass.noteTakingLink,
                styles = TextLinkStyles(
                    style = SpanStyle(color = Color.Blue, textDecoration = TextDecoration.Underline)
                )
            )
        ) {
            append("${stringResource(R.string.notetaking_text)} ${studentClass.title} ")
        }
    }
    Row {
        Icon(
            painter = painterResource(R.drawable.link),
            contentDescription = stringResource(R.string.notetaking_link),
            tint = Color(subject.color.getContrastingColorForText())
        )
        Text(annotatedLink, modifier = Modifier.padding(start = 8.dp, top = 8.dp), style = Typography.labelMedium)
    }
}

@Composable
fun Date(subject: Subject, studentClass: StudentClass) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.DateRange,
                contentDescription = stringResource(R.string.date_icon),
                tint = Color(subject.color.getContrastingColorForText())
            )
            Spacer(modifier = Modifier.padding(end= 8.dp))
            Column {
                Text(
                    text = "${stringResource(R.string.subject_period)}:",
                    style = Typography.labelMedium,
                    color = Color(subject.color.getContrastingColorForText())
                )
                Spacer(modifier = Modifier.padding(start = 16.dp))
                Text(
                    text = "${stringResource(R.string.from)} ${studentClass.start.formattedValue()} → ${
                        stringResource(
                            R.string.to
                        )
                    } ${studentClass.end.formattedValue()}",
                    style = Typography.labelMedium,
                    color = Color(subject.color.getContrastingColorForText())
                )
            }
        }
    }
}

@Composable
fun ExamCard(subject: Subject, exam: Exam) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(subject.color)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        )
    ) {
        ExamComponents(subject = subject, exam = exam)
    }
}

@Composable
fun ExamComponents(exam: Exam, subject: Subject) {
    Column(
        modifier = Modifier.padding(start = 8.dp)
    ) {
        SubjectTitle(title = exam.name, subject = subject)
        Spacer(modifier = Modifier.padding(top = 16.dp))
        ExamDate(subject = subject, exam = exam)
        Spacer(modifier = Modifier.padding(top = 4.dp))
        ExamGrade(exam = exam, subject = subject, displayStyle = GradeStyle.FROM_ZERO_TO_ONE_HUNDRED)
        Spacer(Modifier.padding(bottom = 8.dp))
    }
}

@Composable
fun ExamDate(subject: Subject, exam: Exam) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.DateRange,
                contentDescription = stringResource(R.string.date_icon),
                tint = Color(subject.color.getContrastingColorForText())
            )
            Spacer(modifier = Modifier.padding(end= 8.dp))
            Column {
                Text(
                    text = "${stringResource(R.string.subject_period)}:",
                    style = Typography.labelMedium,
                    color = Color(subject.color.getContrastingColorForText())
                )
                Spacer(modifier = Modifier.padding(start = 16.dp))
                Text(
                    text = "${stringResource(R.string.from)} ${exam.start.formattedValue()} → ${
                        stringResource(
                            R.string.to
                        )
                    } ${exam.end.formattedValue()}",
                    style = Typography.labelMedium,
                    color = Color(subject.color.getContrastingColorForText())
                )
            }
        }
    }
}

@Composable
fun ExamGrade(
    displayStyle: GradeStyle = GradeStyle.FROM_ZERO_TO_ONE_HUNDRED,
    exam: Exam,
    subject: Subject,
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
                    " ${displayStyle.getValueInDisplayStyle(value = exam.grade)} " +
            stringResource(R.string.on_this_exam),
            style = Typography.labelLarge,
            color = Color(subject.color.getContrastingColorForText())
        )
    }
}