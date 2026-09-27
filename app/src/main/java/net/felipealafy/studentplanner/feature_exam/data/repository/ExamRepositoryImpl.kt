package net.felipealafy.studentplanner.feature_exam.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import net.felipealafy.studentplanner.feature_exam.data.local.ExamDao
import net.felipealafy.studentplanner.feature_exam.data.local.Exam
import net.felipealafy.studentplanner.feature_exam.data.mapper.toDatabaseEntry
import net.felipealafy.studentplanner.feature_exam.data.mapper.toDomainModel
import net.felipealafy.studentplanner.feature_exam.domain.repository.ExamRepository
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.jvm.Throws

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

    override suspend fun delete(examId: String): Int {
        return dao.delete(examId = examId)
    }
}