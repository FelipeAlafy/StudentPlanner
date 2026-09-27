package net.felipealafy.studentplanner.feature_exam.domain

import androidx.lifecycle.asLiveData
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import net.felipealafy.studentplanner.feature_exam.data.local.Exam
import net.felipealafy.studentplanner.feature_exam.domain.exception.InvalidExamExceptions
import net.felipealafy.studentplanner.feature_exam.domain.repository.ExamRepository
import net.felipealafy.studentplanner.feature_exam.domain.use_case.GetExamUseCase
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class GetExamUseCaseTest {
    private lateinit var repository: ExamRepository
    private lateinit var getExamUseCase: GetExamUseCase

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        getExamUseCase = GetExamUseCase(repository)
    }

    @Test
    fun shouldNotGetExam() = runTest {
        every {
            repository.getExamById("exam")
        } returns flowOf(emptyList())

        assertFailsWith<InvalidExamExceptions.ExamNotFound> {
            getExamUseCase("exam").firstOrNull()
        }
    }

    @Test
    fun shouldGetAnExam() = runTest {
        val exam = Exam(
            id = "exam",
            subjectId = "subject",
            name = "exam",
            grade = 70F,
            gradeWeight = 100F,
            start = LocalDateTime.now(),
            end = LocalDateTime.now().plusMinutes(50)
        )

        every {
            repository.getExamById("exam")
        } returns flowOf(listOf(exam))

        val result = getExamUseCase("exam").first()
        assertEquals(exam, result)
    }
}