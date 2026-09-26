package net.felipealafy.studentplanner.feature_exam.domain.use_case

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import net.felipealafy.studentplanner.feature_exam.data.local.Exam
import net.felipealafy.studentplanner.feature_exam.data.repository.ExamRepositoryImpl
import net.felipealafy.studentplanner.feature_exam.domain.exception.InvalidExamExceptions
import javax.inject.Inject

class GetExamUseCase @Inject constructor(
    private val repository: ExamRepositoryImpl
) {
    operator fun invoke(examId: String) : Flow<Exam> {
        return repository.getExamById(examId).map {
            it.firstOrNull() ?: throw InvalidExamExceptions.ExamNotFound()
        }
    }
}