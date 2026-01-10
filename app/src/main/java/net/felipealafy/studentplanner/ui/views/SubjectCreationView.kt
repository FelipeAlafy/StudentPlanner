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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import net.felipealafy.studentplanner.R
import net.felipealafy.studentplanner.models.SubjectCreationViewModel
import net.felipealafy.studentplanner.ui.date.time.picker.DateTimePickerDialog
import net.felipealafy.studentplanner.ui.extensions.getFormattedDateTime
import net.felipealafy.studentplanner.ui.theme.DarkGray
import net.felipealafy.studentplanner.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectCreationView(viewModel: SubjectCreationViewModel, onReturn: () -> Unit) {
    val uiState = viewModel.uiState
    val showColorChooserDialog = rememberSaveable { mutableStateOf(false) }
    val showStartDateTimeSelectorDialog = rememberSaveable { mutableStateOf(false) }
    val showEndDateTimeSelectorDialog = rememberSaveable { mutableStateOf(false) }

    if (uiState.collectAsState().value.planner == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White), // Cor de fundo neutra
            contentAlignment = Alignment.Center
        ) {
            if (uiState.collectAsState().value.isDataLoading) {
                CircularProgressIndicator()
            } else {
                Text(text = "No planners available.")
            }
        }
        return
    }

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
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onReturn
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = stringResource(R.string.back_to_past_view)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(uiState.collectAsState().value.currentSubjectEntry.color)
                )
            )
        },
        containerColor = Color(uiState.collectAsState().value.currentSubjectEntry.color).copy(alpha = 0.75F)
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.create_new_subject),
                    style = Typography.headlineLarge,
                    color = Color(uiState.collectAsState().value.currentSubjectEntry.color.getContrastingColorForText())
                )

                TextInputWithColor(
                    text = uiState.collectAsState().value.currentSubjectEntry.name,
                    onValueChange = { newName ->
                        viewModel.updateName(newName)
                    },
                    selectedColor = uiState.collectAsState().value.currentSubjectEntry.color,
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
                        color = Color(uiState.collectAsState().value.currentSubjectEntry.color.getContrastingColorForText())
                    )
                    Spacer(modifier = Modifier.padding(start = 20.dp))
                    ButtonOpenColorSelectionDialog(
                        selectedColor = uiState.collectAsState().value.currentSubjectEntry.color,
                        onClick = {
                            showColorChooserDialog.value = true
                        }
                    )
                }

                if (showColorChooserDialog.value) {
                    ColorSelectionDialog(
                        selectedColor = uiState.collectAsState().value.currentSubjectEntry.color,
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
                        color = Color(uiState.collectAsState().value.currentSubjectEntry.color.getContrastingColorForText())
                    )

                    DateTimeSelector(
                        onClick = {
                            showStartDateTimeSelectorDialog.value = true
                        },
                        dateTime = uiState.collectAsState().value.currentSubjectEntry.start.getFormattedDateTime(),
                        selectedColor = uiState.collectAsState().value.currentSubjectEntry.color
                    )

                    if (showStartDateTimeSelectorDialog.value) {
                        DateTimePickerDialog(
                            initialDateTime = uiState.collectAsState().value.currentSubjectEntry.start,
                            onDismissRequest = {
                                showStartDateTimeSelectorDialog.value = false
                            },
                            onDateTimeSelected = { dateMillis, hour, minute ->
                                viewModel.updateStartDateTime(dateMillis, hour, minute)
                            },
                            backgroundColor = uiState.collectAsState().value.currentSubjectEntry.color
                        )
                    }

                    Spacer(modifier = Modifier.padding(top = 16.dp))
                    Text(
                        text = stringResource(R.string.subject_date_time_end),
                        style = Typography.bodyMedium,
                        modifier = Modifier.padding(start = 16.dp),
                        color = Color(uiState.collectAsState().value.currentSubjectEntry.color.getContrastingColorForText())
                    )
                    DateTimeSelector(
                        onClick = {
                            showEndDateTimeSelectorDialog.value = true
                        },
                        dateTime = uiState.collectAsState().value.currentSubjectEntry.end.getFormattedDateTime(),
                        selectedColor = uiState.collectAsState().value.currentSubjectEntry.color
                    )

                    if (showEndDateTimeSelectorDialog.value) {
                        DateTimePickerDialog(
                            initialDateTime = uiState.collectAsState().value.currentSubjectEntry.end,
                            onDismissRequest = {
                                showEndDateTimeSelectorDialog.value = false
                            },
                            onDateTimeSelected = { dateMillis, hour, minute ->
                                viewModel.updateEndDateTime(dateMillis, hour, minute)
                            },
                            backgroundColor = uiState.collectAsState().value.currentSubjectEntry.color
                        )
                    }
                }
                Spacer(modifier = Modifier.padding(top = 32.dp))
                ButtonWithBackgroundColor(
                    onClick = {
                        viewModel.saveSubject()
                        onReturn()
                    },
                    selectedColor = uiState.collectAsState().value.currentSubjectEntry.color,
                    placeholderTextPath = R.string.create_button
                )
            }
        }
    }
}

@Composable
private fun DateTimeSelector(
    onClick: () -> Unit,
    dateTime: String,
    selectedColor: Long
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            onClick = onClick
        ) {
            Icon(
                painter = painterResource(R.drawable.calendar_start),
                contentDescription = stringResource(R.string.start_date_time)
            )
        }
        Text(
            text = dateTime,
            style = Typography.labelLarge,
            color = Color(selectedColor.getContrastingColorForText())
        )
    }
}