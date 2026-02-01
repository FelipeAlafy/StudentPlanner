package net.felipealafy.studentplanner.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import net.felipealafy.studentplanner.tablemodels.PlannerTable

@Dao
interface PlannerDao {
    @Query("SELECT * FROM planner ORDER BY NAME ASC")
    fun getAllPlanners(): Flow<List<PlannerTable>>

    @Query("SELECT * FROM planner WHERE id = :plannerId ORDER BY name ASC")
    fun getPlanner(plannerId: String): PlannerTable

    @Insert
    suspend fun insert(plannerTable: PlannerTable)

    @Update
    suspend fun update(plannerTable: PlannerTable)

    @Delete
    suspend fun delete(plannerTable: PlannerTable)
}