package net.felipealafy.studentplanner.feature_exams.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import net.felipealafy.studentplanner.feature_exams.data.local.ExamDao
import net.felipealafy.studentplanner.feature_exams.data.local.Exam
import net.felipealafy.studentplanner.feature_exams.data.mapper.toDatabaseEntry
import net.felipealafy.studentplanner.feature_exams.data.mapper.toDomainModel
import net.felipealafy.studentplanner.feature_exams.domain.repository.ExamRepository
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExamRepositoryImpl @Inject constructor(private val dao : ExamDao): ExamRepository {
    override fun getExamsByDateTime(todayStart: LocalDateTime, todayEnd: LocalDateTime): Flow<List<Exam>> {
        return dao.getExamsByDateTime(todayStart = todayStart, todayEnd = todayEnd).map { it.toDomainModel() }
    }

    override fun getExamById(id: String): Flow<List<Exam>> {
        return dao.getExamById(id = id).map { it.toDomainModel() }
    }

    override suspend fun insert(exam: Exam) {
        dao.insert(examTable = exam.toDatabaseEntry())
    }

    override suspend fun update(exam: Exam) {
        dao.update(exam.toDatabaseEntry())
    }

    override suspend fun delete(exam: Exam) {
        dao.delete(examTable = exam.toDatabaseEntry())
    }
}