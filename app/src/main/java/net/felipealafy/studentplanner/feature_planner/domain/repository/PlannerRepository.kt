package net.felipealafy.studentplanner.feature_planner.domain.repository

import kotlinx.coroutines.flow.Flow
import net.felipealafy.studentplanner.feature_planner.domain.model.DetailedPlanner
import net.felipealafy.studentplanner.feature_planner.domain.model.Planner

interface PlannerRepository {
    fun getAllPlanners(): Flow<List<Planner>>
    fun getDetailedPlanner(plannerId: String): Flow<DetailedPlanner?>
    suspend fun insert(planner: Planner)
    suspend fun update(planner: Planner)
    suspend fun delete(planner: Planner)
}