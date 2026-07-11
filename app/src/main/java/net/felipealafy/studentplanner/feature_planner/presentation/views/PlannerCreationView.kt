package net.felipealafy.studentplanner.feature_planner.presentation.views

import TextInputWithColor
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.collectLatest
import net.felipealafy.studentplanner.R
import net.felipealafy.studentplanner.core.ui.components.color.chooser.ButtonOpenColorSelectionDialog
import net.felipealafy.studentplanner.core.ui.components.color.chooser.ButtonWithBackgroundColor
import net.felipealafy.studentplanner.core.ui.components.color.chooser.ColorSelectionDialog
import net.felipealafy.studentplanner.core.ui.components.grade.GradeStyleComboBox
import net.felipealafy.studentplanner.core.ui.components.grade.MinimumGradeToPassInput
import net.felipealafy.studentplanner.core.ui.components.text.label.TopAppBarTitle
import net.felipealafy.studentplanner.core.ui.theme.PlannerTheme
import net.felipealafy.studentplanner.core.ui.theme.PlannerThemeProvider
import net.felipealafy.studentplanner.core.ui.theme.Typography
import net.felipealafy.studentplanner.feature_planner.presentation.viewmodels.PlannerCreationEvent
import net.felipealafy.studentplanner.feature_planner.presentation.viewmodels.PlannerCreationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlannerCreationView(
    viewModel: PlannerCreationViewModel,
    backOnClick: () -> Unit = {},
    forwardToTodayView: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        viewModel.events.collectLatest { event ->

            when (event) {
                is PlannerCreationEvent.ShowErrorToast -> {
                    Toast.makeText(
                        context,
                        event.messageResId,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is PlannerCreationEvent.PlannerCreatedSuccessfully -> {
                    forwardToTodayView()
                }
            }
        }
    }

    PlannerThemeProvider(
        baseColor = uiState.color
    ) {
        Surface {
            Scaffold(
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
                                    text = stringResource(R.string.planner_creation_view_title),
                                )
                            }
                        },
                        navigationIcon = {
                            IconButton(
                                onClick = backOnClick
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.back_arrow),
                                    contentDescription = stringResource(R.string.back_to_past_view),
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
                        .padding(innerPadding)
                        .background(PlannerTheme.colors.background),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.another_planner_creation),
                        style = Typography.displayLarge,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp),
                        color = PlannerTheme.colors.onSurface
                    )
                    Spacer(modifier = Modifier.padding(top = 20.dp))
                    TextInputWithColor(
                        text = uiState.name,
                        onValueChange = {
                            viewModel.onNameTextEdit(it)
                        },
                        hint = R.string.planner_name_input
                    )
                    Spacer(modifier = Modifier.padding(top = 10.dp))
                    MinimumGradeToPassInput(
                        text = uiState.minimumGradeToPass,
                        onValueChange = {
                            viewModel.onMinimumGradeToPassChanged(it)
                        }
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, top = 10.dp)
                    ) {
                        ButtonOpenColorSelectionDialog(
                            onClick = { viewModel.showColorDialog() }
                        )
                        GradeStyleComboBox(
                            onSelectItem = {
                                viewModel.onSelectGradeStyle(it)
                            })
                    }

                    ButtonWithBackgroundColor(
                        onClick = {
                            viewModel.create()
                        },
                        placeholderTextPath = R.string.create_button
                    )
                }

                if (uiState.isColorDialogVisible) {
                    ColorSelectionDialog(
                        onDismissRequest = { viewModel.hideColorDialog() },
                        onColorSelected = { color ->
                            viewModel.onColorChange(color)
                            viewModel.hideColorDialog()
                        }
                    )
                }
            }
        }
    }
}