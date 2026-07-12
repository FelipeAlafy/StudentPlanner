package net.felipealafy.studentplanner.feature_main.views

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import net.felipealafy.studentplanner.feature_class.presentation.viewmodels.DetailedStudentClassViewModel
import net.felipealafy.studentplanner.feature_class.presentation.viewmodels.EditStudentClassViewModel
import net.felipealafy.studentplanner.feature_class.presentation.viewmodels.StudentClassCreationViewModel
import net.felipealafy.studentplanner.feature_class.presentation.views.DetailedClassView
import net.felipealafy.studentplanner.feature_class.presentation.views.EditStudentClassView
import net.felipealafy.studentplanner.feature_class.presentation.views.StudentClassCreationView
import net.felipealafy.studentplanner.feature_exams.presentation.viewmodels.DetailedExamViewModel
import net.felipealafy.studentplanner.feature_exams.presentation.viewmodels.EditExamViewModel
import net.felipealafy.studentplanner.feature_exams.presentation.viewmodels.ExamCreationViewModel
import net.felipealafy.studentplanner.feature_exams.presentation.views.DetailedExamView
import net.felipealafy.studentplanner.feature_exams.presentation.views.EditExamView
import net.felipealafy.studentplanner.feature_exams.presentation.views.ExamCreationView
import net.felipealafy.studentplanner.feature_main.viewmodels.MainViewModel
import net.felipealafy.studentplanner.feature_onboarding.presentation.views.WelcomeView
import net.felipealafy.studentplanner.feature_planner.presentation.viewmodels.DetailedPlannerViewModel
import net.felipealafy.studentplanner.feature_planner.presentation.viewmodels.PlannerCreationViewModel
import net.felipealafy.studentplanner.feature_planner.presentation.views.DetailedPlannerView
import net.felipealafy.studentplanner.feature_planner.presentation.views.PlannerCreationView
import net.felipealafy.studentplanner.feature_subject.presentation.viewmodels.SubjectCreationViewModel
import net.felipealafy.studentplanner.feature_subject.presentation.views.SubjectCreationView
import net.felipealafy.studentplanner.feature_today.presentation.viewmodels.TodayViewModel
import net.felipealafy.studentplanner.feature_today.presentation.views.TodayView
import net.felipealafy.studentplanner.core.ui.theme.StudentPlannerTheme
import net.felipealafy.studentplanner.feature_main.routes.StudentPlannerRoutes
import net.felipealafy.studentplanner.feature_stopwatch.presentation.view.StopWatchView
import net.felipealafy.studentplanner.feature_stopwatch.presentation.viewmodel.StopWatchViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StudentPlannerTheme {
                val navController = rememberNavController()
                val mainViewModel: MainViewModel = hiltViewModel()

                NavHost(
                    navController = navController,
                    startDestination = mainViewModel.startDestination.collectAsState().value
                        ?: StudentPlannerRoutes.WelcomeView.route
                ) {
                    composable(route = StudentPlannerRoutes.WelcomeView.route) {
                        WelcomeView(
                            onStartClick = {
                                navController.navigate(route = StudentPlannerRoutes.SetupView.route)
                            }
                        )
                    }
                    composable(route = StudentPlannerRoutes.SetupView.route) {
                        val viewModel: PlannerCreationViewModel = hiltViewModel()
                        PlannerCreationView(
                            viewModel = viewModel,
                            backOnClick = {
                                navController.popBackStack()
                            },
                            forwardToTodayView = {
                                navController.navigate(route = StudentPlannerRoutes.TodayView.route)
                            }
                        )
                    }
                    composable(route = StudentPlannerRoutes.TodayView.route) {

                        val todayViewModel: TodayViewModel = hiltViewModel()
                        TodayView(
                            viewModel = todayViewModel,
                            onStudentClassClicked = { plannerId, subjectId ->
                                navController.navigate(StudentPlannerRoutes.DetailedClassView.createRoute(plannerId, subjectId))
                            },
                            onExamClicked = { plannerId, subjectId, examId ->
                                navController.navigate(StudentPlannerRoutes.DetailedExamView.createRoute(plannerId, subjectId, examId))
                            },
                            onCreatePlannerClicked = {
                                navController.navigate(StudentPlannerRoutes.SetupView.route)
                            },
                            onCreateSubjectClicked = { plannerId ->
                                navController.navigate(StudentPlannerRoutes.SubjectCreationView.createRoute(plannerId))
                            },
                            onCreateClassClicked = { plannerId ->
                                navController.navigate(StudentPlannerRoutes.StudentClassCreationView.createRoute(plannerId))
                            },
                            onCreateExamClicked = { plannerId ->
                                navController.navigate(StudentPlannerRoutes.ExamCreationView.createRoute(plannerId))
                            },
                            onAccessDetailedPlannerView = { plannerId ->
                                navController.navigate(StudentPlannerRoutes.DetailedPlannerView.createRoute(plannerId))
                            },
                            onStopWatchClicked = { plannerId ->
                                navController.navigate(StudentPlannerRoutes.StopWatchView.createRoute(plannerId))
                            }
                        )
                    }
                    composable(
                        route = StudentPlannerRoutes.DetailedPlannerView.route,
                        arguments = listOf(navArgument("plannerId") { type = NavType.StringType })
                    ) {
                        val viewModel: DetailedPlannerViewModel = hiltViewModel()
                        DetailedPlannerView(
                            viewModel = viewModel,
                            onReturnToPreviousView = { navController.popBackStack() }
                        )
                    }

                    composable(
                        route = StudentPlannerRoutes.StudentClassCreationView.route,
                        arguments = listOf(
                            navArgument("plannerId") { type = NavType.StringType },
                            navArgument("startMillis") {
                                type = NavType.LongType
                                defaultValue = -1L
                            },
                            navArgument("endMillis") {
                                type = NavType.LongType
                                defaultValue = -1L
                            }
                        )
                    ) {
                        val viewModel: StudentClassCreationViewModel = hiltViewModel()
                        StudentClassCreationView(
                            viewModel = viewModel,
                            onReturnAction = { navController.popBackStack() }
                        )
                    }

                    composable(
                        route = StudentPlannerRoutes.SubjectCreationView.route,
                        arguments = listOf(navArgument("plannerId") { type = NavType.StringType })
                    ) {
                        val viewModel: SubjectCreationViewModel = hiltViewModel()
                        SubjectCreationView(viewModel) { navController.popBackStack() }
                    }

                    composable(
                        route = StudentPlannerRoutes.DetailedClassView.route,
                        arguments = listOf(
                            navArgument("subjectId") { type = NavType.StringType },
                            navArgument("studentClassId") { type = NavType.StringType }
                        )
                    ) {
                        val viewModel: DetailedStudentClassViewModel = hiltViewModel()
                        DetailedClassView(
                            viewModel,
                            onEditMode = { plannerId, subjectId, classId ->
                                navController.navigate(StudentPlannerRoutes.EditingClassView.createRoute(plannerId, subjectId, classId))
                            },
                            onReturnAction = { navController.popBackStack() }
                        )
                    }

                    composable(
                        route = StudentPlannerRoutes.ExamCreationView.route,
                        arguments = listOf(
                            navArgument("plannerId") { type = NavType.StringType },
                            navArgument("startMillis") {
                                type = NavType.LongType
                                defaultValue = -1L
                            },
                            navArgument("endMillis") {
                                type = NavType.LongType
                                defaultValue = -1L
                            }
                        )
                    ) {
                        val viewModel: ExamCreationViewModel = hiltViewModel()
                        ExamCreationView(
                            viewModel = viewModel,
                            onReturnAction = { navController.popBackStack() }
                        )
                    }

                    composable(
                        route = StudentPlannerRoutes.DetailedExamView.route,
                        arguments = listOf(
                            navArgument("plannerId") { type = NavType.StringType },
                            navArgument("subjectId") { type = NavType.StringType },
                            navArgument("examId") { type = NavType.StringType }
                        ),
                    ) {
                        val viewModel: DetailedExamViewModel = hiltViewModel()
                        DetailedExamView(
                            viewModel = viewModel,
                            onEditMode = { plannerId, subjectId, examId ->
                                navController.navigate(StudentPlannerRoutes.EditExamView.createRoute(plannerId, subjectId, examId))
                            },
                            onReturnAction = { navController.popBackStack() }
                        )
                    }

                    composable(
                        route = StudentPlannerRoutes.EditingClassView.route,
                        arguments = listOf(
                            navArgument("plannerId") { type = NavType.StringType },
                            navArgument("subjectId") { type = NavType.StringType },
                            navArgument("classId") { type = NavType.StringType }
                        )
                    ) {
                        val viewModel: EditStudentClassViewModel = hiltViewModel()
                        EditStudentClassView(
                            viewModel = viewModel,
                            onReturnAction = {
                                navController.navigate(StudentPlannerRoutes.TodayView.route) {
                                    popUpTo(StudentPlannerRoutes.TodayView.route) {
                                        inclusive = true
                                    }
                                }
                            },
                        )
                    }

                    composable(
                        route = StudentPlannerRoutes.EditExamView.route,
                        arguments = listOf(
                            navArgument("plannerId") { type = NavType.StringType },
                            navArgument("subjectId") { type = NavType.StringType },
                            navArgument("examId") { type = NavType.StringType }
                        )
                    ) {
                        val viewModel: EditExamViewModel = hiltViewModel()
                        EditExamView(
                            viewModel = viewModel,
                            onReturnAction = {
                                navController.navigate(StudentPlannerRoutes.TodayView.route) {
                                    popUpTo(StudentPlannerRoutes.TodayView.route) {
                                        inclusive = true
                                    }
                                }
                            }
                        )
                    }

                    composable (
                        route = StudentPlannerRoutes.StopWatchView.route,
                        arguments = listOf(
                            navArgument("plannerId") { type = NavType.StringType }
                        )
                    ) {
                        val viewModel: StopWatchViewModel = hiltViewModel()
                        StopWatchView(
                            viewModel = viewModel,
                            onReturnAction = {
                                navController.navigate(StudentPlannerRoutes.TodayView.route) {
                                    popUpTo(StudentPlannerRoutes.TodayView.route) {
                                        inclusive = true
                                    }
                                }
                            },
                            onNavigateToCreateClass = { plannerId, startMillis, endMillis ->
                                navController.navigate(
                                    StudentPlannerRoutes.StudentClassCreationView.createRouteWithTime(
                                        plannerId = plannerId,
                                        startMillis = startMillis,
                                        endMillis = endMillis
                                    )
                                )
                            },
                            onNavigateToCreateExam = { plannerId, startMillis, endMillis ->
                                navController.navigate(
                                    StudentPlannerRoutes.ExamCreationView.createRouteWithTime(
                                        plannerId = plannerId,
                                        startMillis = startMillis,
                                        endMillis = endMillis
                                    )
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}