package net.felipealafy.studentplanner.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import net.felipealafy.studentplanner.database.ClassDao
import net.felipealafy.studentplanner.datamodels.StudentClass
import net.felipealafy.studentplanner.mappers.toDatabaseEntity
import net.felipealafy.studentplanner.mappers.toDomainModel
import net.felipealafy.studentplanner.tablemodels.ClassTable
import java.time.LocalDateTime

class ClassRepository(private val dao: ClassDao) {
    fun getAllClasses() : Flow<List<StudentClass>> =
        dao.getAllClasses().map { listTable ->
            listTable.toDomainModel()
        }

    fun getClassesBySubjectId(subjectId: String): Flow<List<StudentClass>> {
        return dao.getClassesBySubjectId(subjectId = subjectId).map {
            it.toDomainModel()
        }
    }

    fun getClassById(id: String): Flow<StudentClass> {
        return dao.getClassById(id).map {
            it.toDomainModel().first()
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

    suspend fun StudentClass.update(data: StudentClass) {
        val updateStudentClassCommit = data.copy(
            id = this.id
        )
        val entity: ClassTable = updateStudentClassCommit.toDatabaseEntity()
        dao.update(entity)
    }

    suspend fun delete(data: StudentClass) {
        val entity: ClassTable = data.toDatabaseEntity()
        dao.delete(entity)
    }
}