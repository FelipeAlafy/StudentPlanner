package net.felipealafy.studentplanner.feature_subject.domain.repository

import kotlinx.coroutines.flow.Flow
import net.felipealafy.studentplanner.feature_subject.domain.model.DetailedSubject
import net.felipealafy.studentplanner.feature_subject.domain.model.Subject

interface SubjectRepository {

    suspend fun insert(subject: Subject)
    suspend fun update(subject: Subject)
    suspend fun delete(subject: Subject)

    fun getAllSubjects(): Flow<List<Subject>>
    fun getSubjectById(subjectId: String): Flow<List<Subject>>
    fun getAllSubjectsOfAPlanner(plannerId: String): Flow<List<Subject>>
    fun getSubjectWithDetails(subjectId: String): Flow<DetailedSubject?>
}