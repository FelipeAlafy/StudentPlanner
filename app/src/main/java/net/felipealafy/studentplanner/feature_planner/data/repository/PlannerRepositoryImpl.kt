package net.felipealafy.studentplanner.feature_planner.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import net.felipealafy.studentplanner.feature_planner.data.local.PlannerDao
import net.felipealafy.studentplanner.feature_planner.data.mapper.toDatabaseEntry
import net.felipealafy.studentplanner.feature_planner.data.mapper.toDomainModel
import net.felipealafy.studentplanner.feature_planner.domain.model.Planner
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlannerRepositoryImpl @Inject constructor(private val dao: PlannerDao) {

    fun getAllPlanners() : Flow<List<Planner>> {
        return dao.getAllPlanners().map { it.toDomainModel() }
    }


    fun getPlannerById(plannerId: String): Planner {
        return dao.getPlanner(plannerId = plannerId).toDomainModel()
    }

    suspend fun insert(planner: Planner) {
        dao.insert(plannerTable = planner.toDatabaseEntry())
    }

    suspend fun Planner.update(newPlanner: Planner) {
        val updatedPlannerCommit = newPlanner.copy(id = this.id)
        dao.update(plannerTable = updatedPlannerCommit.toDatabaseEntry())
    }

    suspend fun delete(planner: Planner) {
        dao.delete(plannerTable = planner.toDatabaseEntry())
    }
}