package net.felipealafy.studentplanner.feature_class.domain

import android.R.id.message
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import net.felipealafy.studentplanner.core.ui.theme.colorPallet
import net.felipealafy.studentplanner.feature_class.domain.exception.InvalidClassExceptions
import net.felipealafy.studentplanner.feature_class.domain.model.EnrichedDetailedClass
import net.felipealafy.studentplanner.feature_class.domain.model.StudentClass
import net.felipealafy.studentplanner.feature_class.domain.repository.ClassRepository
import net.felipealafy.studentplanner.feature_class.domain.use_case.GetClassUseCase
import net.felipealafy.studentplanner.feature_subject.domain.model.Subject
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotEquals

class GetClassUseCaseTest {
    private lateinit var getClassUseCase: GetClassUseCase
    private lateinit var repository: ClassRepository

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        getClassUseCase = GetClassUseCase(repository)
    }

    @Test
    fun successfullyGetAEnrichedClass() = runTest {
        val correctClass = EnrichedDetailedClass(
            studentClass = StudentClass(
                id = "testUUID",
                subjectId = "subjectTestUUID",
                title = "TestSubject",
                start = LocalDateTime.parse("2026-09-21 20:40"),
                end = LocalDateTime.parse("2026-09-21 21:00"),
                noteTakingLink = "No notetaking link provided",
                observation = "Obs"
            ),
            subject = Subject(
                id = "subjectTestUUID",
                plannerId = "plannerTestUUID",
                name = "Test Planner",
                color = colorPallet[0][1],
                start = LocalDateTime.parse("2026-09-21 20:40"),
                end = LocalDateTime.parse("2026-09-21 21:00")
            )
        )

        every {
            repository.getEnrichedDetailedClass("testUUID")
        } returns flowOf(correctClass)

        val enrichedClass = getClassUseCase("testUUID").first()
        assertEquals(correctClass, enrichedClass)
        assertEquals(correctClass.studentClass.id, enrichedClass.studentClass.id)
        assertEquals(correctClass.studentClass.subjectId, enrichedClass.subject.id)
    }

    @Test
    fun notGetAEnrichedClass() = runTest {
        every {
            repository.getEnrichedDetailedClass("testUUID")
        } returns flowOf(null)

        val exception = assertFailsWith<InvalidClassExceptions.ClassNotFound> {
            getClassUseCase("testUUID").first()
        }.message

        assertEquals("The class was not found.", message.toString())
    }
}