package net.felipealafy.studentplanner.feature_today.presentation.views

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
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
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import net.felipealafy.studentplanner.R
import net.felipealafy.studentplanner.core.ui.components.text.label.TopAppBarTitle
import net.felipealafy.studentplanner.core.ui.date.time.picker.DatePickerDialog
import net.felipealafy.studentplanner.core.ui.theme.PlannerTheme
import net.felipealafy.studentplanner.core.ui.theme.PlannerThemeProvider
import net.felipealafy.studentplanner.core.ui.theme.Transparent
import net.felipealafy.studentplanner.core.ui.theme.Typography
import net.felipealafy.studentplanner.core.ui.theme.colorutils.getContrastingColorForText
import net.felipealafy.studentplanner.core.ui.extensions.getValueInDisplayStyle
import net.felipealafy.studentplanner.feature_class.domain.model.StudentClass
import net.felipealafy.studentplanner.feature_exams.data.local.Exam
import net.felipealafy.studentplanner.feature_exams.domain.use_case.GradeStyle
import net.felipealafy.studentplanner.feature_planner.domain.model.DetailedPlanner
import net.felipealafy.studentplanner.feature_subject.domain.model.Subject
import net.felipealafy.studentplanner.feature_today.presentation.viewmodels.TodayUiState
import net.felipealafy.studentplanner.feature_today.presentation.viewmodels.TodayViewModel
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter


@SuppressLint("LocalContextResourcesRead")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodayView(
    viewModel: TodayViewModel,
    onStudentClassClicked: (String, String) -> Unit,
    onExamClicked: (String, String, String) -> Unit,
    onCreatePlannerClicked: () -> Unit,
    onCreateSubjectClicked: (plannerId: String) -> Unit,
    onCreateClassClicked: (plannerId: String) -> Unit,
    onCreateExamClicked: (plannerId: String) -> Unit,
    onAccessDetailedPlannerView: (plannerId: String) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    when (val state = uiState) {
        is TodayUiState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is TodayUiState.Empty -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(R.string.no_planners_available),
                        style = Typography.titleMedium
                    )
                    Spacer(modifier = Modifier.padding(8.dp))
                    FloatingActionButton(onClick = onCreatePlannerClicked) {
                        Icon(painter = painterResource(R.drawable.add_icon), contentDescription = "Create Planner")
                    }
                }
            }
        }

        is TodayUiState.Success -> {

            val density = LocalDensity.current
            val screenWidth =
                with(density) { LocalWindowInfo.current.containerSize.width.toDp() }
            val isExpanded = screenWidth > 600.dp

            if (isExpanded) {
                LargeScreenContent (
                    state,
                    viewModel,
                    onAccessDetailedPlannerView,
                    onCreatePlannerClicked,
                    onCreateSubjectClicked,
                    onCreateClassClicked,
                    onCreateExamClicked,
                    onStudentClassClicked,
                    onExamClicked
                )
            } else {
                SmallScreenContent(
                    state,
                    viewModel,
                    onAccessDetailedPlannerView,
                    onCreatePlannerClicked,
                    onCreateSubjectClicked,
                    onCreateClassClicked,
                    onCreateExamClicked,
                    onStudentClassClicked,
                    onExamClicked
                )
            }

        }
    }
}

