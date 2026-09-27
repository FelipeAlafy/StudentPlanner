package net.felipealafy.studentplanner.feature_subject.domain

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import net.felipealafy.studentplanner.core.ui.theme.colorPallet
import net.felipealafy.studentplanner.feature_subject.domain.exception.InvalidSubjectExceptions
import net.felipealafy.studentplanner.feature_subject.domain.model.Subject
import net.felipealafy.studentplanner.feature_subject.domain.repository.SubjectRepository
import net.felipealafy.studentplanner.feature_subject.domain.use_case.CreateSubjectParams
import net.felipealafy.studentplanner.feature_subject.domain.use_case.CreateSubjectUseCase
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime
import kotlin.test.assertFailsWith

class CreateSubjectUseCaseTest {
    private lateinit var repository: SubjectRepository
    private lateinit var createSubjectUseCase: CreateSubjectUseCase

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        createSubjectUseCase = CreateSubjectUseCase(repository)
    }

    @Test
    fun shouldEmitErrorWhenNoNameProvided() = runTest {
        val params = CreateSubjectParams(
            plannerId = "plannerId",
            name = "",
            color = colorPallet[0][1],
            start = LocalDateTime.now(),
            end = LocalDateTime.now().plusDays(1)
        )

        assertFailsWith<InvalidSubjectExceptions.EmptyName> {
            createSubjectUseCase(params)
        }
    }

    @Test
    fun shouldEmitErrorWhenInvalidDateTimeProvided() = runTest {
        val params = CreateSubjectParams(
            plannerId = "plannerId",
            name = "subject",
            color = colorPallet[0][1],
            start = LocalDateTime.now(),
            end = LocalDateTime.now().minusDays(3)
        )

        assertFailsWith<InvalidSubjectExceptions.InvalidDateSelection> {
            createSubjectUseCase(params)
        }
    }

    @Test
    fun shouldCreateSubjectSuccessfully() = runTest {
        val params = CreateSubjectParams(
            plannerId = "plannerId",
            name = "subject",
            color = colorPallet[0][1],
            start = LocalDateTime.now(),
            end = LocalDateTime.now().plusDays(10)
        )

        coEvery {
            repository.insert(
                Subject(
                    plannerId = params.plannerId,
                    name = params.name,
                    color = params.color,
                    start = params.start,
                    end = params.end
                )
            )
        }

        createSubjectUseCase(params)

        coVerify(exactly = 1) {
            repository.insert(
                match { savedSubject ->
                    savedSubject.name == params.name &&
                    savedSubject.color == params.color &&
                    savedSubject.start == params.start &&
                    savedSubject.end == params.end
                }
            )
        }
    }
}