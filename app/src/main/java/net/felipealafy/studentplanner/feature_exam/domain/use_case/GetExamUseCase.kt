package net.felipealafy.studentplanner.feature_exam.domain.use_case

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import net.felipealafy.studentplanner.feature_exam.data.local.Exam
import net.felipealafy.studentplanner.feature_exam.data.repository.ExamRepositoryImpl
import net.felipealafy.studentplanner.feature_exam.domain.exception.InvalidExamExceptions
import net.felipealafy.studentplanner.feature_exam.domain.repository.ExamRepository
import javax.inject.Inject

class GetExamUseCase @Inject constructor(
    private val repository: ExamRepository
) {
    @Throws(InvalidExamExceptions::class)
    operator fun invoke(examId: String) : Flow<Exam> {

        if (examId.isBlank()) throw InvalidExamExceptions.ExamNotFound()

        return repository.getExamById(examId).map {
            it.firstOrNull() ?: throw InvalidExamExceptions.ExamNotFound()
        }
    }
}