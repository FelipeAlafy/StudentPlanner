package net.felipealafy.studentplanner.feature_exams.domain.use_case

import net.felipealafy.studentplanner.feature_exams.data.local.Exam
import net.felipealafy.studentplanner.feature_exams.data.repository.ExamRepositoryImpl
import net.felipealafy.studentplanner.feature_exams.domain.exception.InvalidExamExceptions
import net.felipealafy.studentplanner.feature_exams.domain.model.ExamParams
import javax.inject.Inject

class UpdateExamUseCase @Inject constructor(private val repository: ExamRepositoryImpl) {
    suspend operator fun invoke(params: ExamParams) {
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