@Composable
private fun SmallScreenContent(
    state: TodayUiState.Success,
    viewModel: TodayViewModel,
    onAccessDetailedPlannerView: (String) -> Unit,
    onCreatePlannerClicked: () -> Unit,
    onCreateSubjectClicked: (String) -> Unit,
    onCreateClassClicked: (String) -> Unit,
    onCreateExamClicked: (String) -> Unit,
    onStudentClassClicked: (String, String) -> Unit,
    onExamClicked: (String, String, String) -> Unit
) {
    PlannerThemeProvider(state.selectedPlanner.planner.color) {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        val showDateSelection = remember { mutableStateOf(false) }
        ModalNavigationDrawer(
            drawerContent = {
                ModalDrawerSheet(
                    drawerContainerColor = PlannerTheme.colors.surface
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState())
                    ) {

                        val planners = state.allPlanners

                        Text(
                            stringResource(R.string.your_planners),
                            style = Typography.bodyLarge
                        )
                        HorizontalDivider(modifier = Modifier.padding(bottom = 5.dp))

                        planners.forEach {
                            Row {
                                NavigationDrawerItem(
                                    label = {
                                        Text(
                                            text = it.name,
                                            style = Typography.labelMedium
                                        )
                                    },
                                    selected = state.selectedPlanner.planner.id == it.id,
                                    onClick = {
                                        viewModel.selectPlanner(it.id)
                                    },
                                    colors = NavigationDrawerItemDefaults.colors(
                                        selectedTextColor = PlannerTheme.colors.onPrimary,
                                        selectedContainerColor = PlannerTheme.colors.primary,
                                        unselectedContainerColor = PlannerTheme.colors.container,
                                        unselectedTextColor = PlannerTheme.colors.onContainer,
                                        selectedBadgeColor = PlannerTheme.colors.onPrimary,
                                        unselectedBadgeColor = PlannerTheme.colors.onContainer

                                    ),
                                    badge = {
                                        IconButton(
                                            onClick = {
                                                onAccessDetailedPlannerView(it.id)
                                            }
                                        ) {
                                            Icon(
                                                painter = painterResource(R.drawable.drill_down),
                                                contentDescription = stringResource(R.string.on_access_detailed_planner_view),
                                                tint = Color(it.color.getContrastingColorForText())
                                            )
                                        }
                                    }
                                )
                            }
                            Spacer(modifier = Modifier.padding(bottom = 8.dp))
                        }
                    }
                }
            },
            gesturesEnabled = true,
            drawerState = drawerState,
        ) {
            TodayViewContents(
                state,
                scope,
                drawerState,
                isSmallScreen = true,
                showDateSelection,
                onCreatePlannerClicked,
                onCreateSubjectClicked,
                onCreateClassClicked,
                onCreateExamClicked,
                viewModel,
                onStudentClassClicked,
                onExamClicked
            )
        }
    }
}

