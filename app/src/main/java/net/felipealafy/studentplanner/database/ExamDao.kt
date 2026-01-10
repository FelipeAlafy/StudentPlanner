package net.felipealafy.studentplanner.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import net.felipealafy.studentplanner.tablemodels.ExamTable

@Dao
interface ExamDao {
    @Query("SELECT * FROM exam WHERE subjectId = :subjectId")
    fun getExams(subjectId: String): Flow<List<ExamTable>>

    @Insert
    suspend fun insert(examTable: ExamTable)

    @Update
    suspend fun update(examTable: ExamTable)

    @Delete
    suspend fun delete(examTable: ExamTable)
}