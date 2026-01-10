package net.felipealafy.studentplanner.ui.views

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.felipealafy.studentplanner.R
import net.felipealafy.studentplanner.datamodels.GradeStyle
import net.felipealafy.studentplanner.datamodels.Planner
import net.felipealafy.studentplanner.datamodels.Subject
import net.felipealafy.studentplanner.ui.theme.Typography
import net.felipealafy.studentplanner.ui.theme.colorPallet
import java.time.LocalDateTime
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailedPlannerView(planner: Planner) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = planner.name,
                            color = Color(planner.color.getContrastingColorForText()),
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(planner.color)
                ),
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = stringResource(R.string.back_to_past_view),
                            tint = Color(planner.color.getContrastingColorForText())
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = stringResource(R.string.go_on_edit_mode_for_planner),
                            tint = Color(planner.color.getContrastingColorForText())
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        val subjects = listOf(
            Subject(
                plannerId = "0",
                name = "Orientação a objetos",
                color = colorPallet[2][3],
                start = LocalDateTime.now(),
                end = LocalDateTime.now()
            ),
            Subject(
                plannerId = "0",
                name = "Sistema Gerenciador de Banco de Dados",
                color = colorPallet[1][2],
                start = LocalDateTime.now(),
                end = LocalDateTime.now(),
            )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(planner.color).copy(0.3F))
                .padding(innerPadding)
        ) {
            TopPlannerCard(planner)
            SubjectsColumn(subjects = subjects, planner = planner)
        }
    }
}

@Composable
fun TopPlannerCard(planner: Planner) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color(planner.color).copy(alpha = .75F),
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
            title = stringResource(R.string.on_going_planner_progress),
            planner = planner
        )
        ProgressIndicator(percentage = .75F, planner = planner)
        SubjectsFinished(planner = planner)
        Spacer(modifier = Modifier.padding(bottom = 4.dp))
        AverageGradeForAllSubjects(
            average = 85F,
            displayStyle = GradeStyle.FROM_A_TO_F_WITH_E,
            percentage = 75F,
            planner = planner
        )
        Spacer(modifier = Modifier.padding(bottom = 8.dp))
    }
}

@Composable
fun AverageGradeForAllSubjects(
    average: Float,
    displayStyle: GradeStyle = GradeStyle.FROM_ZERO_TO_ONE_HUNDRED,
    percentage: Float,
    planner: Planner
) {
    Text(
        text = "${
            if (percentage.getPercentageFromZeroToOneHundred() == 100F) {
                stringResource(R.string.average_grade_for_all_subjects_done)
            } else {
                stringResource(R.string.average_grade_for_all_subjects)
            }
        } ${displayStyle.getValueInDisplayStyleForAverage(average)}",
        style = Typography.labelLarge,
        color = Color(planner.color.getContrastingColorForText())
    )
}

@Composable
private fun SubjectsFinished(planner: Planner) {
    Text(
        text = stringResource(R.string.subjects_done) +
                " ${5} ${stringResource(R.string.subjects)}.",
        style = Typography.labelLarge,
        color = Color(planner.color.getContrastingColorForText())
    )
}

@Composable
fun ProgressIndicator(percentage: Float, planner: Planner) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProgressIndicatorWithPercentage(percentage, planner = planner)

    }
}

@Composable
private fun ProgressIndicatorWithPercentage(percentage: Float, planner: Planner) {

    Row {
        ProgressBar(percentage = percentage, planner = planner)
        PercentageText(percentage = percentage, planner = planner)
    }


}

@Composable
fun ProgressBar(percentage: Float, planner: Planner) {
    val contrastColor = Color(planner.color.getContrastingColorForProgressIndicator())
    LinearProgressIndicator(
        progress = {
            percentage.getPercentageFromZeroToOne()
        },
        color = ProgressIndicatorDefaults.linearColor.copy(
            alpha = 1F,
            red = contrastColor.red,
            green = contrastColor.green,
            blue = contrastColor.blue
        ),
        trackColor = contrastColor.copy(alpha = 0.3F),
        modifier = Modifier
            .fillMaxWidth(.7F)
            .height(20.dp)
    )
}

