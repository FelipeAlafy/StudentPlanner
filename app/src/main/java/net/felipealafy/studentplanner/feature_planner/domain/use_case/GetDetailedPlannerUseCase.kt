package net.felipealafy.studentplanner.feature_planner.domain.use_case

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import net.felipealafy.studentplanner.feature_planner.domain.exception.InvalidPlannerException
import net.felipealafy.studentplanner.feature_planner.domain.model.DetailedPlanner
import net.felipealafy.studentplanner.feature_planner.domain.repository.PlannerRepository
import javax.inject.Inject

class GetDetailedPlannerUseCase @Inject constructor(
    private val repository: PlannerRepository
) {
    operator fun invoke(plannerId: String): Flow<DetailedPlanner> {
        return repository.getDetailedPlanner(plannerId).map {
            it ?: throw InvalidPlannerException.PlannerNotFound()
        }
    }
}