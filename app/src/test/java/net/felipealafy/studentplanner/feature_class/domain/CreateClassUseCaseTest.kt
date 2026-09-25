package net.felipealafy.studentplanner.feature_class.domain


import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import net.felipealafy.studentplanner.feature_class.domain.exception.InvalidClassExceptions
import net.felipealafy.studentplanner.feature_class.domain.repository.ClassRepository
import net.felipealafy.studentplanner.feature_class.domain.use_case.CreateClassParams
import net.felipealafy.studentplanner.feature_class.domain.use_case.CreateClassUseCase
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CreateClassUseCaseTest {
    private lateinit var createClassUseCase: CreateClassUseCase
    private lateinit var repository: ClassRepository

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        createClassUseCase = CreateClassUseCase(repository)
    }

    @Test
    fun shouldEmitAnErrorWhenNoTitleProvided(): Unit = runTest {
        val exception = assertFailsWith<InvalidClassExceptions.EmptyName> {
            createClassUseCase(
                params = CreateClassParams(
                    title = "",
                    subjectId = "subjectID",
                    start = LocalDateTime.now(),
                    end = LocalDateTime.now(),
                    noteTakingLink = "",
                    observation = ""
                )
            )
        }
        assertEquals("The class name can't be empty.", exception.message)
    }

    @Test
    fun shouldEmitAnErrorWhenNoSubjectIdProvided(): Unit = runTest {
        val exception = assertFailsWith<InvalidClassExceptions.EmptySubject> {
            createClassUseCase(
                params = CreateClassParams(
                    title = "Test",
                    subjectId = "",
                    start = LocalDateTime.now(),
                    end = LocalDateTime.now(),
                    noteTakingLink = "",
                    observation = ""
                )
            )
        }
        assertEquals("The subject can't be empty.", exception.message)
    }

    @Test
    fun shouldEmitAnErrorWhenInvalidDateTimeProvided(): Unit = runTest {
        val exception = assertFailsWith<InvalidClassExceptions.EmptyName> {
            createClassUseCase(
                params = CreateClassParams(
                    title = "Test",
                    subjectId = "subjectID",
                    start = LocalDateTime.now(),
                    end = LocalDateTime.now().minusDays(1),
                    noteTakingLink = "",
                    observation = ""
                )
            )
        }
        assertEquals("The start date must be before the end date.", exception.message)
    }

    @Test
    fun shouldEmitErrorWhenClassStartOrEndIsOutOfDateTimeBoundsOfTheSubject(): Unit = runTest {
        val exception = assertFailsWith<InvalidClassExceptions.EmptyName> {
            createClassUseCase(
                params = CreateClassParams(
                    title = "Test",
                    subjectId = "subjectID",
                    start = LocalDateTime.now(),
                    end = LocalDateTime.now().minusDays(1),
                    noteTakingLink = "",
                    observation = ""
                )
            )
        }
        assertEquals("The start date must be before the end date.", exception.message)
    }


    @Test
    fun shouldCreateAClassSuccessfully() {

    }
}