package net.felipealafy.studentplanner.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import net.felipealafy.studentplanner.database.SubjectDao
import net.felipealafy.studentplanner.datamodels.Subject
import net.felipealafy.studentplanner.mappers.toDatabaseEntry
import net.felipealafy.studentplanner.mappers.toDomainModel
import net.felipealafy.studentplanner.tablemodels.SubjectTable

class SubjectRepository (private val dao: SubjectDao) {

    fun getAllSubjects(): Flow<List<Subject>> = dao.getAllSubjects().map { it.toDomainModel() }
    fun getAllSubjectsOfAPlanner(plannerId: String): Flow<List<Subject>> =
        dao.getSubjectsOfAPlanner(plannerId = plannerId).map {
        it.toDomainModel()
    }

    suspend fun insert(subject: Subject) {
        dao.insert(subjectTable = subject.toDatabaseEntry())
    }

    suspend fun Subject.update(newSubject: Subject) {
        val updateSubject = newSubject.copy(id = this.id)
        dao.update(subjectTable = updateSubject.toDatabaseEntry())
    }

    suspend fun delete(subject: Subject) {
        dao.delete(subjectTable = subject.toDatabaseEntry())
    }

    fun getSubjectById(subjectId: String): Flow<List<Subject>> {
        return dao.getSubjectById(subjectId).map {
            it.toDomainModel()
        }
    }
}