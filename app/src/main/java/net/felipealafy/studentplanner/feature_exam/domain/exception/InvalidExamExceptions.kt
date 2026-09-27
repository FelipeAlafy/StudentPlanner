package net.felipealafy.studentplanner.feature_exam.domain.exception

import net.felipealafy.studentplanner.feature_class.domain.exception.InvalidClassExceptions

sealed class InvalidExamExceptions(val exception: String): Exception(exception) {
    class ExamNotFound: InvalidExamExceptions("Unable to locate the exam, please try again.")
    class EmptyName: InvalidExamExceptions("Please provide a valid name.")
    class SubjectNotSelected: InvalidExamExceptions("Please select a subject before saving.")
    class InvalidDateSelection: InvalidExamExceptions("Start date needs to be set before the end date.")
    class InvalidWeightFormat: InvalidExamExceptions("Weight needs to be in the format 1 to 100")
    class InvalidGradeFormat: InvalidExamExceptions("Grade needs to be in one of the following format 0 to 100 or 0,00 to 10.00")
    class ExamDeletionError: InvalidExamExceptions( "An error occurred while deleting the class.")
    class ExamDeletionIODBError: InvalidExamExceptions("An error occurred in the database.")
    class SubjectDoesNotExistAnyMore: InvalidExamExceptions("The subject does not exist anymore.")
    class SelectedDateIsOutOfSubjectPeriodBoundaries: InvalidExamExceptions("The selected date is out of the subject period boundaries.")
}