@Composable
private fun LargeScreenContent(
    state: TodayUiState.Success,
    viewModel: TodayViewModel,
    onAccessDetailedPlannerView: (String) -> Unit,
    onCreatePlannerClicked: () -> Unit,
    onCreateSubjectClicked: (String) -> Unit,
    onCreateClassClicked: (String) -> Unit,
    onCreateExamClicked: (String) -> Unit,
    onStudentClassClicked: (String, String) -> Unit,
    onExamClicked: (String, String, String) -> Unit
) {
    PlannerThemeProvider(state.selectedPlanner.planner.color) {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        val showDateSelection = remember { mutableStateOf(false) }
        PermanentNavigationDrawer (
            drawerContent = {
                PermanentDrawerSheet(
                    drawerContainerColor = PlannerTheme.colors.surface
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState())
                    ) {

                        val planners = state.allPlanners

                        Text(
                            stringResource(R.string.your_planners),
                            style = Typography.bodyLarge
                        )
                        HorizontalDivider(modifier = Modifier.padding(bottom = 5.dp))

                        planners.forEach {
                            Row {
                                NavigationDrawerItem(
                                    label = {
                                        Text(
                                            text = it.name,
                                            style = Typography.labelMedium
                                        )
                                    },
                                    selected = state.selectedPlanner.planner.id == it.id,
                                    onClick = {
                                        viewModel.selectPlanner(it.id)
                                    },
                                    colors = NavigationDrawerItemDefaults.colors(
                                        selectedTextColor = PlannerTheme.colors.onPrimary,
                                        selectedContainerColor = PlannerTheme.colors.primary,
                                        unselectedContainerColor = PlannerTheme.colors.container,
                                        unselectedTextColor = PlannerTheme.colors.onContainer,
                                        selectedBadgeColor = PlannerTheme.colors.onPrimary,
                                        unselectedBadgeColor = PlannerTheme.colors.onContainer

                                    ),
                                    badge = {
                                        IconButton(
                                            onClick = {
                                                onAccessDetailedPlannerView(it.id)
                                            }
                                        ) {
                                            Icon(
                                                painter = painterResource(R.drawable.drill_down),
                                                contentDescription = stringResource(R.string.on_access_detailed_planner_view),
                                                tint = Color(it.color.getContrastingColorForText())
                                            )
                                        }
                                    }
                                )
                            }
                            Spacer(modifier = Modifier.padding(bottom = 8.dp))
                        }
                    }
                }
            }
        ) {
            TodayViewContents(
                state,
                scope,
                drawerState,
                isSmallScreen = false,
                showDateSelection,
                onCreatePlannerClicked,
                onCreateSubjectClicked,
                onCreateClassClicked,
                onCreateExamClicked,
                viewModel,
                onStudentClassClicked,
                onExamClicked
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TodayViewContents(
    state: TodayUiState.Success,
    scope: CoroutineScope,
    drawerState: DrawerState,
    isSmallScreen: Boolean,
    showDateSelection: MutableState<Boolean>,
    onCreatePlannerClicked: () -> Unit,
    onCreateSubjectClicked: (String) -> Unit,
    onCreateClassClicked: (String) -> Unit,
    onCreateExamClicked: (String) -> Unit,
    viewModel: TodayViewModel,
    onStudentClassClicked: (String, String) -> Unit,
    onExamClicked: (String, String, String) -> Unit
) {
    Scaffold(
        containerColor = PlannerTheme.colors.surface,
        topBar = {
            val today =
                DateTimeFormatter.ofPattern("EEEE, dd/MM").format(state.currentDate)
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PlannerTheme.colors.surface
                ),
                navigationIcon = {
                    if (!isSmallScreen) return@TopAppBar
                    IconButton(onClick = {
                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    }) {
                        Icon(
                            painter = painterResource(
                                R.drawable.left_panel_open
                            ),
                            contentDescription = stringResource(R.string.planners_menu),
                            tint = PlannerTheme.colors.onPrimary

                        )
                    }
                },
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        TopAppBarTitle(
                            text = today
                        )
                    }
                },
                actions = {
                    Row {
                        IconButton(onClick = {
                            showDateSelection.value = true
                        }) {
                            Icon(
                                painter = painterResource(R.drawable.calendar),
                                contentDescription = stringResource(R.string.calendars_view),
                                tint = PlannerTheme.colors.onPrimary
                            )
                        }
                    }
                },
            )
        },
        floatingActionButton = {
            var expanded by remember { mutableStateOf(false) }

            val rotationAngle by animateFloatAsState(
                targetValue = if (expanded) 45f else 0f,
                label = "fab_add_rotation"
            )

            Box {
                if (isSmallScreen) {
                    FloatingActionButton(
                        onClick = { expanded = !expanded },
                        containerColor = PlannerTheme.colors.primary,
                    ) {
                        Icon(
                            painterResource(R.drawable.add_icon),
                            contentDescription = stringResource(R.string.add_floating_action_button),
                            tint = PlannerTheme.colors.onPrimary,
                            modifier = Modifier.rotate(rotationAngle)
                        )
                    }
                } else {
                    ExtendedFloatingActionButton(
                        onClick = { expanded = !expanded },
                        containerColor = PlannerTheme.colors.primary,
                        modifier = Modifier.border(
                            width = 0.dp,
                            color = Transparent,
                            shape = RoundedCornerShape(8.dp)
                        )
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painterResource(R.drawable.add_icon),
                                contentDescription = stringResource(R.string.add_floating_action_button),
                                tint = PlannerTheme.colors.onPrimary,
                                modifier = Modifier.rotate(rotationAngle)
                            )
                            Spacer(modifier = Modifier.padding(end = 8.dp))
                            Text(
                                text = stringResource(R.string.add_expanded_floating_action_button),
                                color = PlannerTheme.colors.onPrimary
                            )
                        }
                    }
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    containerColor = Transparent,
                    tonalElevation = 0.dp,
                    shadowElevation = 0.dp
                ) {
                    DropdownMenuItem(
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.planner),
                                contentDescription = stringResource(R.string.new_planner),
                                tint = PlannerTheme.colors.onPrimary
                            )
                        },
                        text = {
                            Text(
                                stringResource(R.string.new_planner),
                                color = PlannerTheme.colors.onPrimary
                            )
                        },
                        onClick = {
                            onCreatePlannerClicked()
                            expanded = false
                        }, modifier = Modifier.background(
                            color = PlannerTheme.colors.primary,
                            shape = RoundedCornerShape(30.dp)
                        )
                    )
                    Spacer(Modifier.padding(top = 8.dp))
                    DropdownMenuItem(
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.subject),
                                contentDescription = stringResource(R.string.new_subject),
                                tint = PlannerTheme.colors.onPrimary
                            )
                        },
                        text = {
                            Text(
                                stringResource(R.string.new_subject),
                                color = PlannerTheme.colors.onPrimary
                            )
                        },
                        onClick = {
                            onCreateSubjectClicked(state.selectedPlanner.planner.id)
                            expanded = false
                        }, modifier = Modifier.background(
                            color = PlannerTheme.colors.primary,
                            shape = RoundedCornerShape(30.dp)
                        )
                    )
                    Spacer(Modifier.padding(top = 8.dp))
                    DropdownMenuItem(
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.resource_class),
                                contentDescription = stringResource(R.string.new_resource_class),
                                tint = PlannerTheme.colors.onPrimary
                            )
                        },
                        text = {
                            Text(
                                stringResource(R.string.new_resource_class),
                                color = PlannerTheme.colors.onPrimary
                            )
                        },
                        onClick = {
                            onCreateClassClicked(state.selectedPlanner.planner.id)
                            expanded = false
                        },
                        modifier = Modifier.background(
                            color = PlannerTheme.colors.primary,
                            shape = RoundedCornerShape(30.dp)
                        )
                    )
                    Spacer(Modifier.padding(top = 8.dp))
                    DropdownMenuItem(
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.grade),
                                contentDescription = stringResource(R.string.grade),
                                tint = PlannerTheme.colors.onPrimary
                            )
                        },
                        text = {
                            Text(
                                stringResource(R.string.insert_grade),
                                color = PlannerTheme.colors.onPrimary
                            )
                        },
                        onClick = {
                            onCreateExamClicked(state.selectedPlanner.planner.id)
                            expanded = false
                        }, modifier = Modifier.background(
                            color = PlannerTheme.colors.primary,
                            shape = RoundedCornerShape(30.dp)
                        )
                    )
                }
            }
        }) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .clip(RoundedCornerShape(topStart = if (isSmallScreen) 0.dp else 24.dp))
                .background(Color.White)
        ) {
            if (showDateSelection.value) {
                DatePickerDialog(
                    initialDateTime = LocalDateTime.of(
                        state.currentDate,
                        LocalTime.now()
                    ),
                    onDismissRequest = {
                        showDateSelection.value = false
                    },
                    onDateSelected = { dateMillis ->
                        viewModel.updateTodaySelection(dateMillis)
                    },
                )
            }

            CalendarView(
                selectedPlanner = state.selectedPlanner,
                gradeDisplayStyle = state.selectedPlanner.planner.gradeDisplayStyle,
                onStudentClassClicked = onStudentClassClicked,
                onExamClicked = onExamClicked
            )
        }
    }
}

