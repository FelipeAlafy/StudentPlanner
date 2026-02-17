package net.felipealafy.studentplanner.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import net.felipealafy.studentplanner.database.ExamDao
import net.felipealafy.studentplanner.datamodels.Exam
import net.felipealafy.studentplanner.mappers.toDatabaseEntry
import net.felipealafy.studentplanner.mappers.toDomainModel
import java.time.LocalDateTime

class ExamRepository(private val dao : ExamDao) {
    fun getExams(subjectId: String): Flow<List<Exam>> {
        return dao.getExams(subjectId = subjectId).map { it.toDomainModel() }
    }

    fun getExamsByDateTime(todayStart: LocalDateTime, todayEnd: LocalDateTime): Flow<List<Exam>> {
        return dao.getExamsByDateTime(todayStart = todayStart, todayEnd = todayEnd).map { it.toDomainModel() }
    }

    fun getExamById(id: String): Flow<List<Exam>> {
        return dao.getExamById(id = id).map { it.toDomainModel() }
    }

    fun getAllExamsOfAPlanner(plannerId: String): Flow<List<Exam>> {
        return dao.getExamsByPlannerId(plannerId = plannerId).map { it.toDomainModel() }
    }

    suspend fun insert(exam: Exam) {
        dao.insert(examTable = exam.toDatabaseEntry())
    }

    suspend fun update(data: Exam) {
        dao.update(data.toDatabaseEntry())
    }

    suspend fun delete(exam: Exam) {
        dao.delete(examTable = exam.toDatabaseEntry())
    }
}