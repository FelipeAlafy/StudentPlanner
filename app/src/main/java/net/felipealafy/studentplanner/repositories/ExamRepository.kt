package net.felipealafy.studentplanner.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import net.felipealafy.studentplanner.database.ExamDao
import net.felipealafy.studentplanner.datamodels.Exam
import net.felipealafy.studentplanner.mappers.toDatabaseEntry
import net.felipealafy.studentplanner.mappers.toDomainModel

class ExamRepository(private val dao : ExamDao) {
    fun getExams(subjectId: String): Flow<List<Exam>> {
        return dao.getExams(subjectId = subjectId).map { it.toDomainModel() }
    }

    suspend fun insert(exam: Exam) {
        dao.insert(examTable = exam.toDatabaseEntry())
    }

    suspend fun Exam.update(newExam: Exam) {
        val updatedExamCommit = newExam.copy(id = this.id)
        dao.update(updatedExamCommit.toDatabaseEntry())
    }

    suspend fun delete(exam: Exam) {
        dao.delete(examTable = exam.toDatabaseEntry())
    }
}