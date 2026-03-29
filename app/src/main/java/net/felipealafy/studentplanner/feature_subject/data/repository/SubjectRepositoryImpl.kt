package net.felipealafy.studentplanner.feature_subject.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import net.felipealafy.studentplanner.feature_subject.data.local.SubjectDao
import net.felipealafy.studentplanner.feature_subject.data.mapper.toDatabaseEntry
import net.felipealafy.studentplanner.feature_subject.data.mapper.toDomainModel
import net.felipealafy.studentplanner.feature_subject.domain.model.DetailedSubject
import net.felipealafy.studentplanner.feature_subject.domain.model.Subject
import net.felipealafy.studentplanner.feature_subject.domain.repository.SubjectRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubjectRepositoryImpl @Inject constructor(private val dao: SubjectDao) : SubjectRepository {

    override fun getAllSubjects(): Flow<List<Subject>> = dao.getAllSubjects().map { it.toDomainModel() }
    override fun getAllSubjectsOfAPlanner(plannerId: String): Flow<List<Subject>> =
        dao.getSubjectsOfAPlanner(plannerId = plannerId).map {
        it.toDomainModel()
    }

    override suspend fun insert(subject: Subject) {
        dao.insert(subjectTable = subject.toDatabaseEntry())
    }

    override suspend fun update(subject: Subject) {
        dao.update(subjectTable = subject.toDatabaseEntry())
    }

    override suspend fun delete(subject: Subject) {
        dao.delete(subjectTable = subject.toDatabaseEntry())
    }

    override fun getSubjectById(subjectId: String): Flow<List<Subject>> {
        return dao.getSubjectById(subjectId).map {
            it.toDomainModel()
        }
    }

    override fun getSubjectWithDetails(subjectId: String): Flow<DetailedSubject?> {
        return dao.getSubjectWithDetails(subjectId).map {
            it.toDomainModel()
        }
    }
}