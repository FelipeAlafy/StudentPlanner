package net.felipealafy.studentplanner.feature_onboarding.presentation.views

import TextInputWithColor
import android.widget.Toast
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.felipealafy.studentplanner.R
import net.felipealafy.studentplanner.feature_onboarding.presentation.viewmodels.SetupViewModelFirstPlannerCreation
import net.felipealafy.studentplanner.feature_planner.presentation.viewmodels.PlannerCreationEvent
import net.felipealafy.studentplanner.core.ui.components.color.chooser.ButtonOpenColorSelectionDialog
import net.felipealafy.studentplanner.core.ui.components.color.chooser.ButtonWithBackgroundColor
import net.felipealafy.studentplanner.core.ui.components.color.chooser.ColorSelectionDialog
import net.felipealafy.studentplanner.core.ui.components.grade.GradeStyleComboBox
import net.felipealafy.studentplanner.core.ui.theme.PlannerTheme
import net.felipealafy.studentplanner.core.ui.theme.PlannerThemeProvider
import net.felipealafy.studentplanner.core.ui.theme.Typography

@Composable
fun SetupViewFirstPlannerCreation(viewModel: SetupViewModelFirstPlannerCreation) {
    val uiState = viewModel.uiState.collectAsState().value
    var showColorSelectionDialog by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is PlannerCreationEvent.ShowErrorToast -> {
                    Toast.makeText(context, event.messageResId, Toast.LENGTH_SHORT).show()
                }
                is PlannerCreationEvent.PlannerCreatedSuccessfully -> {
                    Toast.makeText(context, R.string.created_successfully, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    PlannerThemeProvider (baseColor = uiState.color) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(PlannerTheme.colors.surface),
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
                text = uiState.name,
                onValueChange = {
                    viewModel.updateName(it)
                },
                hint = R.string.planner_name_input,
            )
            Spacer(modifier = Modifier.padding(top = 10.dp))
            MinimumGradeToPassInput(
                text = uiState.minimumGradeToPass, onValueChange = {
                    viewModel.updateMinimumGradeToPassChanged(it)
                }
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth().padding(start = 20.dp, top = 10.dp)
            ) {
                ButtonOpenColorSelectionDialog(onClick = {
                    showColorSelectionDialog = true
                })
                GradeStyleComboBox(onSelectItem = {
                    viewModel.updateSelectedGradeStyle(newGradeStyle = it)
                })
            }

            ButtonWithBackgroundColor(
                onClick = {
                    runBlocking {
                        launch {
                            viewModel.create()
                        }
                    }
                },
                placeholderTextPath = R.string.planner_creation
            )
        }

        if (showColorSelectionDialog) {
            ColorSelectionDialog(
                onDismissRequest = { showColorSelectionDialog = false },
                onColorSelected = { color ->
                    viewModel.updateColor(color)
                    showColorSelectionDialog = false
                }
            )
        }
    }
}