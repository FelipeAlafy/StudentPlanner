package net.felipealafy.studentplanner.feature_class.domain


import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import net.felipealafy.studentplanner.core.ui.theme.colorPallet
import net.felipealafy.studentplanner.feature_class.domain.exception.InvalidClassExceptions
import net.felipealafy.studentplanner.feature_class.domain.repository.ClassRepository
import net.felipealafy.studentplanner.feature_class.domain.use_case.CreateClassParams
import net.felipealafy.studentplanner.feature_class.domain.use_case.CreateClassUseCase
import net.felipealafy.studentplanner.feature_subject.domain.model.Subject
import net.felipealafy.studentplanner.feature_subject.domain.repository.SubjectRepository
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CreateClassUseCaseTest {
    private lateinit var createClassUseCase: CreateClassUseCase
    private lateinit var classRepository: ClassRepository
    private lateinit var subjectRepository: SubjectRepository

    @Before
    fun setUp() {
        classRepository = mockk(relaxed = true)
        subjectRepository = mockk(relaxed = true)

        createClassUseCase = CreateClassUseCase(
            classRepository = classRepository,
            subjectRepository = subjectRepository
        )
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
        val exception = assertFailsWith<InvalidClassExceptions.InvalidDateTime> {
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
    fun shouldEmitErrorWhenClassStartOrEndIsOutOfDateTimeBoundsOfTheSubjectForLess(): Unit =
        runTest {
            val mockHeavySubject = Subject(
                id = "Subject",
                plannerId = "Planner",
                name = "Test",
                color = colorPallet[0][1],
                start = LocalDateTime.now(),
                end = LocalDateTime.now().plusDays(3)
            )

            val stClass = CreateClassParams(
                title = "Test",
                subjectId = "subjectId",
                start = LocalDateTime.now().minusDays(2),
                end = LocalDateTime.now().minusDays(1),
                noteTakingLink = "",
                observation = ""
            )

            every {
                subjectRepository.getSubjectById("subjectId")
            } returns flowOf(mockHeavySubject)

            val exception = assertFailsWith<InvalidClassExceptions.DateTimeOutOfSubjectBoundaries> {
                createClassUseCase(
                    params = stClass
                )
            }
            assertEquals("The start and end date/time of a class should be inside of the subject start/end interval.", exception.message)
        }

    @Test
    fun shouldEmitErrorWhenClassStartOrEndIsOutOfDateTimeBoundsOfTheSubjectForMore(): Unit =
        runTest {
            val mockHeavySubject = Subject(
                id = "Subject",
                plannerId = "Planner",
                name = "Test",
                color = colorPallet[0][1],
                start = LocalDateTime.now(),
                end = LocalDateTime.now().plusDays(3)
            )

            val stClass = CreateClassParams(
                title = "Test",
                subjectId = "subjectId",
                start = LocalDateTime.now(),
                end = LocalDateTime.now().plusDays(10),
                noteTakingLink = "",
                observation = ""
            )

            every {
                subjectRepository.getSubjectById("subjectId")
            } returns flowOf(mockHeavySubject)

            val exception = assertFailsWith<InvalidClassExceptions.DateTimeOutOfSubjectBoundaries> {
                createClassUseCase(
                    params = stClass
                )
            }
            assertEquals("The start and end date/time of a class should be inside of the subject start/end interval.", exception.message)
        }

    @Test
    fun shouldEmitErrorWhenSubjectIsNotFoundedInDatabase(): Unit = runTest {
        val stClass = CreateClassParams(
            title = "Test",
            subjectId = "Subject",
            start = LocalDateTime.now(),
            end = LocalDateTime.now().plusDays(10),
            noteTakingLink = "https://studentplanner.felipealafy.net",
            observation = "This is a class"
        )

        every {
            subjectRepository.getSubjectById("Subject")
        } returns flowOf(null)

        val exception = assertFailsWith<InvalidClassExceptions.SubjectDoesNotExist> {
            createClassUseCase(stClass)
        }

        assertEquals("The provided subject does not exist in the data base.", exception.message)
    }


    @Test
    fun shouldCreateAClassSuccessfully(): Unit = runTest {
        val mockHeavySubject = Subject(
            id = "Subject",
            plannerId = "Planner",
            name = "Test",
            color = colorPallet[0][1],
            start = LocalDateTime.now(),
            end = LocalDateTime.now().plusDays(3)
        )
        val stClass = CreateClassParams(
            title = "Test",
            subjectId = "Subject",
            start = LocalDateTime.now(),
            end = LocalDateTime.now().plusMinutes(50),
            noteTakingLink = "https://studentplanner.felipealafy.net",
            observation = "This is a class"
        )

        every {
            subjectRepository.getSubjectById("Subject")
        } returns flowOf(mockHeavySubject)

        createClassUseCase(stClass)

        coVerify(exactly = 1) {
            classRepository.insert(any())
        }
    }
}