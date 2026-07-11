package net.felipealafy.studentplanner.feature_class.domain.use_case

import net.felipealafy.studentplanner.feature_class.domain.exception.InvalidClassExceptions
import net.felipealafy.studentplanner.feature_class.domain.model.StudentClass
import net.felipealafy.studentplanner.feature_class.domain.repository.ClassRepository
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

data class CreateClassParams(
    val title: String,
    val subjectId: String,
    val start: LocalDateTime,
    val end: LocalDateTime,
    val noteTakingLink: String,
    val observation: String
)

class CreateClassUseCase @Inject constructor(
    private val classRepository: ClassRepository
) {
    @Throws(InvalidClassExceptions::class)
    suspend operator fun invoke(
        params: CreateClassParams
    ) {
        if (params.title.isBlank()) {
            throw InvalidClassExceptions.EmptyName()
        }
        if (params.start.isAfter(params.end)) {
            throw InvalidClassExceptions.InvalidDateTime()
        }

        if (params.subjectId.isBlank()) {
            throw InvalidClassExceptions.EmptySubject()
        }

        val newStudentClass = StudentClass(
            id = UUID.randomUUID().toString(),
            subjectId = params.subjectId,
            title = params.title,
            start = params.start,
            end = params.end,
            noteTakingLink = params.noteTakingLink,
            observation = params.observation
        )

        classRepository.insert(newStudentClass)
    }
}