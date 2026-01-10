package net.felipealafy.studentplanner.ui.views

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.felipealafy.studentplanner.R
import net.felipealafy.studentplanner.models.PlannerModel
import net.felipealafy.studentplanner.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlannerCreationView(
    viewModel: PlannerModel,
    backOnClick: () -> Unit = {},
    forwardToTodayView: () -> Unit)
{
    val context = LocalContext.current
    LaunchedEffect(key1 = Unit) {
        viewModel.toastEvent.collectLatest { _ ->
            Toast.makeText(
                context,
                R.string.invalid_number_input,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    //Ui definition
    Surface {
        Scaffold(
            topBar = {

                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(viewModel.getSelectedColor())
                    ),
                    title = {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(R.string.planner_creation_view_title),
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = backOnClick
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = stringResource(R.string.back_to_past_view)
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
                    .background(Color(viewModel.getSelectedColor()).copy(alpha = .5F)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.another_planner_creation),
                    style = Typography.displayLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp),
                )
                Spacer(modifier = Modifier.padding(top = 20.dp))
                TextInputWithColor(
                    text = viewModel.getPlannersName(),
                    selectedColor = viewModel.getSelectedColor(),
                    onValueChange = {
                    viewModel.onNameTextEdit(it)
                    },
                    hint = R.string.planner_name_input
                    )
                Spacer(modifier = Modifier.padding(top = 10.dp))
                MinimumGradeToPassInput(
                    text = viewModel.getMinimumGradeToPassAsString(),
                    onValueChange = {
                        viewModel.onMinimumGradeToPassChanged(it)
                    },
                    selectedColor = viewModel.getSelectedColor()
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, top = 10.dp)
                ) {
                    ButtonOpenColorSelectionDialog(
                        selectedColor = viewModel.getSelectedColor(),
                        onClick = { viewModel.showColorDialog() }
                    )
                    GradeStyleCombobox(selectedColor = viewModel.getSelectedColor(), onSelectItem = {
                        viewModel.onSelectGradeStyle(it)
                    })
                }

                ButtonWithBackgroundColor(
                    onClick = {
                        runBlocking {
                            launch {
                                viewModel.create()
                                forwardToTodayView()
                            }
                        }
                    },
                    selectedColor = viewModel.getSelectedColor(),
                    placeholderTextPath = R.string.create_button
                )
            }

            if (viewModel.colorDialogState.collectAsState().value) {
                ColorSelectionDialog(
                    selectedColor = viewModel.color.collectAsState().value,
                    onDismissRequest = { viewModel.hideColorDialog() },
                    onColorSelected = { color ->
                        viewModel.selectPlannerColor(color = color)
                        viewModel.hideColorDialog()
                    }
                )
            }
        }
    }
}

@Composable
private fun PlannerModel.getPlannersName(): String = this.planner.collectAsState().value.name

@Composable
private fun PlannerModel.getMinimumGradeToPassAsString(): String =
    this.minimumGradeToPass.collectAsState().value

@Composable
private fun PlannerModel.getSelectedColor(): Long =
    this.planner.collectAsState().value.color