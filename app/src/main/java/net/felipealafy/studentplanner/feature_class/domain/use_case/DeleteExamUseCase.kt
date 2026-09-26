package net.felipealafy.studentplanner.feature_class.domain.use_case

import jakarta.inject.Inject
import net.felipealafy.studentplanner.feature_exam.domain.exception.InvalidExamExceptions
import net.felipealafy.studentplanner.feature_exam.domain.exception.InvalidExamExceptions.ExamNotFound
import net.felipealafy.studentplanner.feature_exam.domain.repository.ExamRepository

class DeleteExamUseCase @Inject constructor(
    private val repository: ExamRepository
) {
    @Throws(InvalidExamExceptions::class)
    suspend operator fun invoke(examId: String) {
        if (examId.isBlank()) throw ExamNotFound()

        try {
            val affectedRows = repository.delete(examId)

            if (affectedRows == 0) {
                throw InvalidExamExceptions.ExamDeletionError()
            }
        } catch (e: InvalidExamExceptions) {
            throw e
        } catch (e: Exception) {
            throw InvalidExamExceptions.ExamDeletionIODBError()
        }
    }
}