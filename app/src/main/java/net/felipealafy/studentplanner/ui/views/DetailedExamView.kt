package net.felipealafy.studentplanner.ui.views

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import net.felipealafy.studentplanner.R
import net.felipealafy.studentplanner.datamodels.StudentClass
import net.felipealafy.studentplanner.datamodels.Exam
import net.felipealafy.studentplanner.datamodels.GradeStyle
import net.felipealafy.studentplanner.datamodels.Subject
import net.felipealafy.studentplanner.ui.theme.GradeIndicatorProgressGradient
import net.felipealafy.studentplanner.ui.theme.Typography
import net.felipealafy.studentplanner.ui.theme.colorPallet
import java.time.LocalDateTime
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailedExamView(gradeStyle: GradeStyle, subject: Subject) {
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
                            text = stringResource(R.string.exam_view),
                            color = Color(subject.color.getContrastingColorForText())
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
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
                            imageVector = Icons.Default.Delete,
                            contentDescription = stringResource(R.string.delete),
                            tint = Color(subject.color.getContrastingColorForText())
                        )
                    }
                }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(subject.color).copy(0.3F))
        ) {
            Column (modifier = Modifier.fillMaxSize().padding(16.dp)) {
                ExamName(subject = subject, examIndex = 0)
                GradeIndicatorWithLabel(subject = subject, examIndex = 0, gradeStyle = gradeStyle)
            }
        }
    }
}

@Composable
fun ExamName(subject: Subject, examIndex: Int) {
    Text(
        text = subject.exams[examIndex].name,
        style = Typography.headlineLarge,
    )
}

@Composable
fun GradeIndicatorWithLabel(subject: Subject, examIndex: Int, gradeStyle: GradeStyle) {
    Card(
        modifier = Modifier.fillMaxWidth().height(200.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(subject.color).copy(alpha = 0.7F)
        ),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(0.dp),
    ) {
        Column (modifier = Modifier.fillMaxWidth().padding(8.dp)) {
            Text(
                text = stringResource(R.string.performance),
                style = Typography.headlineMedium
            )
            GradeIndicator(subject = subject, examIndex = examIndex)

            Column(
                modifier = Modifier.fillMaxWidth().padding(top = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = gradeStyle.getValueInDisplayStyle(
                        subject.exams[examIndex].grade,
                    ),
                    style = Typography.bodyLarge
                )
                Text(
                    text = stringResource(gradeStyle.getGradePhrase(subject.exams[examIndex].grade)),
                    style = Typography.labelLarge
                )
            }

        }
    }
}

@Composable
fun GradeIndicator(subject: Subject, examIndex: Int) {
    val localDensity = LocalDensity.current
    val gradeProportion = subject.exams.getOrNull(examIndex)?.grade ?: 0.0f
    var barWidthPx by remember { mutableIntStateOf(0) }
    val iconSizeDp = 48.dp
    val iconWidthPx = with(localDensity) { iconSizeDp.roundToPx() }
    val barHeightDp = 30.dp

    val targetOffsetX by remember {
        derivedStateOf {
            if (barWidthPx > 0) {
                val availableWidth = barWidthPx - iconWidthPx
                val pixelOffset = (availableWidth * gradeProportion).toInt()
                with(localDensity) { pixelOffset.toDp() }
            } else {
                0.dp
            }
        }
    }

    val animatedOffsetX: Dp by animateDpAsState(
        targetValue = targetOffsetX,
        animationSpec = tween(durationMillis = 2000),
        label = "IconOffsetXAnimation"
    )

    Box(
        modifier = Modifier
            .padding(top = 10.dp)
            .fillMaxWidth()
            .height(30.dp)
            .onGloballyPositioned { coordinates ->
                barWidthPx = coordinates.size.width
            }
            .background(
                brush = Brush.linearGradient(GradeIndicatorProgressGradient),
                shape = RoundedCornerShape(24.dp)
            )
    ) {
        val offsetY = -(iconSizeDp - (barHeightDp / 4))
        val verticalOffset = - (iconSizeDp / 2) + (barHeightDp / 2)
        val finalOffsetY = verticalOffset - (iconSizeDp * 0.4f) - offsetY

        Icon(
            painter = painterResource(R.drawable.outline_arrow_upward_alt_24),
            contentDescription = stringResource(R.string.arrow_up),
            modifier = Modifier
                .size(iconSizeDp)
                .offset(x = animatedOffsetX, y = finalOffsetY),
            tint = Color.Black
        )
    }
}


@Preview
@Composable
private fun DetailedExamPreview() {
    val subject = Subject(
        plannerId = "0",
        name = "Orientação a objetos",
        color = colorPallet[2][3],
        start = LocalDateTime.now(),
        end = LocalDateTime.now()
    )
    subject.studentClasses += StudentClass(
        id = UUID.randomUUID().toString(),
        subjectId = subject.id,
        title = "Aula 1",
        start = LocalDateTime.now(),
        end = LocalDateTime.now(),
        noteTakingLink = "https://felipealafy.net",
        observation = "Olá Mundo. Eu me chamo Felipe Alafy."
    )
    subject.exams += Exam(
        id = UUID.randomUUID().toString(),
        subjectId = subject.id,
        name = "Avaliação Prática",
        grade = 1F,
        gradeWeight = 40,
        start = LocalDateTime.now(),
        end = LocalDateTime.now(),
    )
    DetailedExamView(gradeStyle = GradeStyle.FROM_ZERO_TO_ONE_HUNDRED, subject = subject)
}