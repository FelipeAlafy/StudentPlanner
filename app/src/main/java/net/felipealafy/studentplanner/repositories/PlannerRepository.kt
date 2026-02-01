package net.felipealafy.studentplanner.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import net.felipealafy.studentplanner.database.PlannerDao
import net.felipealafy.studentplanner.datamodels.Planner
import net.felipealafy.studentplanner.mappers.toDatabaseEntry
import net.felipealafy.studentplanner.mappers.toDomainModel

class PlannerRepository(private val dao: PlannerDao) {

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