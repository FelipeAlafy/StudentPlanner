package net.felipealafy.studentplanner.feature_today.domain.use_case

import kotlinx.coroutines.flow.Flow
import net.felipealafy.studentplanner.feature_planner.data.repository.PlannerRepositoryImpl
import net.felipealafy.studentplanner.feature_planner.domain.model.Planner
import javax.inject.Inject

class GetAllPlannersUseCase @Inject constructor(
    private val repository: PlannerRepositoryImpl
) {
    operator fun invoke(): Flow<List<Planner>> {
        return repository.getAllPlanners()
    }
}