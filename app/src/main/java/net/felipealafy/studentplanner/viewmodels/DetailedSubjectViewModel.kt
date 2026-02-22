package net.felipealafy.studentplanner.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import net.felipealafy.studentplanner.datamodels.Exam
import net.felipealafy.studentplanner.datamodels.StudentClass
import net.felipealafy.studentplanner.datamodels.Subject
import net.felipealafy.studentplanner.repositories.ClassRepository
import net.felipealafy.studentplanner.repositories.ExamRepository
import net.felipealafy.studentplanner.repositories.SubjectRepository
import javax.inject.Inject

data class SubjectDetailsUiState(
    val subject: Subject? = null,
    val studentClasses: List<StudentClass> = emptyList(),
    val exams: List<Exam> = emptyList()
)

@HiltViewModel
class DetailedSubjectViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    subjectRepository: SubjectRepository,
    classRepository: ClassRepository,
    examRepository: ExamRepository
): ViewModel() {
    private val subjectId: String = checkNotNull(savedStateHandle["subjectId"])

    val uiState: StateFlow<SubjectDetailsUiState> = combine(
        subjectRepository.getSubjectById(subjectId = subjectId),
        classRepository.getClassesBySubjectId(subjectId = subjectId),
        examRepository.getExams(subjectId = subjectId)
    ) { subject, classes, exams ->
        SubjectDetailsUiState(
            subject = subject.first { it.id == subjectId },
            studentClasses= classes,
            exams = exams
        )

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SubjectDetailsUiState()
    )
}