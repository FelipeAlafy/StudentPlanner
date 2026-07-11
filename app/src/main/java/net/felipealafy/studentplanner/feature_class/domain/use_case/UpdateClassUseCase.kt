package net.felipealafy.studentplanner.feature_class.domain.use_case

import net.felipealafy.studentplanner.feature_class.domain.exception.InvalidClassExceptions
import net.felipealafy.studentplanner.feature_class.domain.model.StudentClass
import net.felipealafy.studentplanner.feature_class.domain.repository.ClassRepository
import java.time.LocalDateTime
import javax.inject.Inject

data class UpdateClassParams(
    val classId: String,
    val title: String,
    val subjectId: String,
    val start: LocalDateTime,
    val end: LocalDateTime,
    val noteTakingLink: String,
    val observation: String
)

class UpdateClassUseCase @Inject constructor(
    private val repository: ClassRepository
) {
    @Throws(InvalidClassExceptions::class)
    suspend operator fun invoke(params: UpdateClassParams) {
        if (params.classId.isEmpty()) {
            throw InvalidClassExceptions.ClassNotFound()
        }

        if (params.title.isEmpty()) {
            throw InvalidClassExceptions.EmptyName()
        }

        if (params.subjectId.isEmpty()) {
            throw InvalidClassExceptions.EmptySubject()
        }

        if (params.start.isAfter(params.end)) {
            throw InvalidClassExceptions.InvalidDateTime()
        }

        val updateClass = StudentClass(
            id = params.classId,
            subjectId = params.subjectId,
            title = params.title,
            start = params.start,
            end = params.end,
            noteTakingLink = params.noteTakingLink,
            observation = params.observation
        )

        repository.update(studentClass = updateClass)
    }
}