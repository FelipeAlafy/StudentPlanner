package net.felipealafy.studentplanner.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import net.felipealafy.studentplanner.database.ClassDao
import net.felipealafy.studentplanner.datamodels.StudentClass
import net.felipealafy.studentplanner.mappers.toDatabaseEntity
import net.felipealafy.studentplanner.mappers.toDomainModel
import net.felipealafy.studentplanner.tablemodels.ClassTable
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClassRepository @Inject constructor(private val dao: ClassDao) {
    fun getAllClassesOfAPlanner(plannerId: String): Flow<List<StudentClass>> {
        return dao.getAllClassesOfAPlanner(plannerId).map {
            it.toDomainModel()
        }
    }

    fun getClassesBySubjectId(subjectId: String): Flow<List<StudentClass>> {
        return dao.getClassesBySubjectId(subjectId = subjectId).map {
            it.toDomainModel()
        }
    }

    fun getClassById(id: String): Flow<List<StudentClass>> {
        return dao.getClassById(id).map {
            it.toDomainModel()
        }
    }

    fun getClassesByDateTime(start: LocalDateTime, end: LocalDateTime): Flow<List<StudentClass>> =
        dao.getClassesByDateTime(start = start, end = end).map { listTable ->
            listTable.toDomainModel()
        }

    suspend fun insert(data: StudentClass) {
        val entity: ClassTable = data.toDatabaseEntity()
        dao.insert(entity)
    }

    suspend fun delete(data: StudentClass) {
        val entity: ClassTable = data.toDatabaseEntity()
        dao.delete(entity)
    }

    suspend fun update(data: StudentClass) {
        val entity: ClassTable = data.toDatabaseEntity()
        dao.update(entity)
    }
}