@Composable
fun PercentageText(percentage: Float, planner: Planner) {
    Text(
        text = "${percentage.getPercentageFromZeroToOneHundred()}%",
        style = Typography.labelLarge,
        color = Color(planner.color.getContrastingColorForText()),
        modifier = Modifier.padding(start = 8.dp)
    )
}

@Composable
fun SubjectsColumn(subjects: List<Subject>, planner: Planner) {
    LazyColumn {
        items(subjects) { subject ->
            SubjectCard(subject = subject, planner = planner)
        }
    }
}

@Composable
fun SubjectCard(subject: Subject, planner: Planner) {
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
        SubjectCardComponents(subject = subject, planner = planner)
    }
}

@Composable
private fun SubjectCardComponents(subject: Subject, planner: Planner) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 8.dp)
    ) {
        SubjectTitle(subject.name, subject = subject)
        Spacer(modifier = Modifier.padding(top = 16.dp))
        Date(subject = subject)
        Spacer(modifier = Modifier.padding(top = 4.dp))
        AverageGrade(
            subject = subject,
            displayStyle = GradeStyle.FROM_ZERO_TO_ONE_HUNDRED,
            planner = planner
        )
        Spacer(modifier = Modifier.padding(top = 8.dp))
        ClassesTaken(subject = subject)
        Spacer(modifier = Modifier.padding(bottom = 8.dp))
    }
}

@Composable
fun AverageGrade(subject: Subject, displayStyle: GradeStyle, planner: Planner) {
    val average = subject.exams.getExamsAverage()
    val borderColor = if (average >= planner.minimumGradeToPass) {
        Color(subject.color.getContrastingColorForProgressIndicator())
    } else {
        Color(colorPallet[4][0])
    }
    val isApproved = if (average >= planner.minimumGradeToPass) {
        stringResource(R.string.approved)
    } else {
        stringResource(R.string.failed)
    }

    Row {
        Icon(
            painter = painterResource(R.drawable.grade),
            contentDescription = stringResource(R.string.grade)
        )
        Text(
            text = "${stringResource(R.string.average_grade_for_subject)} " +
                    displayStyle.getValueInDisplayStyleForAverage(
                        average =
                            average
                    ),
            style = Typography.labelLarge,
            color = Color(subject.color.getContrastingColorForText())
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
                imageVector = Icons.Filled.DateRange,
                contentDescription = stringResource(R.string.date_icon),
                tint = Color(subject.color.getContrastingColorForText())
            )
            Text(
                text = "${stringResource(R.string.subject_period)}:",
                style = Typography.labelMedium,
                modifier = Modifier.padding(start = 8.dp),
                color = Color(subject.color.getContrastingColorForText())
            )
        }
        Row {
            Spacer(modifier = Modifier.padding(start = 16.dp))
            Text(
                text = "${stringResource(R.string.from)} ${subject.start.formattedValue()} → ${
                    stringResource(
                        R.string.to
                    )
                } ${subject.start.formattedValue()}",
                style = Typography.labelMedium,
                color = Color(subject.color.getContrastingColorForText())
            )
        }
    }
}

@Composable
fun SubjectTitle(title: String, subject: Subject) {
    Text(
        modifier = Modifier.padding(top = 8.dp),
        text = title,
        style = Typography.bodyMedium,
        color = Color(subject.color.getContrastingColorForText()),
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
    )
}


@Preview
@Composable
private fun DetailedPlannerViewPreview() {
    val planner = Planner(
        id = UUID.randomUUID().toString(),
        name = "CC",
        color = colorPallet[6][0],
        minimumGradeToPass = 70F
    )
    DetailedPlannerView(planner)
}