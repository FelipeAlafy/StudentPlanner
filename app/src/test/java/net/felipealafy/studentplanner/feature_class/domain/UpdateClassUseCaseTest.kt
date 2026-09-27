package net.felipealafy.studentplanner.feature_class.domain


import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import net.felipealafy.studentplanner.core.ui.theme.colorPallet
import net.felipealafy.studentplanner.feature_class.domain.exception.InvalidClassExceptions
import net.felipealafy.studentplanner.feature_class.domain.repository.ClassRepository
import net.felipealafy.studentplanner.feature_class.domain.use_case.UpdateClassParams
import net.felipealafy.studentplanner.feature_class.domain.use_case.UpdateClassUseCase
import net.felipealafy.studentplanner.feature_subject.domain.model.Subject
import net.felipealafy.studentplanner.feature_subject.domain.repository.SubjectRepository
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class UpdateClassUseCaseTest {
    private lateinit var updateClassUseCase: UpdateClassUseCase
    private lateinit var classRepository: ClassRepository
    private lateinit var subjectRepository: SubjectRepository

    @Before
    fun setUp() {
        classRepository = mockk(relaxed = true)
        subjectRepository = mockk(relaxed = true)

        updateClassUseCase = UpdateClassUseCase(
            classRepository = classRepository,
            subjectRepository = subjectRepository
        )
    }
    @Test
    fun shouldEmitAnErrorWhenNoClassIdWasProvided(): Unit = runTest {
        val exception = assertFailsWith<InvalidClassExceptions.ClassNotFound> {
            updateClassUseCase(
                params = UpdateClassParams(
                    title = "Class",
                    subjectId = "subjectID",
                    start = LocalDateTime.now(),
                    end = LocalDateTime.now(),
                    noteTakingLink = "",
                    observation = "",
                    classId = ""
                )
            )
        }
        assertEquals("The class was not found.", exception.message)
    }

    @Test
    fun shouldEmitAnErrorWhenNoTitleWasProvided(): Unit = runTest {
        val exception = assertFailsWith<InvalidClassExceptions.EmptyName> {
            updateClassUseCase(
                params = UpdateClassParams(
                    title = "",
                    subjectId = "subjectID",
                    start = LocalDateTime.now(),
                    end = LocalDateTime.now(),
                    noteTakingLink = "",
                    observation = "",
                    classId = "ClassId"
                )
            )
        }
        assertEquals("The class name can't be empty.", exception.message)
    }

    @Test
    fun shouldEmitAnErrorWhenNoSubjectIdProvided(): Unit = runTest {
        val exception = assertFailsWith<InvalidClassExceptions.EmptySubject> {
            updateClassUseCase(
                params = UpdateClassParams(
                    title = "Test",
                    subjectId = "",
                    start = LocalDateTime.now(),
                    end = LocalDateTime.now(),
                    noteTakingLink = "",
                    observation = "",
                    classId = "ClassId",
                )
            )
        }
        assertEquals("The subject can't be empty.", exception.message)
    }

    @Test
    fun shouldEmitAnErrorWhenInvalidDateTimeProvided(): Unit = runTest {
        val exception = assertFailsWith<InvalidClassExceptions.InvalidDateTime> {
            updateClassUseCase(
                params = UpdateClassParams(
                    title = "Test",
                    subjectId = "subjectID",
                    start = LocalDateTime.now(),
                    end = LocalDateTime.now().minusDays(1),
                    noteTakingLink = "",
                    observation = "",
                    classId = "ClassId",
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

            val stClass = UpdateClassParams(
                title = "Test",
                subjectId = "subjectId",
                start = LocalDateTime.now().minusDays(2),
                end = LocalDateTime.now().minusDays(1),
                noteTakingLink = "",
                observation = "",
                classId = "ClassId",
            )

            every {
                subjectRepository.getSubjectById("subjectId")
            } returns flowOf(mockHeavySubject)

            val exception = assertFailsWith<InvalidClassExceptions.DateTimeOutOfSubjectBoundaries> {
                updateClassUseCase(
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

            val stClass = UpdateClassParams(
                title = "Test",
                subjectId = "subjectId",
                start = LocalDateTime.now(),
                end = LocalDateTime.now().plusDays(10),
                noteTakingLink = "",
                observation = "",
                classId = "ClassId"
            )

            every {
                subjectRepository.getSubjectById("subjectId")
            } returns flowOf(mockHeavySubject)

            val exception = assertFailsWith<InvalidClassExceptions.DateTimeOutOfSubjectBoundaries> {
                updateClassUseCase(
                    params = stClass
                )
            }
            assertEquals("The start and end date/time of a class should be inside of the subject start/end interval.", exception.message)
        }

    @Test
    fun shouldEmitErrorWhenSubjectIsNotFoundedInDatabase(): Unit = runTest {
        val stClass = UpdateClassParams(
            title = "Test",
            subjectId = "Subject",
            start = LocalDateTime.now(),
            end = LocalDateTime.now().plusDays(10),
            noteTakingLink = "https://studentplanner.felipealafy.net",
            observation = "This is a class",
            classId = "ClassId",
        )

        every {
            subjectRepository.getSubjectById("Subject")
        } returns flowOf(null)

        val exception = assertFailsWith<InvalidClassExceptions.SubjectDoesNotExist> {
            updateClassUseCase(stClass)
        }

        assertEquals("The provided subject does not exist in the data base.", exception.message)
    }


    @Test
    fun shouldUpdateAClassSuccessfully(): Unit = runTest {
        val mockHeavySubject = Subject(
            id = "Subject",
            plannerId = "Planner",
            name = "Test",
            color = colorPallet[0][1],
            start = LocalDateTime.now(),
            end = LocalDateTime.now().plusDays(3)
        )
        val stClass = UpdateClassParams(
            title = "Test",
            subjectId = "Subject",
            start = LocalDateTime.now(),
            end = LocalDateTime.now().plusMinutes(50),
            noteTakingLink = "https://studentplanner.felipealafy.net",
            observation = "This is a class",
            classId = "ClassId"
        )

        every {
            subjectRepository.getSubjectById("Subject")
        } returns flowOf(mockHeavySubject)

        updateClassUseCase(stClass)

        coVerify(exactly = 1) {
            classRepository.update(any())
        }
    }
}