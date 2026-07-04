package net.felipealafy.studentplanner.feature_exams.domain.repository

import kotlinx.coroutines.flow.Flow
import net.felipealafy.studentplanner.feature_exams.data.local.Exam
import java.time.LocalDateTime

interface ExamRepository {
    suspend fun delete(exam: Exam)
    suspend fun insert(exam: Exam)
    suspend fun update(exam: Exam)
    fun getExamById(id: String): Flow<List<Exam>>
    fun getExamsByDateTime(todayStart: LocalDateTime, todayEnd: LocalDateTime): Flow<List<Exam>>
}