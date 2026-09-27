package net.felipealafy.studentplanner.feature_exam.domain.use_case

import kotlinx.coroutines.flow.firstOrNull
import net.felipealafy.studentplanner.feature_exam.data.local.Exam
import net.felipealafy.studentplanner.feature_exam.data.repository.ExamRepositoryImpl
import net.felipealafy.studentplanner.feature_exam.domain.exception.InvalidExamExceptions
import net.felipealafy.studentplanner.feature_exam.domain.model.ExamParams
import net.felipealafy.studentplanner.feature_exam.domain.repository.ExamRepository
import net.felipealafy.studentplanner.feature_subject.domain.repository.SubjectRepository
import java.util.UUID
import javax.inject.Inject

class CreateExamUseCase @Inject constructor(
    private val repository: ExamRepository,
    private val subjectRepository: SubjectRepository
) {
    @Throws(InvalidExamExceptions::class)
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

        val subject = subjectRepository.getSubjectById(params.subjectId).firstOrNull()
            ?: throw InvalidExamExceptions.SubjectDoesNotExistAnyMore()

        if (params.start.isBefore(subject.start) || params.end.isAfter(subject.end)) {
            throw InvalidExamExceptions.SelectedDateIsOutOfSubjectPeriodBoundaries()
        }

        val newExam = Exam(
            id = UUID.randomUUID().toString(),
            subjectId = params.subjectId,
            name = params.name,
            grade = parsedGrade,
            gradeWeight = parsedWeight,
            start = params.start,
            end = params.end
        )

        repository.insert(newExam)
    }
}