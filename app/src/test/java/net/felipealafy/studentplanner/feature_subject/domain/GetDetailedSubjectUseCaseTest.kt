package net.felipealafy.studentplanner.feature_subject.domain

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import net.felipealafy.studentplanner.core.ui.theme.colorPallet
import net.felipealafy.studentplanner.feature_subject.domain.exception.InvalidSubjectExceptions
import net.felipealafy.studentplanner.feature_subject.domain.model.DetailedSubject
import net.felipealafy.studentplanner.feature_subject.domain.model.Subject
import net.felipealafy.studentplanner.feature_subject.domain.repository.SubjectRepository
import net.felipealafy.studentplanner.feature_subject.domain.use_case.GetDetailedSubjectUseCase
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class GetDetailedSubjectUseCaseTest {
    private lateinit var getDetailedSubjectUseCase: GetDetailedSubjectUseCase
    private lateinit var repository: SubjectRepository

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        getDetailedSubjectUseCase = GetDetailedSubjectUseCase(repository)
    }

    @Test
    fun shouldEmitErrorWhenSubjectNotFound() = runTest {
        every {
            repository.getSubjectWithDetails("subjectId")
        } returns flowOf(null)

        assertFailsWith<InvalidSubjectExceptions.SubjectNotFound> {
            getDetailedSubjectUseCase("subjectId").firstOrNull()
        }
    }

    @Test
    fun shouldGetDetailedSubjectSuccessfully() = runTest {
        val detailedSubject = DetailedSubject(
            subject = Subject(
                id = "subjectId",
                plannerId = "plannerId",
                name = "subject",
                color = colorPallet[0][1],
                start = LocalDateTime.now(),
                end = LocalDateTime.now().plusDays(20)
            ),
            studentClasses = emptyList(),
            exams = emptyList()
        )

        every {
            repository.getSubjectWithDetails("subjectId")
        } returns flowOf(detailedSubject)

        val result = getDetailedSubjectUseCase("subjectId").firstOrNull()

        assertEquals(detailedSubject, result)
    }
}