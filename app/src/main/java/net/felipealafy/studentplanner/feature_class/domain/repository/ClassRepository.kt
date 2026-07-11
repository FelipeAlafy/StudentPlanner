package net.felipealafy.studentplanner.feature_class.domain.repository

import kotlinx.coroutines.flow.Flow
import net.felipealafy.studentplanner.feature_class.domain.model.EnrichedDetailedClass
import net.felipealafy.studentplanner.feature_class.domain.model.StudentClass
import java.time.LocalDateTime

interface ClassRepository {
    suspend fun delete(studentClass: StudentClass)
    suspend fun insert(studentClass: StudentClass)
    suspend fun update(studentClass: StudentClass)
    fun getClassById(classId: String): Flow<List<StudentClass>>
    fun getClassesByDateTime(start: LocalDateTime, end: LocalDateTime): Flow<List<StudentClass>>
    fun getEnrichedDetailedClass(classId: String): Flow<EnrichedDetailedClass?>
}