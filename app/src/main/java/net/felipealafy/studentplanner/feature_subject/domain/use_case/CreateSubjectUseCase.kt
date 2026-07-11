package net.felipealafy.studentplanner.feature_subject.domain.use_case

import net.felipealafy.studentplanner.feature_subject.data.repository.SubjectRepositoryImpl
import net.felipealafy.studentplanner.feature_subject.domain.exception.InvalidSubjectExceptions
import net.felipealafy.studentplanner.feature_subject.domain.model.Subject
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

data class CreateSubjectParams(
    val plannerId: String,
    val name: String,
    val color: Long,
    val start: LocalDateTime,
    val end: LocalDateTime
)

class CreateSubjectUseCase @Inject constructor(
    private val repository: SubjectRepositoryImpl
) {
    suspend operator fun invoke(params: CreateSubjectParams) {
        if (params.name.isBlank()) {
            throw InvalidSubjectExceptions.EmptyName()
        }
        if (params.start.isAfter(params.end)) {
            throw InvalidSubjectExceptions.InvalidDateSelection()
        }

        val subjectToSave = Subject(
            id = UUID.randomUUID().toString(),
            plannerId = params.plannerId,
            name = params.name,
            color = params.color,
            start = params.start,
            end = params.end
        )

        repository.insert(subjectToSave)
    }
}