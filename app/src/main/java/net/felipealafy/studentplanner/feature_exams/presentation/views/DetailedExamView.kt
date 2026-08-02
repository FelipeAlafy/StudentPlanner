package net.felipealafy.studentplanner.feature_exams.presentation.views

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import net.felipealafy.studentplanner.R
import net.felipealafy.studentplanner.feature_exams.data.local.Exam
import net.felipealafy.studentplanner.feature_exams.domain.use_case.GradeStyle
import net.felipealafy.studentplanner.feature_exams.presentation.viewmodels.DetailedExamUiState
import net.felipealafy.studentplanner.feature_exams.presentation.viewmodels.DetailedExamViewModel
import net.felipealafy.studentplanner.core.ui.components.text.label.TopAppBarTitle
import net.felipealafy.studentplanner.core.ui.theme.GradeIndicatorProgressGradient
import net.felipealafy.studentplanner.core.ui.theme.PlannerTheme
import net.felipealafy.studentplanner.core.ui.theme.PlannerThemeProvider
import net.felipealafy.studentplanner.core.ui.theme.Typography
import net.felipealafy.studentplanner.core.ui.theme.colorPallet
import net.felipealafy.studentplanner.core.ui.extensions.getGradePhrase
import net.felipealafy.studentplanner.core.ui.extensions.getValueInDisplayStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailedExamView(
    viewModel: DetailedExamViewModel,
    onEditMode: (String, String, String) -> Unit,
    onReturnAction: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    when (val state = uiState) {
        is DetailedExamUiState.Error -> {
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
        is DetailedExamUiState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(colorPallet[0][1])),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is DetailedExamUiState.Success -> {
            PlannerThemeProvider(state.subject.color) {
                Scaffold(
                    containerColor = PlannerTheme.colors.background,
                    topBar = {
                        TopAppBar(
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = PlannerTheme.colors.surface
                            ),
                            title = {
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    TopAppBarTitle(
                                        text = stringResource(R.string.exam_view),
                                    )
                                }
                            },
                            navigationIcon = {
                                IconButton(
                                    onClick = { onReturnAction() }
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.back_arrow),
                                        contentDescription = stringResource(R.string.back_to_past_view),
                                        tint = PlannerTheme.colors.onSurface
                                    )
                                }
                            },
                            actions = {
                                IconButton(
                                    onClick = {
                                        onEditMode(
                                            state.planner.id,
                                            state.subject.id,
                                            state.exam.id
                                        )
                                    }
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.edit_document),
                                        contentDescription = stringResource(R.string.go_on_edit_mode_for_edit),
                                        tint = PlannerTheme.colors.onSurface
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
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {
                            ExamName(exam = state.exam)
                            GradeIndicatorWithLabel(exam = state.exam, gradeStyle = state.planner.gradeDisplayStyle)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ExamName(exam: Exam) {
    Text(
        text = exam.name,
        style = Typography.headlineLarge,
        color = PlannerTheme.colors.onContainer
    )
}

@Composable
fun GradeIndicatorWithLabel(exam: Exam, gradeStyle: GradeStyle) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        colors = CardDefaults.cardColors(
            containerColor = PlannerTheme.colors.cardTop
        ),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(0.dp),
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)) {
            Text(
                text = stringResource(R.string.performance),
                style = Typography.headlineMedium,
                color = PlannerTheme.colors.onCardTop
            )
            GradeIndicator(exam = exam)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = gradeStyle.getValueInDisplayStyle(
                        exam.grade
                    ),
                    style = Typography.bodyLarge,
                    color = PlannerTheme.colors.onCardTop
                )
                Text(
                    text = stringResource(gradeStyle.getGradePhrase(exam.grade)),
                    style = Typography.labelLarge,
                    color = PlannerTheme.colors.onCardTop
                )
            }
        }
    }
}

@Composable
fun GradeIndicator(exam: Exam) {
    val localDensity = LocalDensity.current
    val gradeProportion = (exam.grade / 100F)
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
        val verticalOffset = -(iconSizeDp / 2) + (barHeightDp / 2)
        val finalOffsetY = verticalOffset - (iconSizeDp * 0.4f) - offsetY

        Icon(
            painter = painterResource(R.drawable.outline_arrow_upward_alt_24),
            contentDescription = stringResource(R.string.arrow_up),
            modifier = Modifier
                .size(iconSizeDp)
                .offset(x = animatedOffsetX, y = finalOffsetY),
            tint = PlannerTheme.colors.onCardTop
        )
    }
}