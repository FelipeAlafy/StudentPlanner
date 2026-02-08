package net.felipealafy.studentplanner.ui.date.time.picker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
    initialDateTime: LocalDateTime = LocalDateTime.now(),
    onDismissRequest: () -> Unit,
    onDateSelected: (Long?) -> Unit,
    backgroundColor: Long
) {
    val dateState = rememberDatePickerState(
        initialSelectedDateMillis = initialDateTime.toInstant(ZoneOffset.UTC).toEpochMilli()
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
                    onDateSelected(
                        dateState.selectedDateMillis,
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
            DatePicker(
                state = dateState,
                showModeToggle = false,
                colors = DatePickerDefaults.colors(
                    selectedDayContainerColor = Color(backgroundColor.getContrastingButtonColor()),
                    containerColor = Color(backgroundColor)
                )
            )
        }
    }
}


@Preview
@Composable
private fun DatePickerPreview() {
    Surface(modifier = Modifier.fillMaxSize()) {
        DatePickerDialog(
            onDismissRequest = {},
            onDateSelected = { _ ->

            },
            backgroundColor = colorPallet[0][1]
        )
    }
}