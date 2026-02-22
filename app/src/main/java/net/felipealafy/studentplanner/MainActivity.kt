package net.felipealafy.studentplanner

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
import net.felipealafy.studentplanner.models.DetailedExamViewModel
import net.felipealafy.studentplanner.models.DetailedPlannerViewModel
import net.felipealafy.studentplanner.models.DetailedStudentClassViewModel
import net.felipealafy.studentplanner.models.EditExamViewModel
import net.felipealafy.studentplanner.models.EditStudentClassViewModel
import net.felipealafy.studentplanner.models.ExamCreationViewModel
import net.felipealafy.studentplanner.models.MainViewModel
import net.felipealafy.studentplanner.models.PlannerModel
import net.felipealafy.studentplanner.models.StudentClassCreationViewModel
import net.felipealafy.studentplanner.models.SubjectCreationViewModel
import net.felipealafy.studentplanner.models.TodayViewModel
import net.felipealafy.studentplanner.ui.theme.StudentPlannerTheme
import net.felipealafy.studentplanner.ui.views.DetailedClassView
import net.felipealafy.studentplanner.ui.views.DetailedExamView
import net.felipealafy.studentplanner.ui.views.DetailedPlannerView
import net.felipealafy.studentplanner.ui.views.EditExamView
import net.felipealafy.studentplanner.ui.views.EditStudentClassView
import net.felipealafy.studentplanner.ui.views.ExamCreationView
import net.felipealafy.studentplanner.ui.views.PlannerCreationView
import net.felipealafy.studentplanner.ui.views.StudentClassCreationView
import net.felipealafy.studentplanner.ui.views.StudentPlannerViews
import net.felipealafy.studentplanner.ui.views.SubjectCreationView
import net.felipealafy.studentplanner.ui.views.TodayView
import net.felipealafy.studentplanner.ui.views.WelcomeView

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
                        ?: StudentPlannerViews.WelcomeView.name
                ) {
                    composable(route = StudentPlannerViews.WelcomeView.name) {
                        WelcomeView(
                            onStartClick = {
                                navController.navigate(route = StudentPlannerViews.SetupView.name)
                            }
                        )
                    }
                    composable(route = StudentPlannerViews.SetupView.name) {
                        val viewModel: PlannerModel = hiltViewModel()
                        PlannerCreationView(
                            viewModel = viewModel,
                            backOnClick = {
                                navController.popBackStack()
                            },
                            forwardToTodayView = {
                                navController.navigate(route = StudentPlannerViews.TodayView.name)
                            }
                        )
                    }
                    composable(route = StudentPlannerViews.TodayView.name) {

                        val todayViewModel: TodayViewModel = hiltViewModel()
                        TodayView(
                            viewModel = todayViewModel,
                            onStudentClassClicked = { subjectId, studentClassId ->
                                navController.navigate("${StudentPlannerViews.DetailedClassView.name}/$subjectId/$studentClassId")
                            },
                            onExamClicked = { plannerId, subjectId, examId ->
                                navController.navigate("${StudentPlannerViews.DetailedExamView.name}/$plannerId/$subjectId/$examId")
                            },
                            onCreatePlannerClicked = {
                                navController.navigate(StudentPlannerViews.SetupView.name)
                            },
                            onCreateSubjectClicked = { plannerId ->
                                navController.navigate("${StudentPlannerViews.SubjectCreationView.name}/$plannerId")
                            },
                            onCreateClassClicked = { plannerId ->
                                navController.navigate("${StudentPlannerViews.StudentClassCreationView.name}/$plannerId")
                            },
                            onCreateExamClicked = { plannerId ->
                                navController.navigate("${StudentPlannerViews.ExamCreationView.name}/$plannerId")
                            },
                            onAccessDetailedPlannerView = { plannerId ->
                                navController.navigate("${StudentPlannerViews.DetailedPlannerView.name}/$plannerId")
                            }
                        )
                    }
                    composable (
                        route = "${StudentPlannerViews.DetailedPlannerView.name}/{plannerId}",
                        arguments = listOf(navArgument("plannerId") { type = NavType.StringType })
                    ) {
                        val viewModel: DetailedPlannerViewModel = hiltViewModel()
                        DetailedPlannerView(
                            viewModel = viewModel,
                            onReturnToPreviousView = { navController.popBackStack() }
                        )
                    }

                    composable(
                        route = "${StudentPlannerViews.StudentClassCreationView.name}/{plannerId}",
                        arguments = listOf(navArgument("plannerId") { type = NavType.StringType })
                    ) {
                        val viewModel: StudentClassCreationViewModel = hiltViewModel()
                        StudentClassCreationView(
                            studentClassCreationViewModel = viewModel,
                            onReturnAction = { navController.popBackStack() })
                    }

                    composable(
                        route = "${StudentPlannerViews.SubjectCreationView.name}/{plannerId}",
                        arguments = listOf(navArgument("plannerId") { type = NavType.StringType })
                    ) {
                        val viewModel: SubjectCreationViewModel = hiltViewModel()
                        SubjectCreationView(viewModel, { navController.popBackStack() })
                    }

                    composable(
                        route = "${StudentPlannerViews.DetailedClassView.name}/{subjectId}/{studentClassId}",
                        arguments = listOf(
                            navArgument("subjectId") { type = NavType.StringType },
                            navArgument("studentClassId") { type = NavType.StringType }
                        )
                    ) {
                        val viewModel: DetailedStudentClassViewModel = hiltViewModel()
                        DetailedClassView(
                            viewModel,
                            onEditMode = { plannerId, subjectId, classId ->
                                navController.navigate("${StudentPlannerViews.EditStudentClassView.name}/$plannerId/$subjectId/$classId")
                            },
                            onReturnAction = { navController.popBackStack() })
                    }

                    composable(
                        route = "{StudentPlannerViews.ExamCreationView.name}/{plannerId}",
                        arguments = listOf(
                            navArgument("plannerId") { type = NavType.StringType },
                        )
                    ) {
                        val viewModel: ExamCreationViewModel = hiltViewModel()
                        ExamCreationView(
                            viewModel = viewModel,
                            onReturnAction = { navController.popBackStack() })
                    }

                    composable(
                        route = "${StudentPlannerViews.DetailedExamView.name}/{plannerId}/{subjectId}/{examId}",
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
                                navController.navigate("${StudentPlannerViews.EditExamView.name}/$plannerId/$subjectId/$examId")
                            },
                            onReturnAction = { navController.popBackStack() }
                        )
                    }
                    composable(
                        route = "${StudentPlannerViews.EditStudentClassView.name}/{plannerId}/{subjectId}/{classId}",
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
                                navController.navigate(StudentPlannerViews.TodayView.name) {
                                    popUpTo(StudentPlannerViews.TodayView.name) {
                                        inclusive = true
                                    }
                                }
                            },
                        )
                    }

                    composable (
                        route = "${StudentPlannerViews.EditExamView.name}/{plannerId}/{subjectId}/{examId}",
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
                                navController.navigate(StudentPlannerViews.TodayView.name) {
                                    popUpTo(StudentPlannerViews.TodayView.name) {
                                        inclusive = true
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}