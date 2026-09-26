package net.felipealafy.studentplanner.feature_class.domain.exception

sealed class InvalidClassExceptions(val exception: String): Exception(exception) {
    class EmptyName: InvalidClassExceptions("The class name can't be empty.")
    class InvalidDateTime: InvalidClassExceptions("The start date must be before the end date.")
    class ClassNotFound: InvalidClassExceptions("The class was not found.")
    class EmptySubject: InvalidClassExceptions("The subject can't be empty.")
    class DateTimeOutOfSubjectBoundaries: InvalidClassExceptions("The start and end date/time of a class should be inside of the subject start/end interval.")
    class SubjectDoesNotExist: InvalidClassExceptions("The provided subject does not exist in the data base.")
    class ClassDeletionError: InvalidClassExceptions( "An error occurred while deleting the class.")
    class ClassDeletionIODBError: InvalidClassExceptions("An error occurred in the database.")
}