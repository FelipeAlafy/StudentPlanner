package net.felipealafy.studentplanner.ui.views

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import net.felipealafy.studentplanner.R
import net.felipealafy.studentplanner.datamodels.StudentClass
import net.felipealafy.studentplanner.models.TodayModel
import net.felipealafy.studentplanner.models.TodayUiState
import net.felipealafy.studentplanner.ui.theme.DarkGray
import net.felipealafy.studentplanner.ui.theme.Typography
import net.felipealafy.studentplanner.ui.theme.colorPallet
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@SuppressLint("LocalContextResourcesRead")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodayView(
    viewModel: TodayModel,
    onPlannerClick: (String) -> Unit,
    onCreatePlannerClicked: () -> Unit,
    onCreateSubjectClicked: (plannerId: String) -> Unit,
    onCreateClassClicked: (plannerId: String) -> Unit,
    onCreateExamClicked: () -> Unit
) {

    val uiState = viewModel.uiState.collectAsState()
    val planner = uiState.value.selectedPlanner

    if (planner == null) {
        Box(
            modifier = Modifier.fillMaxSize().background(Color.White), // Cor de fundo neutra
            contentAlignment = Alignment.Center
        ) {
            if (uiState.value.isLoading) {
                CircularProgressIndicator()
            } else {
                Text(text = "No planners available.")
            }
        }
        return
    }


    val state = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        modifier = Modifier.background(color = Color(colorPallet[0][0]).copy(0.1F)),
        drawerContent = {
            val plannerColor = Color(planner.color)
            ModalDrawerSheet(
                drawerContainerColor = DrawerDefaults.modalContainerColor.copy(
                    alpha = .75F,
                    red = plannerColor.red,
                    green = plannerColor.green,
                    blue = plannerColor.blue
                ),
            ) {
                Column (
                    modifier = Modifier.padding(16.dp).verticalScroll(rememberScrollState())
                ) {
                    Text(stringResource(R.string.your_subjects), style = Typography.bodyLarge)
                    HorizontalDivider(modifier = Modifier.padding(bottom = 5.dp))
                    NavigationDrawerItem(
                        label = { Text(text = "Default Planner", style = Typography.labelMedium) },
                        selected = true,
                        onClick = {}
                    )
                }
            }
        },
        gesturesEnabled = true, drawerState = state
    ) {
        Scaffold(
            topBar = {
                val today =
                    DateTimeFormatter.ofPattern("EEEE").format(LocalDateTime.now())
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(planner.color.toInt())
                    ),
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                state.apply {
                                    if (isClosed) open() else close()
                                }
                            }
                        }) {
                            Icon(
                                Icons.Default.Menu,
                                contentDescription = stringResource(R.string.planners_menu)
                            )
                        }
                    },
                    title = {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(today)
                        }
                    },
                    actions = {
                        Row {

                            IconButton(onClick = {}) {
                                Icon(
                                    Icons.Default.DateRange,
                                    contentDescription = stringResource(R.string.calendars_view)
                                )
                            }
                        }
                    }
                )
            },
            floatingActionButton = {
                var expanded by remember { mutableStateOf(false) }
                val context = LocalContext.current
                val screenWidth = context.resources.displayMetrics.widthPixels.dp


                Box {
                    if (screenWidth < 500.dp) {
                        FloatingActionButton(
                            onClick = { expanded = !expanded },
                            containerColor = Color(planner.color.toInt()).copy(alpha = 0.8F)
                        ) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = stringResource(R.string.add_floating_action_button)
                            )
                        }
                    } else {
                        ExtendedFloatingActionButton(
                            onClick = { expanded = !expanded },
                            containerColor = Color(planner.color.toInt()).copy(alpha = 0.8F)
                        ) {
                            Row {
                                Icon(
                                    Icons.Default.Add,
                                    contentDescription = stringResource(R.string.add_floating_action_button)
                                )
                                Text(text = stringResource(R.string.add_expanded_floating_action_button))
                            }
                        }
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        containerColor = Color(planner.color.toInt()).copy(alpha = 0.8F),
                        shape = RoundedCornerShape(30.dp)
                    ) {
                        DropdownMenuItem(
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(R.drawable.planner),
                                    contentDescription = stringResource(R.string.new_planner)
                                )
                            },
                            text = { Text(stringResource(R.string.new_planner)) },
                            onClick = {
                                onCreatePlannerClicked()
                                expanded = false
                            }
                        )
                        DropdownMenuItem(
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(R.drawable.subject),
                                    contentDescription = stringResource(R.string.new_subject)
                                )
                            },
                            text = { Text(stringResource(R.string.new_subject)) },
                            onClick = {
                                onCreateSubjectClicked(planner.id)
                                expanded = false
                            }
                        )
                        DropdownMenuItem(
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(R.drawable.resource_class),
                                    contentDescription = stringResource(R.string.new_resource_class)
                                )
                            },
                            text = { Text(stringResource(R.string.new_resource_class)) },
                            onClick = {
                                Log.i("TodayView", "id: ${planner.id} name: ${planner.name}")
                                onCreateClassClicked(planner.id)
                                expanded = false
                            }
                        )
                        DropdownMenuItem(
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(R.drawable.grade),
                                    contentDescription = stringResource(R.string.grade)
                                )
                            },
                            text = { Text(stringResource(R.string.insert_grade)) },
                            onClick = {
                                onCreateExamClicked()
                                expanded = false
                            }
                        )
                    }
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .background(color = Color(planner.color).copy(0.1F))
            ) {
                CalendarView(
                    selectedColor = planner.color,
                    subjectColor = colorPallet[3][2],
                    uiState = uiState.value,
                    onStudentClassClicked = onPlannerClick
                )
            }
        }
    }
}

@Composable
fun CalendarView(
    selectedColor: Long, subjectColor: Long,
    uiState: TodayUiState,
    onStudentClassClicked: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(selectedColor).copy(0.1F))
    ) {
        items(items = uiState.classesToday) {
            ClassCard(
                studentClass = it,
                subjectColor = subjectColor,
                onStudentClassClicked = onStudentClassClicked
            )
            Spacer(modifier = Modifier.padding(5.dp))
        }
    }
}

@Composable
fun ClassCard(studentClass: StudentClass, subjectColor: Long, onStudentClassClicked: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .height(80.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(subjectColor).copy(0.8F),
        ),
        shape = RoundedCornerShape(15.dp),
        onClick = {onStudentClassClicked(studentClass.id)}
    ) {
        Text(
            text = studentClass.title,
            color = DarkGray,
            style = Typography.labelMedium,
            modifier = Modifier.padding(start = 12.dp)
        )
    }
}