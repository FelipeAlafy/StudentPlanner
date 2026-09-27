package net.felipealafy.studentplanner.feature_class.domain.use_case

import jakarta.inject.Inject
import net.felipealafy.studentplanner.feature_class.domain.exception.InvalidClassExceptions
import net.felipealafy.studentplanner.feature_class.domain.repository.ClassRepository

class DeleteClassUseCase @Inject constructor(
    private val repository: ClassRepository
) {
    @Throws(InvalidClassExceptions::class)
    suspend operator fun invoke(classId: String) {

        if (classId.isEmpty()) {
            throw InvalidClassExceptions.ClassNotFound()
        }

        try {
            val affectedRows = repository.delete(classId)

            if (affectedRows == 0) {
                throw InvalidClassExceptions.ClassDeletionError()
            }
        } catch (e: InvalidClassExceptions) {
            throw e
        } catch (e: Exception) {
            throw InvalidClassExceptions.ClassDeletionIODBError()
        }

    }
}