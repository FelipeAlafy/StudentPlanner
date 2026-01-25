package net.felipealafy.studentplanner.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import net.felipealafy.studentplanner.tablemodels.ClassTable
import java.time.LocalDateTime

@Dao
interface ClassDao {
    @Query("SELECT * FROM class ORDER BY title ASC")
    fun getAllClasses(): Flow<List<ClassTable>>

    @Query("SELECT * FROM class WHERE start < :end AND 'end' > :start ORDER BY start ASC")
    fun getClassesByDateTime(start: LocalDateTime, end: LocalDateTime): Flow<List<ClassTable>>

    @Insert
    suspend fun insert(data: ClassTable)

    @Update
    suspend fun update(data: ClassTable)

    @Delete
    suspend fun delete(data: ClassTable)
    @Query("SELECT * FROM class WHERE subjectId = :subjectId ORDER BY start ASC")
    fun getClassesBySubjectId(subjectId: String): Flow<List<ClassTable>>
    @Query("SELECT * FROM class WHERE id = :id")
    fun getClassById(id: String): Flow<List<ClassTable>>
}