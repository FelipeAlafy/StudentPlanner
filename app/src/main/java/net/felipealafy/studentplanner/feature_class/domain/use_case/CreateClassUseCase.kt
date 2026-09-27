package net.felipealafy.studentplanner.feature_class.domain.use_case

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import net.felipealafy.studentplanner.feature_class.domain.exception.InvalidClassExceptions
import net.felipealafy.studentplanner.feature_class.domain.model.StudentClass
import net.felipealafy.studentplanner.feature_class.domain.repository.ClassRepository
import net.felipealafy.studentplanner.feature_subject.domain.repository.SubjectRepository
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
    private val classRepository: ClassRepository,
    private val subjectRepository: SubjectRepository
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

        val parentSubject = subjectRepository.getSubjectById(params.subjectId).firstOrNull()
            ?: throw InvalidClassExceptions.SubjectDoesNotExist()

        if (params.start.isBefore(parentSubject.start) || params.end.isAfter(parentSubject.end)) {
            throw InvalidClassExceptions.DateTimeOutOfSubjectBoundaries()
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