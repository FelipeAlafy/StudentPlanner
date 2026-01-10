package net.felipealafy.studentplanner.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import net.felipealafy.studentplanner.datamodels.Subject
import net.felipealafy.studentplanner.tablemodels.SubjectTable

@Dao
interface SubjectDao {
    @Query("SELECT * FROM subject ORDER BY name ASC")
    fun getAllSubjects(): Flow<List<SubjectTable>>

    @Query("SELECT * FROM subject WHERE :plannerId ORDER BY name ASC")
    fun getSubjectsOfAPlanner(plannerId: String): Flow<List<SubjectTable>>
    @Query("SELECT * FROM subject WHERE :subjectId LIMIT 1")
    fun getSubjectById(subjectId: String): Flow<List<SubjectTable>>
    @Insert
    suspend fun insert(subjectTable: SubjectTable)

    @Update
    suspend fun update(subjectTable: SubjectTable)

    @Delete
    suspend fun delete(subjectTable: SubjectTable)

}