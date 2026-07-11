package net.felipealafy.studentplanner.feature_subject.domain.use_case

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import net.felipealafy.studentplanner.feature_subject.domain.model.DetailedSubject
import net.felipealafy.studentplanner.feature_subject.domain.repository.SubjectRepository
import javax.inject.Inject

class GetDetailedSubjectUseCase @Inject constructor(
    private val repository: SubjectRepository
) {
    operator fun invoke(subjectId: String): Flow<DetailedSubject> {
        return repository.getSubjectWithDetails(subjectId).map {
            it ?: throw Exception("Subject not found")
        }
    }
}