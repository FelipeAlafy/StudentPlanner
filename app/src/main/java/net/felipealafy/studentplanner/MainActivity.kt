package net.felipealafy.studentplanner

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import net.felipealafy.studentplanner.models.DetailedExamViewModel
import net.felipealafy.studentplanner.models.DetailedPlannerViewModel
import net.felipealafy.studentplanner.models.DetailedStudentClassViewModel
import net.felipealafy.studentplanner.models.MainViewModel
import net.felipealafy.studentplanner.models.PlannerModel
import net.felipealafy.studentplanner.models.StudentClassViewModel
import net.felipealafy.studentplanner.models.SubjectCreationViewModel
import net.felipealafy.studentplanner.models.TodayViewModel
import net.felipealafy.studentplanner.ui.AppViewModelProvider
import net.felipealafy.studentplanner.ui.theme.StudentPlannerTheme
import net.felipealafy.studentplanner.ui.views.DetailedClassView
import net.felipealafy.studentplanner.ui.views.DetailedPlannerView
import net.felipealafy.studentplanner.ui.views.PlannerCreationView
import net.felipealafy.studentplanner.ui.views.StudentClassCreationView
import net.felipealafy.studentplanner.ui.views.StudentPlannerViews
import net.felipealafy.studentplanner.ui.views.SubjectCreationView
import net.felipealafy.studentplanner.ui.views.TodayView
import net.felipealafy.studentplanner.ui.views.WelcomeView
import net.felipealafy.studentplanner.models.ExamCreationViewModel
import net.felipealafy.studentplanner.ui.views.DetailedExamView
import net.felipealafy.studentplanner.ui.views.ExamCreationView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StudentPlannerTheme {
                val navController = rememberNavController()
                val mainViewModel: MainViewModel = viewModel(factory = AppViewModelProvider.Factory)

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
                        val viewModel: PlannerModel =
                            viewModel(factory = AppViewModelProvider.Factory)
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

                        val todayViewModel: TodayViewModel =
                            viewModel(factory = AppViewModelProvider.Factory)
                        TodayView(
                            viewModel = todayViewModel,
                            onStudentClassClicked = { subjectId, studentClassId ->
                                Log.i("MainActitity -> TodayView", "id: $subjectId name: $studentClassId")
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
                            }
                        )
                    }
                    composable(
                        route = "${StudentPlannerViews.DetailedPlannerView.name}/{plannerId}",
                        arguments = listOf(navArgument("plannerId") { type = NavType.StringType })
                    ) {
                        val viewModel: DetailedPlannerViewModel =
                            viewModel(factory = AppViewModelProvider.Factory)
                        val plannerState = viewModel.uiState.collectAsState()

                        plannerState.value?.let { planner ->
                            DetailedPlannerView(planner = planner)
                        }
                    }

                    composable(
                        route = "${StudentPlannerViews.StudentClassCreationView.name}/{plannerId}",
                        arguments = listOf(navArgument("plannerId") { type = NavType.StringType })
                    ) {
                        val viewModel: StudentClassViewModel =
                            viewModel(factory = AppViewModelProvider.Factory)
                        StudentClassCreationView(
                            studentClassViewModel = viewModel,
                            onReturnAction = { navController.popBackStack() })
                    }

                    composable(
                        route = "${StudentPlannerViews.SubjectCreationView.name}/{plannerId}",
                        arguments = listOf(navArgument("plannerId") { type = NavType.StringType })
                    ) {
                        val viewModel: SubjectCreationViewModel =
                            viewModel(factory = AppViewModelProvider.Factory)
                        SubjectCreationView(viewModel, { navController.popBackStack() })
                    }

                    composable(
                        route = "${StudentPlannerViews.DetailedClassView.name}/{subjectId}/{studentClassId}",
                        arguments = listOf(
                            navArgument("subjectId") { type = NavType.StringType },
                            navArgument("studentClassId") { type = NavType.StringType }
                        )
                    ) {
                        val viewModel: DetailedStudentClassViewModel =
                            viewModel(factory = AppViewModelProvider.Factory)
                        DetailedClassView(
                            viewModel,
                            onReturnAction = { navController.popBackStack() })
                    }

                    composable(
                        route = "{StudentPlannerViews.ExamCreationView.name}/{plannerId}",
                        arguments = listOf(
                            navArgument("plannerId") { type = NavType.StringType },
                        )
                    ) {
                        val viewModel: ExamCreationViewModel =
                            viewModel(factory = AppViewModelProvider.Factory)
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
                        val viewModel: DetailedExamViewModel =
                            viewModel(factory = AppViewModelProvider.Factory)
                        DetailedExamView(
                            viewModel = viewModel,
                            onReturnAction = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}