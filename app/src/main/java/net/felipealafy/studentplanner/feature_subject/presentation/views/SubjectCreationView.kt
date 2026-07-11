package net.felipealafy.studentplanner.feature_subject.presentation.views

import TextInputWithColor
import android.widget.Toast
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.collectLatest
import net.felipealafy.studentplanner.R
import net.felipealafy.studentplanner.core.ui.components.color.chooser.ButtonOpenColorSelectionDialog
import net.felipealafy.studentplanner.core.ui.components.color.chooser.ButtonWithBackgroundColor
import net.felipealafy.studentplanner.core.ui.components.color.chooser.ColorSelectionDialog
import net.felipealafy.studentplanner.core.ui.date.time.picker.DateTimePickerDialog
import net.felipealafy.studentplanner.core.ui.extensions.getFormattedDateTime
import net.felipealafy.studentplanner.core.ui.theme.PlannerTheme
import net.felipealafy.studentplanner.core.ui.theme.PlannerThemeProvider
import net.felipealafy.studentplanner.core.ui.theme.Typography
import net.felipealafy.studentplanner.core.ui.theme.colorPallet
import net.felipealafy.studentplanner.feature_subject.presentation.viewmodels.SubjectCreationEvent
import net.felipealafy.studentplanner.feature_subject.presentation.viewmodels.SubjectCreationUiState
import net.felipealafy.studentplanner.feature_subject.presentation.viewmodels.SubjectCreationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectCreationView(viewModel: SubjectCreationViewModel, onReturnAction: () -> Unit) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is SubjectCreationEvent.ShowError -> Toast.makeText(context, event.messageResId,
                    Toast.LENGTH_SHORT).show()
                SubjectCreationEvent.SubjectCreatedSuccessfully -> Toast.makeText(context,
                    R.string.created_successfully, Toast.LENGTH_SHORT).show()
            }
        }
    }


    val showColorChooserDialog = rememberSaveable { mutableStateOf(false) }
    val showStartDateTimeSelectorDialog = rememberSaveable { mutableStateOf(false) }
    val showEndDateTimeSelectorDialog = rememberSaveable { mutableStateOf(false) }


    when (val state = uiState) {
        is SubjectCreationUiState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(colorPallet[0][1])),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is SubjectCreationUiState.Error -> {
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

        is SubjectCreationUiState.Success -> {
            PlannerThemeProvider(state.form.color) {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Text(
                                        text = stringResource(R.string.subject_creation_title),
                                        style = Typography.headlineMedium,
                                        color = PlannerTheme.colors.onSurface
                                    )
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
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = PlannerTheme.colors.surface
                            )
                        )
                    }
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .background(PlannerTheme.colors.background),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Column(
                            Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = stringResource(R.string.create_new_subject),
                                style = Typography.headlineLarge,
                                color = PlannerTheme.colors.onSurface
                            )

                            TextInputWithColor(
                                text = state.form.name,
                                onValueChange = { newName ->
                                    viewModel.updateName(newName)
                                },
                                hint = R.string.subject_name,
                            )
                            Row(
                                modifier = Modifier
                                    .padding(top = 32.dp)
                                    .fillMaxWidth()
                                    .height(32.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = stringResource(R.string.subject_color_selection),
                                    style = Typography.bodyMedium,
                                    color = PlannerTheme.colors.onSurface
                                )
                                Spacer(modifier = Modifier.padding(start = 20.dp))
                                ButtonOpenColorSelectionDialog(
                                    onClick = {
                                        showColorChooserDialog.value = true
                                    }
                                )
                            }

                            if (showColorChooserDialog.value) {
                                ColorSelectionDialog(
                                    onColorSelected = { newColor ->
                                        viewModel.updateColor(newColor)
                                    },
                                    onDismissRequest = {
                                        showColorChooserDialog.value = false
                                    }
                                )
                            }

                            Spacer(modifier = Modifier.padding(top = 32.dp))
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 8.dp, end = 8.dp)
                                    .height(256.dp)
                                    .border(
                                        width = 2.dp,
                                        color = Gray,
                                        shape = RoundedCornerShape(32.dp)
                                    ),
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = stringResource(R.string.subject_date_time_start),
                                    style = Typography.bodyMedium,
                                    modifier = Modifier.padding(start = 16.dp),
                                    color = PlannerTheme.colors.onSurface
                                )

                                DateTimeSelector(
                                    onClick = {
                                        showStartDateTimeSelectorDialog.value = true
                                    },
                                    dateTime = state.form.start.getFormattedDateTime(),
                                )

                                if (showStartDateTimeSelectorDialog.value) {
                                    DateTimePickerDialog(
                                        initialDateTime = state.form.start,
                                        onDismissRequest = {
                                            showStartDateTimeSelectorDialog.value = false
                                        },
                                        onDateTimeSelected = { dateMillis, hour, minute ->
                                            viewModel.updateStartDateTime(dateMillis, hour, minute)
                                        },
                                    )
                                }

                                Spacer(modifier = Modifier.padding(top = 16.dp))
                                Text(
                                    text = stringResource(R.string.subject_date_time_end),
                                    style = Typography.bodyMedium,
                                    modifier = Modifier.padding(start = 16.dp),
                                    color = PlannerTheme.colors.onSurface
                                )
                                DateTimeSelector(
                                    onClick = {
                                        showEndDateTimeSelectorDialog.value = true
                                    },
                                    dateTime = state.form.end.getFormattedDateTime(),
                                )

                                if (showEndDateTimeSelectorDialog.value) {
                                    DateTimePickerDialog(
                                        initialDateTime = state.form.end,
                                        onDismissRequest = {
                                            showEndDateTimeSelectorDialog.value = false
                                        },
                                        onDateTimeSelected = { dateMillis, hour, minute ->
                                            viewModel.updateEndDateTime(dateMillis, hour, minute)
                                        },
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.padding(top = 32.dp))
                            ButtonWithBackgroundColor(
                                onClick = {
                                    viewModel.saveSubject()
                                    onReturnAction()
                                },
                                placeholderTextPath = R.string.create_button
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DateTimeSelector(
    onClick: () -> Unit,
    dateTime: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            onClick = onClick
        ) {
            Icon(
                painter = painterResource(R.drawable.calendar_start),
                contentDescription = stringResource(R.string.start_date_time),
                tint = PlannerTheme.colors.onSurface
            )
        }
        Text(
            text = dateTime,
            style = Typography.labelLarge,
            color = PlannerTheme.colors.onCardTop
        )
    }
}