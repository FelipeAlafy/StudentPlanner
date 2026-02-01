package net.felipealafy.studentplanner.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import net.felipealafy.studentplanner.tablemodels.ExamTable
import java.time.LocalDateTime

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

    @Query("SELECT * FROM exam WHERE start BETWEEN :todayStart AND :todayEnd")
    fun getExamsByDateTime(todayStart: LocalDateTime, todayEnd: LocalDateTime): Flow<List<ExamTable>>

    @Query("SELECT * FROM exam WHERE id = :id")
    fun getExamById(id: String) : Flow<List<ExamTable>>
}