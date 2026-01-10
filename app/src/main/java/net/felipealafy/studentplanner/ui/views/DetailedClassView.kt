package net.felipealafy.studentplanner.ui.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.felipealafy.studentplanner.R
import net.felipealafy.studentplanner.datamodels.StudentClass
import net.felipealafy.studentplanner.datamodels.Subject
import net.felipealafy.studentplanner.ui.theme.Typography
import net.felipealafy.studentplanner.ui.theme.colorPallet
import java.time.LocalDateTime
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailedClassView(
    subject: Subject
) {
    Scaffold(
        modifier = Modifier.background(Color(subject.color)),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(subject.color)
                ),
                title = {
                    Box (
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = subject.name,
                            color = Color(subject.color.getContrastingColorForText())
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = stringResource(R.string.back_to_past_view),
                            tint = Color(subject.color.getContrastingColorForText())
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = stringResource(R.string.go_on_edit_mode_for_planner),
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
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                ClassDate(subject = subject, classIndex = 0)
                Spacer(modifier = Modifier.padding(top = 10.dp))
                InnerClassLink(subject = subject, classIndex = 0)
                Spacer(modifier = Modifier.padding(top = 10.dp))
                Observation(subject = subject, classIndex = 0)
            }
        }
    }
}

@Composable
fun ClassDate(subject: Subject, classIndex: Int) {
    val scClass = subject.studentClasses[classIndex]
    Column {
        Text(
            text = "${stringResource(R.string.when_class_was_take)}:",
            style = Typography.bodyLarge,
            color = Color(subject.color.getContrastingColorForText())
        )
        Spacer(modifier = Modifier.padding(start = 16.dp))
        Text(
            text = "${stringResource(R.string.from)} ${subject.start.formattedValue()} → ${
                stringResource(
                    R.string.to
                )
            } ${scClass.end.formattedValue()}",
            style = Typography.labelLarge,
            color = Color(subject.color.getContrastingColorForText())
        )
    }
}

@Composable
fun InnerClassLink(subject: Subject, classIndex: Int) {
    val scClass = subject.studentClasses[classIndex]
    val annotatedLink = buildAnnotatedString {
        withLink(
            LinkAnnotation.Url(
                url = scClass.noteTakingLink,
                styles = TextLinkStyles(
                    style = SpanStyle(color = Color.Blue, textDecoration = TextDecoration.Underline)
                )
            )
        ) {
            append("${stringResource(R.string.notetaking_text)} ${scClass.title} ")
        }
    }
    Row (
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(
            painter = painterResource(R.drawable.link),
            contentDescription = stringResource(R.string.notetaking_link),
            tint = Color(subject.color.getContrastingColorForText())
        )
        Text(
            text = annotatedLink,
            modifier = Modifier.padding(start = 8.dp, top = 8.dp),
            style = Typography.labelLarge,
        )
    }
}

@Composable
fun Observation(subject: Subject, classIndex: Int) {

    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(subject.color)
        ),
        border = BorderStroke(
            width = 2.dp,
            color = Color(subject.color.getContrastingColorForText()),
        ),
        shape = RoundedCornerShape(25.dp),
        modifier = Modifier.fillMaxWidth().fillMaxHeight(0.75F)
    ) {
        Text(
            text = subject.studentClasses[classIndex].observation,
            style = Typography.bodyMedium,
            modifier = Modifier.padding(12.dp)
        )
    }
}

@Preview
@Composable
fun DetailedClassPreview() {
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
    DetailedClassView(subject = subject)
}