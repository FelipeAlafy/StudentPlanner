package net.felipealafy.studentplanner.planner_feature.domain

import net.felipealafy.studentplanner.feature_planner.domain.repository.PlannerRepository
import org.junit.Before
import org.junit.Test
import io.mockk.mockk
import io.mockk.coVerify
import kotlinx.coroutines.test.runTest
import net.felipealafy.studentplanner.core.ui.theme.colorPallet
import net.felipealafy.studentplanner.feature_exams.domain.use_case.GradeStyle
import net.felipealafy.studentplanner.feature_planner.domain.exception.InvalidPlannerException
import net.felipealafy.studentplanner.feature_planner.domain.model.Planner
import net.felipealafy.studentplanner.feature_planner.domain.use_case.CreatePlannerUseCase
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CreatePlannerUseCaseTest {
    private lateinit var createPlannerUseCase: CreatePlannerUseCase
    private lateinit var repository: PlannerRepository

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)

        createPlannerUseCase = CreatePlannerUseCase(repository)
    }

    @Test
    fun shouldEmitAnErrorWhenNoPlannerNamesWhereGivenTo(): Unit = runTest {
        val exception = assertFailsWith<InvalidPlannerException> {
            createPlannerUseCase(
                name = "",
                color = colorPallet[0][1],
                minimumGradeToPass = 70F,
                gradeDisplayStyle = GradeStyle.FROM_ZERO_TO_ONE_HUNDRED
            )
        }

        assertEquals("The planner name can't be empty.", exception.message)
    }

    @Test
    fun shouldCallTheRepositoryWhenPlannerDataIsValid() = runTest {
        val planner = Planner(
            id = "",
            name = "Testing Planner",
            color = colorPallet[0][1],
            minimumGradeToPass = 70F,
            gradeDisplayStyle = GradeStyle.FROM_ZERO_TO_ONE_HUNDRED
        )

        createPlannerUseCase(
            name = planner.name,
            color = planner.color,
            minimumGradeToPass = planner.minimumGradeToPass,
            gradeDisplayStyle = planner.gradeDisplayStyle
        )

        coVerify(exactly = 1) {
            repository.insert(any())
        }
    }
}