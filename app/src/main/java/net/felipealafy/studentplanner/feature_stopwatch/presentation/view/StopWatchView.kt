package net.felipealafy.studentplanner.feature_stopwatch.presentation.view

import android.graphics.drawable.Icon
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.collectLatest
import net.felipealafy.studentplanner.R
import net.felipealafy.studentplanner.core.ui.components.color.chooser.ButtonWithBackgroundColor
import net.felipealafy.studentplanner.core.ui.components.text.label.TopAppBarTitle
import net.felipealafy.studentplanner.core.ui.theme.PlannerTheme
import net.felipealafy.studentplanner.core.ui.theme.PlannerThemeProvider
import net.felipealafy.studentplanner.core.ui.theme.Typography
import net.felipealafy.studentplanner.core.ui.theme.colorPallet
import net.felipealafy.studentplanner.feature_stopwatch.domain.model.StopwatchEvent
import net.felipealafy.studentplanner.feature_stopwatch.domain.model.StopwatchStates
import net.felipealafy.studentplanner.feature_stopwatch.presentation.viewmodel.StopWatchUiState.Error
import net.felipealafy.studentplanner.feature_stopwatch.presentation.viewmodel.StopWatchUiState.Loading
import net.felipealafy.studentplanner.feature_stopwatch.presentation.viewmodel.StopWatchUiState.Success
import net.felipealafy.studentplanner.feature_stopwatch.presentation.viewmodel.StopWatchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StopWatchView(
    onReturnAction: () -> Unit,
    onNavigateToCreateClass: (plannerId: String, startMillis: Long, endMillis: Long) -> Unit,
    onNavigateToCreateExam: (plannerId: String, startMillis: Long, endMillis: Long) -> Unit,
    viewModel: StopWatchViewModel
) {

    LaunchedEffect(key1 = Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is StopwatchEvent.NavigateToCreateClass -> {
                    onNavigateToCreateClass(event.plannerId, event.startMillis, event.endMillis)
                }
                is StopwatchEvent.NavigateToExam -> {
                    onNavigateToCreateExam(event.plannerId, event.startMillis, event.endMillis)
                }
            }
        }
    }

    when (val state = viewModel.uiState.collectAsState().value) {
        is Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(colorPallet[0][1])),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is Error -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = state.exception)

                Spacer(modifier = Modifier.height(16.dp))

                IconButton(onClick = onReturnAction) {
                    Icon(
                        painter = painterResource(R.drawable.back_arrow),
                        contentDescription = stringResource(R.string.back_to_past_view)
                    )
                }
            }
        }

        is Success -> {
            val form = state.stopWatchUiModel
            PlannerThemeProvider(baseColor = form.planner.color) {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            colors = TopAppBarDefaults.topAppBarColors(containerColor = PlannerTheme.colors.surface),
                            title = {
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    TopAppBarTitle(stringResource(R.string.stopwatch_title))
                                }
                            },
                            navigationIcon = {
                                IconButton(
                                    onClick = onReturnAction
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

                                    }
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.hourglass),
                                        contentDescription = stringResource(R.string.hourglass_icon),
                                        tint = PlannerTheme.colors.onSurface
                                    )
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = PlannerTheme.colors.background)
                            .padding(innerPadding),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row {
                            StopWatchCard(form.currentHourage)
                            StopWatchText(":")
                            StopWatchCard(form.currentMinutage)
                            StopWatchText(":")
                            StopWatchCard(form.currentSeconds)
                        }

                        Spacer(modifier = Modifier.padding(top = 16.dp))

                        Row {
                            IconButton(
                                onClick = {
                                    if (form.stopwatchState == StopwatchStates.Running) {
                                        viewModel.pauseStopWatch()
                                    }
                                    else {
                                        viewModel.startStopWatch()
                                    }
                                }
                            ) {
                                Icon(
                                    painter = if (form.stopwatchState == StopwatchStates.Running) {
                                        painterResource(R.drawable.pause)
                                    } else {
                                        painterResource(R.drawable.play)
                                    },
                                    contentDescription = if (form.stopwatchState == StopwatchStates.Running) {
                                        stringResource(R.string.stopwatch_button_pause)
                                    } else {
                                        stringResource(R.string.stopwatch_button_start)
                                    },
                                    tint = PlannerTheme.colors.primary
                                )
                            }
                            if (form.stopwatchState == StopwatchStates.Running || form.stopwatchState == StopwatchStates.Paused) {
                                IconButton(
                                    onClick = {
                                        viewModel.finishStopWatch()
                                    },
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.check_icon),
                                        contentDescription = stringResource(R.string.stopwatch_finish),
                                        tint = PlannerTheme.colors.primary
                                    )
                                }
                            }
                            if (form.stopwatchState == StopwatchStates.Finished) {
                                IconButton(
                                    onClick = {
                                        viewModel.resetStopwatch()
                                    }
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.baseline_close),
                                        contentDescription = stringResource(R.string.stopwatch_reset),
                                        tint = PlannerTheme.colors.error
                                    )
                                }
                            }
                        }
                        if (form.stopwatchState == StopwatchStates.Finished) {
                            ButtonWithBackgroundColor(
                                onClick = {
                                    viewModel.addTimeToStudentClass()
                                },
                                placeholderTextPath = R.string.add_time_to_class
                            )

                            ButtonWithBackgroundColor(
                                onClick = {
                                    viewModel.addTimeToExam()
                                },
                                placeholderTextPath = R.string.add_time_to_exam
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StopWatchCard(text: String) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = PlannerTheme.colors.surface,
            contentColor = PlannerTheme.colors.onSurface
        )
    ) {
        StopWatchText(text)
    }
}

@Composable
fun StopWatchText(text: String) {
    Text(
        text = text,
        color = PlannerTheme.colors.primary,
        style = Typography.displayLarge,
    )
}

@Preview(showBackground = true)
@Composable
fun StopWatchPreview() {
    PlannerThemeProvider(baseColor = colorPallet[6][4]) {
        Row {
            StopWatchCard("13")
            StopWatchText(":")
            StopWatchCard("00")
        }
    }
}