package net.felipealafy.studentplanner.feature_planner.domain.use_case

import net.felipealafy.studentplanner.datamodels.GradeStyle
import net.felipealafy.studentplanner.feature_planner.domain.exception.InvalidPlannerException
import net.felipealafy.studentplanner.feature_planner.domain.model.Planner
import net.felipealafy.studentplanner.feature_planner.domain.repository.PlannerRepository
import java.util.UUID
import javax.inject.Inject
import kotlin.jvm.Throws

class CreatePlannerUseCase @Inject constructor(
    private val plannerRepository: PlannerRepository
) {
    @Throws(InvalidPlannerException::class)
    suspend operator fun invoke(
        name: String,
        color: Long,
        minimumGradeToPass: Float,
        gradeDisplayStyle: GradeStyle
    ) {
        if (name.isBlank()) {
            throw InvalidPlannerException.EmptyName()
        }
        if (minimumGradeToPass !in 0.0..100.0) {
            throw InvalidPlannerException.InvalidMinimumGradeToPass()
        }

        val newPlanner = Planner(
            id = UUID.randomUUID().toString(),
            name = name,
            color = color,
            minimumGradeToPass = minimumGradeToPass,
            gradeDisplayStyle = gradeDisplayStyle
        )

        plannerRepository.insert(newPlanner)
    }
}