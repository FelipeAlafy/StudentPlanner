package net.felipealafy.studentplanner.feature_class.domain.use_case

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import net.felipealafy.studentplanner.feature_class.domain.exception.InvalidClassExceptions
import net.felipealafy.studentplanner.feature_class.domain.model.EnrichedDetailedClass
import net.felipealafy.studentplanner.feature_class.domain.repository.ClassRepository
import javax.inject.Inject

class GetClassUseCase @Inject constructor(
    private val repository: ClassRepository
) {
    operator fun invoke(classId: String): Flow<EnrichedDetailedClass> {
        return repository.getEnrichedDetailedClass(classId).map {
            it ?: throw InvalidClassExceptions.ClassNotFound()
        }
    }
}