package net.felipealafy.studentplanner.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.felipealafy.studentplanner.R
import net.felipealafy.studentplanner.models.PlannerModel
import net.felipealafy.studentplanner.ui.theme.Typography

@Composable
fun SetupView(plannerViewModel: PlannerModel) {
    val uiState = plannerViewModel.planner.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(uiState.value.color).copy(alpha = .3F)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.planner_creation),
            style = Typography.displayLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp),
        )
        Spacer(modifier = Modifier.padding(top = 20.dp))
        TextInputWithColor(
            text = uiState.value.name,
            selectedColor = uiState.value.color,
            onValueChange = {
                plannerViewModel.onNameTextEdit(it)
            },
            hint = R.string.planner_name_input,
        )
        Spacer(modifier = Modifier.padding(top = 10.dp))
        MinimumGradeToPassInput(
            text = uiState.value.minimumGradeToPass.toString(), onValueChange = {
                plannerViewModel.onMinimumGradeToPassChanged(it)
            },
            selectedColor = uiState.value.color
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth().padding(start = 20.dp, top = 10.dp)
        ) {
            ButtonOpenColorSelectionDialog(selectedColor = uiState.value.color, onClick = {
                plannerViewModel.showColorDialog()
            })
            GradeStyleCombobox(selectedColor = uiState.value.color, onSelectItem = {
                plannerViewModel.onSelectGradeStyle(value = it)
            })
        }

        ButtonWithBackgroundColor(
            onClick = {
                runBlocking {
                    launch {
                        plannerViewModel.create()
                    }
                }
            },
            selectedColor = uiState.value.color,
            placeholderTextPath = R.string.planner_creation
        )
    }

    if (plannerViewModel.colorDialogState.value) {
        ColorSelectionDialog(
            selectedColor = uiState.value.color,
            onDismissRequest = { plannerViewModel.hideColorDialog() },
            onColorSelected = { color ->
                plannerViewModel.onColorChange(color)
                plannerViewModel.hideColorDialog()
            }
        )
    }
}