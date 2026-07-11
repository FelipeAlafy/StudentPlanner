package net.felipealafy.studentplanner.feature_class.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import net.felipealafy.studentplanner.feature_class.data.local.ClassDao
import net.felipealafy.studentplanner.feature_class.domain.model.StudentClass
import net.felipealafy.studentplanner.feature_class.data.mapper.toDatabaseEntity
import net.felipealafy.studentplanner.feature_class.data.mapper.toDomainModel
import net.felipealafy.studentplanner.feature_class.data.local.ClassTable
import net.felipealafy.studentplanner.feature_class.domain.model.EnrichedDetailedClass
import net.felipealafy.studentplanner.feature_class.domain.repository.ClassRepository
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClassRepositoryImpl @Inject constructor(private val dao: ClassDao): ClassRepository {
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

    override fun getClassById(classId: String): Flow<List<StudentClass>> {
        return dao.getClassById(classId).map {
            it.toDomainModel()
        }
    }

    override fun getClassesByDateTime(start: LocalDateTime, end: LocalDateTime): Flow<List<StudentClass>> =
        dao.getClassesByDateTime(start = start, end = end).map { listTable ->
            listTable.toDomainModel()
        }

    override fun getEnrichedDetailedClass(classId: String): Flow<EnrichedDetailedClass?> {
        return dao.getEnrichedClass(classId).map { table ->
            table.toDomainModel()
        }
    }

    override suspend fun insert(studentClass: StudentClass) {
        val entity: ClassTable = studentClass.toDatabaseEntity()
        dao.insert(entity)
    }

    override suspend fun delete(studentClass: StudentClass) {
        val entity: ClassTable = studentClass.toDatabaseEntity()
        dao.delete(entity)
    }

    override suspend fun update(studentClass: StudentClass) {
        val entity: ClassTable = studentClass.toDatabaseEntity()
        dao.update(entity)
    }
}