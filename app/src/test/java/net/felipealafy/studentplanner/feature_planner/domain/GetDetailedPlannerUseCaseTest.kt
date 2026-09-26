package net.felipealafy.studentplanner.feature_planner.domain

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import net.felipealafy.studentplanner.core.ui.theme.colorPallet
import net.felipealafy.studentplanner.feature_exam.domain.use_case.GradeStyle
import net.felipealafy.studentplanner.feature_planner.domain.exception.InvalidPlannerException
import net.felipealafy.studentplanner.feature_planner.domain.model.DetailedPlanner
import net.felipealafy.studentplanner.feature_planner.domain.model.Planner
import net.felipealafy.studentplanner.feature_planner.domain.repository.PlannerRepository
import net.felipealafy.studentplanner.feature_planner.domain.use_case.GetDetailedPlannerUseCase
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class GetDetailedPlannerUseCaseTest {
    private lateinit var getDetailedPlannerUseCase: GetDetailedPlannerUseCase
    private lateinit var repository: PlannerRepository

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        getDetailedPlannerUseCase = GetDetailedPlannerUseCase(repository)
    }

    @Test
    fun successfullyGetADetailedPlanner() = runTest {
        val correctPlanner = Planner(
            id = "TestPlanner",
            name = "Computer Science",
            color = colorPallet[0][1],
            minimumGradeToPass = 70F,
            gradeDisplayStyle = GradeStyle.FROM_ZERO_TO_ONE_HUNDRED
        )

        val expectedDetailedPlanner =  DetailedPlanner(
            correctPlanner,
            subjects = emptyList()
        )

        every {
            repository.getDetailedPlanner("TestPlanner")
        } returns flowOf(expectedDetailedPlanner)

        val result = getDetailedPlannerUseCase("TestPlanner").first()

        assertEquals(expectedDetailedPlanner, result)
    }

    @Test
    fun unSuccessfullyGetADetailedPlanner() = runTest {

        every {
            repository.getDetailedPlanner("TestPlanner")
        } returns flowOf(null)

       val message = assertFailsWith<InvalidPlannerException>{
           getDetailedPlannerUseCase("TestPlanner").first()
       }.message

        assertEquals("The planner was not found.", message)
    }
}