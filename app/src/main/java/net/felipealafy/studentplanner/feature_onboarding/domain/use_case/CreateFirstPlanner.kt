package net.felipealafy.studentplanner.feature_onboarding.domain.use_case

import net.felipealafy.studentplanner.feature_exams.domain.use_case.GradeStyle
import net.felipealafy.studentplanner.feature_onboarding.domain.model.PlannerForm
import net.felipealafy.studentplanner.feature_planner.domain.exception.InvalidPlannerException
import net.felipealafy.studentplanner.feature_planner.domain.model.Planner
import net.felipealafy.studentplanner.feature_planner.domain.repository.PlannerRepository
import java.util.UUID
import javax.inject.Inject

class CreateFirstPlanner @Inject constructor(
    private val repository: PlannerRepository
) {
    @Throws
    suspend operator fun invoke(
        form: PlannerForm
    ) {
        val regex = Regex("^(100|[1-9][0-9]?)$")
        with(form) {
            if (name.isBlank()) {
                throw InvalidPlannerException.EmptyName()
            }
            if (regex.matches(minimumGradeToPass)) {
                throw InvalidPlannerException.InvalidMinimumGradeToPass()
            }
            if (minimumGradeToPass.toFloat() !in 0.0 .. 100.0) {
                throw InvalidPlannerException.InvalidMinimumGradeToPass()
            }

            val planner = Planner(
                id = UUID.randomUUID().toString(),
                name = name,
                color = color.toLong(),
                minimumGradeToPass = minimumGradeToPass.toFloat(),
                gradeDisplayStyle = gradeStyle
            )

            repository.insert(planner)
        }
    }
}