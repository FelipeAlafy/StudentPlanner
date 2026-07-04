package net.felipealafy.studentplanner.feature_class.domain.use_case

import net.felipealafy.studentplanner.feature_exams.data.local.Exam
import net.felipealafy.studentplanner.feature_exams.data.repository.ExamRepository
import net.felipealafy.studentplanner.feature_exams.domain.exception.InvalidExamExceptions
import net.felipealafy.studentplanner.feature_exams.domain.use_case.GradeStyle
import java.time.LocalDateTime
import javax.inject.Inject

data class UpdateExamParams (
    val examId: String,
    val subjectId: String,
    val name: String,
    val gradeString: String,
    val gradeWeightString: String,
    val start: LocalDateTime,
    val end: LocalDateTime,
    val gradeStyle: GradeStyle
)

class UpdateExamUseCase @Inject constructor(private val repository: ExamRepository) {
    suspend operator fun invoke(params: UpdateExamParams) {
        if (params.name.isBlank()) throw InvalidExamExceptions.EmptyName()
        if (params.subjectId.isBlank()) throw InvalidExamExceptions.SubjectNotSelected()
        if (params.start.isAfter(params.end)) throw InvalidExamExceptions.InvalidDateSelection()

        val gradePattern = if (params.gradeStyle == GradeStyle.FROM_ZERO_TO_ONE_HUNDRED) {
            Regex("^(100|[1-9]?[0-9])$")
        } else {
            Regex("^(10([.,]0*)?|[0-9]([.,][0-9]+)?)$")
        }
        if (!gradePattern.matches(params.gradeString)) {
            throw InvalidExamExceptions.InvalidGradeFormat()
        }

        val weightPattern = Regex("^(100|[1-9]?[0-9])$")
        if (!weightPattern.matches(params.gradeWeightString)) {
            throw InvalidExamExceptions.InvalidWeightFormat()
        }

        val parsedGrade = params.gradeString.replace(',', '.').toFloat()
        val parsedWeight = params.gradeWeightString.replace(',', '.').toFloat() / 100f

        val examToUpdate = Exam(
            id = params.examId,
            subjectId = params.subjectId,
            name = params.name,
            grade = parsedGrade,
            gradeWeight = parsedWeight,
            start = params.start,
            end = params.end
        )

        repository.update(examToUpdate)
    }
}