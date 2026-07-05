package net.felipealafy.studentplanner.feature_planner.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import net.felipealafy.studentplanner.feature_planner.data.local.PlannerDao
import net.felipealafy.studentplanner.feature_planner.data.mapper.toDatabaseEntry
import net.felipealafy.studentplanner.feature_planner.data.mapper.toDomainModel
import net.felipealafy.studentplanner.feature_planner.domain.model.DetailedPlanner
import net.felipealafy.studentplanner.feature_planner.domain.model.Planner
import net.felipealafy.studentplanner.feature_planner.domain.repository.PlannerRepository
import net.felipealafy.studentplanner.feature_subject.data.mapper.toDomainModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlannerRepositoryImpl @Inject constructor(private val dao: PlannerDao): PlannerRepository {
    override fun getAllPlanners(): Flow<List<Planner>> {
        return dao.getAllPlanners().map { it.toDomainModel() }
    }

    override fun getDetailedPlanner(plannerId: String): Flow<DetailedPlanner> {
        return dao.getPlannerWithDetailsFlow(plannerId).map { DetailedPlanner(
            planner = it.planner.toDomainModel(),
            subjects = it.subjects.map { subject -> subject.toDomainModel() }
        ) }
    }

    override suspend fun insert(planner: Planner) {
        dao.insert(plannerTable = planner.toDatabaseEntry())
    }

    override suspend fun update(planner: Planner) {
        dao.update(plannerTable = planner.toDatabaseEntry())
    }

    override suspend fun delete(planner: Planner) {
        dao.delete(plannerTable = planner.toDatabaseEntry())
    }

    override fun getPlannerById(plannerId: String): Flow<Planner> {
        return dao.getPlanner(plannerId = plannerId).map { it.toDomainModel() }
    }
}