@Composable
fun CalendarView(
    selectedPlanner: DetailedPlanner,
    gradeDisplayStyle: GradeStyle,
    onStudentClassClicked: (String, String) -> Unit,
    onExamClicked: (String, String, String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(topStart = 24.dp))
    ) {

        selectedPlanner.subjects.forEach { detailedSubject ->
            items(items = detailedSubject.studentClasses) {
                ClassCard(
                    subject = detailedSubject.subject,
                    studentClass = it,
                    subjectColor = detailedSubject.subject.color,
                    onStudentClassClicked = onStudentClassClicked
                )
                Spacer(modifier = Modifier.padding(5.dp))
            }
            items(items = detailedSubject.exams) {
                ExamCard(
                    exam = it,
                    subject = detailedSubject.subject,
                    gradeStyle = gradeDisplayStyle,
                    onExamClicked = onExamClicked
                )
            }
        }
    }
}

@Composable
fun ClassCard(
    subject: Subject,
    studentClass: StudentClass,
    subjectColor: Long,
    onStudentClassClicked: (String, String) -> Unit
) {
    PlannerThemeProvider(subjectColor) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = PlannerTheme.colors.primary,
            ),
            shape = RoundedCornerShape(15.dp),
            onClick = {
                onStudentClassClicked(studentClass.subjectId, studentClass.id)
            }) {
            Column(
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().background(PlannerTheme.colors.cardTop)
                ) {
                    Icon(
                        painterResource(R.drawable.resource_class),
                        contentDescription = stringResource(R.string.class_icon),
                        modifier = Modifier.padding(start = 12.dp),
                        tint = PlannerTheme.colors.onCardTop

                    )
                    Text(
                        text = studentClass.title,
                        color = PlannerTheme.colors.onCardTop,
                        style = Typography.labelLarge,
                        modifier = Modifier.padding(start = 12.dp, top = 8.dp)
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(PlannerTheme.colors.primary)
                        .padding(start = 12.dp, top = 8.dp)
                ) {
                    Icon(
                        painterResource(R.drawable.calendar_start),
                        contentDescription = stringResource(R.string.date_icon),
                        tint = PlannerTheme.colors.onCardTop
                    )
                    Text(
                        text = getFormattedDateTimeForClassCard(studentClass),
                        color = PlannerTheme.colors.onCardTop,
                        style = Typography.labelMedium
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(PlannerTheme.colors.primary)
                        .padding(start = 12.dp, top = 8.dp)
                ) {
                    Icon(
                        painterResource(R.drawable.subject),
                        contentDescription = stringResource(R.string.subject_icon),
                        tint = PlannerTheme.colors.onCardTop
                    )
                    Text(
                        text = subject.name,
                        color = PlannerTheme.colors.onCardTop,
                        style = Typography.labelMedium
                    )
                }
            }
        }
    }
}

