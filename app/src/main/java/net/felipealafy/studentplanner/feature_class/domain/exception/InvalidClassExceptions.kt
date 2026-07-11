package net.felipealafy.studentplanner.feature_class.domain.exception

import net.felipealafy.studentplanner.feature_planner.domain.exception.InvalidPlannerException

sealed class InvalidClassExceptions(val exception: String): Exception(exception) {
    class EmptyName: InvalidClassExceptions("The class name can't be empty.")
    class InvalidDateTime: InvalidClassExceptions("The start date must be before the end date.")
    class ClassNotFound: InvalidClassExceptions("The class was not found.")
    class EmptySubject: InvalidClassExceptions("The subject can't be empty.")
}