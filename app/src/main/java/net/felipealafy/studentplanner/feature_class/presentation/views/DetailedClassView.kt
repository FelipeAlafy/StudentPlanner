package net.felipealafy.studentplanner.feature_class.presentation.views

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
import net.felipealafy.studentplanner.feature_class.presentation.viewmodels.DetailedStudentClassViewModel
import net.felipealafy.studentplanner.ui.theme.Typography
import androidx.compose.runtime.collectAsState
import net.felipealafy.studentplanner.ui.components.text.label.TopAppBarTitle
import net.felipealafy.studentplanner.ui.extensions.getFormattedDateTime
import net.felipealafy.studentplanner.ui.theme.PlannerTheme
import net.felipealafy.studentplanner.ui.theme.PlannerThemeProvider

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

    PlannerThemeProvider(baseColor = subject.color) {
        Scaffold(
            containerColor = PlannerTheme.colors.container,
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = PlannerTheme.colors.primary
                    ),
                    title = {
                        Box (
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            TopAppBarTitle(
                                text = subject.name,
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onReturnAction) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = stringResource(R.string.back_to_past_view),
                                tint = PlannerTheme.colors.onPrimary
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
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    ClassDate(start = studentClass.start.getFormattedDateTime(), end = studentClass.end.getFormattedDateTime())
                    Spacer(modifier = Modifier.padding(top = 10.dp))
                    InnerClassLink(notetakingLink = studentClass.noteTakingLink, classTitle = studentClass.title)
                    Spacer(modifier = Modifier.padding(top = 10.dp))
                    Observation(observation = studentClass.noteTakingLink)
                }
            }
        }
    }

}

@Composable
fun ClassDate(start: String, end: String) {
    Column {
        Text(
            text = "${stringResource(R.string.when_class_was_take)}:",
            style = Typography.bodyLarge,
            color = PlannerTheme.colors.onSurface
        )
        Spacer(modifier = Modifier.padding(start = 16.dp))
        Text(
            text = "${stringResource(R.string.from)} $start → ${
                stringResource(
                    R.string.to
                )
            } $end",
            style = Typography.labelLarge,
            color = PlannerTheme.colors.onSurface
        )
    }
}

@Composable
fun InnerClassLink(notetakingLink: String, classTitle: String) {
    val annotatedLink = buildAnnotatedString {
        withLink(
            LinkAnnotation.Url(
                url = notetakingLink,
                styles = TextLinkStyles(
                    style = SpanStyle(color = Color.Blue, textDecoration = TextDecoration.Underline)
                )
            )
        ) {
            append("${stringResource(R.string.notetaking_text)} $classTitle ")
        }
    }
    Row (
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(
            painter = painterResource(R.drawable.link),
            contentDescription = stringResource(R.string.notetaking_link),
            tint = PlannerTheme.colors.onSurface
        )
        Text(
            text = annotatedLink,
            modifier = Modifier.padding(start = 8.dp, top = 8.dp),
            style = Typography.labelLarge,
        )
    }
}

@Composable
fun Observation(observation: String) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = PlannerTheme.colors.surface
        ),
        border = BorderStroke(
            width = 2.dp,
            color = PlannerTheme.colors.onSurface,
        ),
        shape = RoundedCornerShape(25.dp),
        modifier = Modifier.fillMaxWidth().fillMaxHeight(0.75F)
    ) {
        Text(
            text = observation,
            style = Typography.bodyMedium,
            modifier = Modifier.padding(12.dp)
        )
    }
}