@Composable
fun ExamCard(
    exam: Exam,
    subject: Subject,
    gradeStyle: GradeStyle,
    onExamClicked: (String, String, String) -> Unit
) {
    PlannerThemeProvider(subject.color) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = PlannerTheme.colors.primary,
            ),
            shape = RoundedCornerShape(15.dp),
            onClick = { onExamClicked(subject.plannerId, exam.subjectId, exam.id) }) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().background(PlannerTheme.colors.cardTop)
                ) {
                    Icon(
                        painterResource(R.drawable.grade),
                        contentDescription = stringResource(R.string.grade),
                        tint = PlannerTheme.colors.onCardTop,
                        modifier = Modifier.padding(start = 12.dp)
                    )
                    Text(
                        text = exam.name,
                        color = PlannerTheme.colors.onCardTop,
                        style = Typography.labelLarge,
                        modifier = Modifier.padding(start = 12.dp, top = 8.dp)
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(PlannerTheme.colors.primary)
                        .padding(start = 12.dp, top = 8.dp)
                ) {
                    Icon(
                        painterResource(R.drawable.calendar_start),
                        contentDescription = stringResource(R.string.date_icon),
                        tint = PlannerTheme.colors.onPrimary
                    )
                    Text(
                        text = getFormattedDateTimeForExamCard(exam),
                        color = PlannerTheme.colors.onPrimary,
                        style = Typography.labelMedium
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(PlannerTheme.colors.primary)
                        .padding(start = 12.dp, top = 8.dp)
                ) {
                    Icon(
                        painterResource(R.drawable.outline_bar_chart),
                        contentDescription = stringResource(R.string.chart),
                        tint = PlannerTheme.colors.onPrimary
                    )
                    Text(
                        text = "${stringResource(R.string.grade_took)} → ${
                            gradeStyle.getValueInDisplayStyle(
                                exam.grade
                            )
                        }",
                        color = PlannerTheme.colors.onPrimary,
                        style = Typography.labelMedium
                    )
                }
            }
        }
    }
}

@Composable
fun getFormattedDateTimeForClassCard(studentClass: StudentClass): String {
    return " ${stringResource(R.string.class_period)} " + "${
        studentClass.start.format(
            DateTimeFormatter.ofPattern("dd MMM hh:mm")
        )
    } → ${
        studentClass.end.format(
            DateTimeFormatter.ofPattern("dd MMM hh:mm")
        )
    }"
}

@Composable
fun getFormattedDateTimeForExamCard(exam: Exam): String {
    return " ${stringResource(R.string.when_date)} " + "${
        exam.start.format(
            DateTimeFormatter.ofPattern("dd MMM hh:mm")
        )
    } → ${
        exam.end.format(
            DateTimeFormatter.ofPattern("dd MMM hh:mm")
        )
    }"
}