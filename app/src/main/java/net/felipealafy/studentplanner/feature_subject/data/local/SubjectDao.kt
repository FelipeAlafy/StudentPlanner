package net.felipealafy.studentplanner.feature_subject.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import net.felipealafy.studentplanner.feature_class.data.local.SubjectDatesProjection
import java.time.LocalDateTime

@Dao
interface SubjectDao {
    @Query("SELECT * FROM subject ORDER BY name ASC")
    fun getAllSubjects(): Flow<List<SubjectTable>>

    @Query("SELECT * FROM subject WHERE plannerId = :plannerId ORDER BY name ASC")
    fun getSubjectsOfAPlanner(plannerId: String): Flow<List<SubjectTable>>
    @Query("SELECT * FROM subject WHERE id = :subjectId LIMIT 1")
    fun getSubjectById(subjectId: String): Flow<SubjectTable?>

    @Transaction
    @Query("SELECT * FROM subject WHERE id = :subjectId")
    fun getSubjectWithDetails(subjectId: String): Flow<SubjectWithDetailsTable>

    @Query("SELECT `start`, `end` FROM subject WHERE id = :subjectId")
    fun getSubjectDates(subjectId: String): Flow<SubjectDatesProjection?>
    @Insert
    suspend fun insert(subjectTable: SubjectTable)

    @Update
    suspend fun update(subjectTable: SubjectTable)

    @Delete
    suspend fun delete(subjectTable: SubjectTable)

}