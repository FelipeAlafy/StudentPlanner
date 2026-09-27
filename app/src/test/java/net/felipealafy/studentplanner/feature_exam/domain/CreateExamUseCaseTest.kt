package net.felipealafy.studentplanner.feature_exam.domain

import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import net.felipealafy.studentplanner.feature_exam.domain.exception.InvalidExamExceptions
import net.felipealafy.studentplanner.feature_exam.domain.model.ExamParams
import net.felipealafy.studentplanner.feature_exam.domain.repository.ExamRepository
import org.junit.Before
import net.felipealafy.studentplanner.feature_exam.domain.use_case.CreateExamUseCase
import net.felipealafy.studentplanner.feature_exam.domain.use_case.GradeStyle
import net.felipealafy.studentplanner.feature_subject.domain.model.Subject
import net.felipealafy.studentplanner.feature_subject.domain.repository.SubjectRepository
import org.junit.Test
import java.time.LocalDateTime
import kotlin.test.assertFailsWith

class CreateExamUseCaseTest {
    private lateinit var repository: ExamRepository
    private lateinit var subjectRepository: SubjectRepository
    private lateinit var createExamUseCase: CreateExamUseCase

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        subjectRepository = mockk(relaxed = true)
        createExamUseCase = CreateExamUseCase(repository, subjectRepository)
    }

    @Test
    fun shouldEmitErrorWhenNoNameProvided() = runTest {
        val params = ExamParams(
            examId = "exam",
            subjectId = "subject",
            name = "",
            gradeString = "70",
            gradeWeightString = "100",
            start = LocalDateTime.now(),
            end = LocalDateTime.now().plusMinutes(50),
            gradeStyle = GradeStyle.FROM_ZERO_TO_ONE_HUNDRED
        )

        assertFailsWith<InvalidExamExceptions.EmptyName> {
            createExamUseCase(params)
        }
    }

    @Test
    fun shouldEmitErrorWhenNoSubjectIdProvided() = runTest {
        val params = ExamParams(
            examId = "exam",
            subjectId = "",
            name = "exam 1",
            gradeString = "70",
            gradeWeightString = "100",
            start = LocalDateTime.now(),
            end = LocalDateTime.now().plusMinutes(50),
            gradeStyle = GradeStyle.FROM_ZERO_TO_ONE_HUNDRED
        )

        assertFailsWith<InvalidExamExceptions.SubjectNotSelected> {
            createExamUseCase(params)
        }
    }

    @Test
    fun shouldEmitErrorWhenInvalidDateSelection() = runTest {
        val params = ExamParams(
            examId = "exam",
            subjectId = "subject",
            name = "exam 1",
            gradeString = "70",
            gradeWeightString = "100",
            start = LocalDateTime.now().plusDays(1),
            end = LocalDateTime.now(),
            gradeStyle = GradeStyle.FROM_ZERO_TO_ONE_HUNDRED
        )

        assertFailsWith<InvalidExamExceptions.InvalidDateSelection> {
            createExamUseCase(params)
        }
    }

    @Test
    fun shouldEmitErrorWhenGradeIsInvalidForZeroToOneHundredStyle() = runTest {
        val params = ExamParams(
            examId = "exam",
            subjectId = "subject",
            name = "Math Exam",
            gradeString = "105",
            gradeWeightString = "100",
            start = LocalDateTime.now(),
            end = LocalDateTime.now().plusMinutes(50),
            gradeStyle = GradeStyle.FROM_ZERO_TO_ONE_HUNDRED
        )

        assertFailsWith<InvalidExamExceptions.InvalidGradeFormat> {
            createExamUseCase(params)
        }
    }

    @Test
    fun shouldEmitErrorWhenGradeIsInvalidForDecimalStyle() = runTest {
        val params = ExamParams(
            examId = "exam",
            subjectId = "subject",
            name = "Math Exam",
            gradeString = "11,5",
            gradeWeightString = "100",
            start = LocalDateTime.now(),
            end = LocalDateTime.now().plusMinutes(50),
            gradeStyle = GradeStyle.FROM_A_TO_F
        )

        assertFailsWith<InvalidExamExceptions.InvalidGradeFormat> {
            createExamUseCase(params)
        }
    }

    @Test
    fun shouldEmitErrorWhenWeightIsInvalid() = runTest {
        val params = ExamParams(
            examId = "exam",
            subjectId = "subject",
            name = "Math Exam",
            gradeString = "85",
            gradeWeightString = "150",
            start = LocalDateTime.now(),
            end = LocalDateTime.now().plusMinutes(50),
            gradeStyle = GradeStyle.FROM_ZERO_TO_ONE_HUNDRED
        )

        assertFailsWith<InvalidExamExceptions.InvalidWeightFormat> {
            createExamUseCase(params)
        }
    }

    @Test
    fun shouldEmitErrorWhenSubjectIsNotFoundInDatabase() = runTest {
        val params = ExamParams(
            examId = "exam",
            subjectId = "invalidSubjectId",
            name = "Math Exam",
            gradeString = "9,5",
            gradeWeightString = "50",
            start = LocalDateTime.now(),
            end = LocalDateTime.now().plusMinutes(50),
            gradeStyle = GradeStyle.FROM_A_TO_F
        )

        every {
            subjectRepository.getSubjectById("invalidSubjectId")
        } returns flowOf(null)

        assertFailsWith<InvalidExamExceptions.SubjectDoesNotExistAnyMore> {
            createExamUseCase(params)
        }
    }

    @Test
    fun shouldEmitErrorWhenExamIsOutOfSubjectBoundaries() = runTest {
        val mockHeavySubject = Subject(
            id = "subjectId",
            plannerId = "plannerId",
            name = "Math",
            color = 0L,
            start = LocalDateTime.now(),
            end = LocalDateTime.now().plusDays(5)
        )

        val params = ExamParams(
            examId = "exam",
            subjectId = "subjectId",
            name = "Math Exam",
            gradeString = "9,5",
            gradeWeightString = "50",
            start = LocalDateTime.now().minusDays(1),
            end = LocalDateTime.now().plusMinutes(50),
            gradeStyle = GradeStyle.FROM_A_TO_F
        )

        every {
            subjectRepository.getSubjectById("subjectId")
        } returns flowOf(mockHeavySubject)

        assertFailsWith<InvalidExamExceptions.SelectedDateIsOutOfSubjectPeriodBoundaries> {
            createExamUseCase(params)
        }
    }

    @Test
    fun shouldCreateExamSuccessfullyAndParseCommaCorrectly() = runTest {
        val mockHeavySubject = Subject(
            id = "subjectId",
            plannerId = "plannerId",
            name = "Math",
            color = 0L,
            start = LocalDateTime.now(),
            end = LocalDateTime.now().plusDays(5)
        )

        val params = ExamParams(
            examId = "exam",
            subjectId = "subjectId",
            name = "Math Exam",
            gradeString = "9,5",
            gradeWeightString = "50",
            start = LocalDateTime.now().plusDays(1),
            end = LocalDateTime.now().plusDays(1).plusMinutes(50),
            gradeStyle = GradeStyle.FROM_A_TO_F
        )

        every {
            subjectRepository.getSubjectById("subjectId")
        } returns flowOf(mockHeavySubject)

        createExamUseCase(params)

        coVerify(exactly = 1) {
            repository.insert(match { savedExam ->
                savedExam.grade == 9.5f &&
                        savedExam.gradeWeight == 0.5f &&
                        savedExam.name == params.name
            })
        }
    }
}