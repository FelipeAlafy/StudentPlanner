package net.felipealafy.studentplanner.ui.date.time.picker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.felipealafy.studentplanner.R
import net.felipealafy.studentplanner.ui.theme.Green
import net.felipealafy.studentplanner.ui.theme.Red
import net.felipealafy.studentplanner.ui.theme.colorPallet
import net.felipealafy.studentplanner.ui.views.getContrastingButtonColor
import java.time.LocalDateTime
import java.time.ZoneOffset

private enum class DateTimePickerState {
    DATE, TIME
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTimePickerDialog(
    initialDateTime: LocalDateTime = LocalDateTime.now(),
    onDismissRequest: () -> Unit,
    onDateTimeSelected: (Long?, Int, Int) -> Unit,
    backgroundColor: Long
) {
    var currentSelectedMode by rememberSaveable { mutableStateOf(DateTimePickerState.DATE) }
    val dateState = rememberDatePickerState(
        initialSelectedDateMillis = initialDateTime.toInstant(ZoneOffset.UTC).toEpochMilli()
    )
    val timeState = rememberTimePickerState(
        initialHour = initialDateTime.hour,
        initialMinute = initialDateTime.minute,
        is24Hour = true
    )


    DatePickerDialog(
        modifier = Modifier.heightIn(min = 500.dp),
        colors = DatePickerDefaults.colors(
            containerColor = Color(backgroundColor)
        ),
        onDismissRequest = onDismissRequest,
        confirmButton = {
            IconButton(
                onClick = {
                    onDateTimeSelected(
                        dateState.selectedDateMillis,
                        timeState.hour,
                        timeState.minute
                    )
                    onDismissRequest()
                }
            ) {
                Icon(
                    painter = painterResource(R.drawable.check_icon),
                    contentDescription = stringResource(R.string.confirm),
                    tint = Green
                )
            }
        },
        dismissButton = {
            IconButton(
                onClick = { onDismissRequest() }
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_close),
                    contentDescription = stringResource(R.string.close),
                    tint = Red
                )
            }
        }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.background(Color(backgroundColor))
        ) {
            SingleChoiceSegmentedButtonRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 16.dp,
                        top = 0.dp,
                        end = 16.dp,
                        bottom = 8.dp
                    ),
                space = (-5).dp
            ) {
                SegmentedButton(
                    selected = currentSelectedMode == DateTimePickerState.DATE,
                    onClick = {
                        currentSelectedMode = DateTimePickerState.DATE
                    },
                    shape = RoundedCornerShape(20.dp),
                    colors = SegmentedButtonDefaults.colors(
                        activeContainerColor = Color(backgroundColor.getContrastingButtonColor()),
                        activeContentColor = Color(backgroundColor),
                        inactiveContentColor = Color(backgroundColor)
                    )
                ) {
                    Icon(
                        painter = painterResource(R.drawable.calendar_icon),
                        contentDescription = stringResource(R.string.date_mode),
                    )
                }
                SegmentedButton(
                    selected = currentSelectedMode == DateTimePickerState.TIME,
                    onClick = {
                        currentSelectedMode = DateTimePickerState.TIME
                    },
                    shape = RoundedCornerShape(20.dp),
                    colors = SegmentedButtonDefaults.colors(
                        activeContainerColor = Color(backgroundColor.getContrastingButtonColor()),
                        activeContentColor = Color(backgroundColor),
                        inactiveContentColor = Color(backgroundColor)
                    )
                ) {
                    Icon(
                        painter = painterResource(R.drawable.clock_icon),
                        contentDescription = stringResource(R.string.time_mode),
                        tint = Color(backgroundColor)
                    )
                }
            }

            when (currentSelectedMode) {
                DateTimePickerState.DATE -> {
                    DatePicker(
                        state = dateState,
                        showModeToggle = false,
                        colors = DatePickerDefaults.colors(
                            selectedDayContainerColor = Color(backgroundColor.getContrastingButtonColor()),
                            containerColor = Color(backgroundColor)
                        )
                    )
                }

                DateTimePickerState.TIME -> {
                    TimePicker(
                        state = timeState,
                        colors = TimePickerDefaults.colors(
                            selectorColor = Color(backgroundColor.getContrastingButtonColor()),
                            timeSelectorSelectedContainerColor = Color(backgroundColor.getContrastingButtonColor()),
                            timeSelectorSelectedContentColor = Color(backgroundColor),
                            timeSelectorUnselectedContentColor = Color(backgroundColor.getContrastingButtonColor())
                        )
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun DateTimePickerPreview() {
    Surface(modifier = Modifier.fillMaxSize()) {
        DateTimePickerDialog(
            onDismissRequest = {},
            onDateTimeSelected = { _, _, _ ->

            },
            backgroundColor = colorPallet[0][1]
        )
    }
}