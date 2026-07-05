package net.felipealafy.studentplanner.feature_subject.domain.exception

sealed class InvalidSubjectExceptions(val exception: String): Exception(exception) {
    class PlannerNotFound: InvalidSubjectExceptions("Unable to locate the planner, please try again.")
    class EmptyName: InvalidSubjectExceptions("Please provide a valid name.")
    class InvalidDateSelection: InvalidSubjectExceptions("Start date needs to be set before the end date.")
}