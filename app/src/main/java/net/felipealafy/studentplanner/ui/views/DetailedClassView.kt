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
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.unit.dp
import net.felipealafy.studentplanner.R
import net.felipealafy.studentplanner.datamodels.StudentClass
import net.felipealafy.studentplanner.datamodels.Subject
import net.felipealafy.studentplanner.viewmodels.DetailedStudentClassViewModel
import net.felipealafy.studentplanner.ui.theme.Typography
import androidx.compose.runtime.collectAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailedClassView(
    viewModel: DetailedStudentClassViewModel,
    onEditMode: (String, String, String) -> Unit,
    onReturnAction: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsState().value
    val subject = uiState.subject
    val studentClass = uiState.classEntry

    if (subject == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else {
                Text(text = "No Subject available.")
            }
        }
        return
    }

    if (studentClass == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else {
                Text(text = "No StudentClass available.")
            }
        }
        return
    }

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
                    IconButton(onClick = onReturnAction) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = stringResource(R.string.back_to_past_view),
                            tint = Color(subject.color.getContrastingColorForText())
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        onEditMode(subject.plannerId, subject.id, studentClass.id)
                    }) {
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
                ClassDate(subject = subject, studentClass)
                Spacer(modifier = Modifier.padding(top = 10.dp))
                InnerClassLink(subject = subject, studentClass)
                Spacer(modifier = Modifier.padding(top = 10.dp))
                Observation(subject = subject, studentClass)
            }
        }
    }
}

@Composable
fun ClassDate(subject: Subject, studentClass: StudentClass) {
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
            } ${studentClass.end.formattedValue()}",
            style = Typography.labelLarge,
            color = Color(subject.color.getContrastingColorForText())
        )
    }
}

@Composable
fun InnerClassLink(subject: Subject, studentClass: StudentClass) {
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
fun Observation(subject: Subject, studentClass: StudentClass) {

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
            text = studentClass.observation,
            style = Typography.bodyMedium,
            modifier = Modifier.padding(12.dp)
        )
    }
}