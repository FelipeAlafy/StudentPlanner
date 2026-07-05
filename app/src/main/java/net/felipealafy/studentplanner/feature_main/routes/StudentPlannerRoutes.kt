package net.felipealafy.studentplanner.feature_main.routes

sealed class StudentPlannerRoutes(val route: String) {
    data object WelcomeView: StudentPlannerRoutes("welcome_view")
    data object ImportingView: StudentPlannerRoutes("importing_view")
    data object SetupView: StudentPlannerRoutes("setup_view")
    data object TodayView: StudentPlannerRoutes("today_view")
    data object ConfigurationView: StudentPlannerRoutes("configuration_view")

    data object DetailedPlannerView : StudentPlannerRoutes("detailed_planner/{plannerId}") {
        fun createRoute(plannerId: String) = "detailed_planner/$plannerId"
    }

    data object EditingPlannerView : StudentPlannerRoutes("editing_planner/{plannerId}") {
        fun createRoute(plannerId: String) = "editing_planner/$plannerId"
    }

    data object SubjectCreationView : StudentPlannerRoutes("subject_creation/{plannerId}") {
        fun createRoute(plannerId: String) = "subject_creation/$plannerId"
    }

    data object StudentClassCreationView : StudentPlannerRoutes("class_creation/{plannerId}") {
        fun createRoute(plannerId: String) = "class_creation/$plannerId"
    }

    data object ExamCreationView : StudentPlannerRoutes("exam_creation/{plannerId}") {
        fun createRoute(plannerId: String) = "exam_creation/$plannerId"
    }

    // Rotas que precisam de MÚLTIPLOS argumentos (Hierarquia completa)
    data object DetailedSubjectView : StudentPlannerRoutes("detailed_subject/{plannerId}/{subjectId}") {
        fun createRoute(plannerId: String, subjectId: String) = "detailed_subject/$plannerId/$subjectId"
    }

    data object EditingSubjectView : StudentPlannerRoutes("editing_subject/{plannerId}/{subjectId}") {
        fun createRoute(plannerId: String, subjectId: String) = "editing_subject/$plannerId/$subjectId"
    }

    data object DetailedClassView : StudentPlannerRoutes("detailed_class/{subjectId}/{studentClassId}") {
        fun createRoute(subjectId: String, studentClassId: String) =
            "detailed_class/$subjectId/$studentClassId"
    }

    data object EditingClassView : StudentPlannerRoutes("editing_class/{plannerId}/{subjectId}/{classId}") {
        fun createRoute(plannerId: String, subjectId: String, classId: String) =
            "editing_class/$plannerId/$subjectId/$classId"
    }

    data object DetailedExamView : StudentPlannerRoutes("detailed_exam/{plannerId}/{subjectId}/{examId}") {
        fun createRoute(plannerId: String, subjectId: String, examId: String) =
            "detailed_exam/$plannerId/$subjectId/$examId"
    }

    data object EditExamView : StudentPlannerRoutes("edit_exam/{plannerId}/{subjectId}/{examId}") {
        fun createRoute(plannerId: String, subjectId: String, examId: String) =
            "edit_exam/$plannerId/$subjectId/$examId"
    }
}