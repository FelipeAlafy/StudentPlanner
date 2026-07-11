package net.felipealafy.studentplanner.feature_planner.domain.exception

sealed class InvalidPlannerException(exception: String): Exception(exception) {
    class EmptyName: InvalidPlannerException("The planner name can't be empty.")
    class InvalidMinimumGradeToPass: InvalidPlannerException("The minimum grade to pass must be between 0 and 100.")
    class PlannerNotFound: InvalidPlannerException("The planner was not found.")
}