package net.felipealafy.studentplanner.models

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import net.felipealafy.studentplanner.datamodels.Exam
import net.felipealafy.studentplanner.datamodels.Planner
import net.felipealafy.studentplanner.datamodels.Subject
import net.felipealafy.studentplanner.repositories.ExamRepository
import net.felipealafy.studentplanner.repositories.PlannerRepository
import net.felipealafy.studentplanner.repositories.SubjectRepository

data class DetailedExamUiState(
    val planner: Planner? = null,
    val subject: Subject? = null,
    val exam: Exam? = null,
    var isLoading: Boolean = false
)

class DetailedExamViewModel(
    savedStateHandler: SavedStateHandle,
    plannerRepository: PlannerRepository,
    subjectRepository: SubjectRepository,
    examRepository: ExamRepository,
) : ViewModel() {
    private val plannerId: String = checkNotNull(savedStateHandler["plannerId"])
    private val subjectId: String = checkNotNull(savedStateHandler["subjectId"])
    private val examId: String = checkNotNull(savedStateHandler["examId"])

    private val _currentExam = mutableStateOf(
        DetailedExamUiState(
            isLoading = true,
        )
    )

    private val plannerFlow: Flow<Planner?> = flow {
        emit(plannerRepository.getPlannerById(plannerId))
    }.flowOn(Dispatchers.IO)


    val uiState: StateFlow<DetailedExamUiState> = combine(
        plannerFlow,
        subjectRepository.getSubjectById(subjectId),
        examRepository.getExamById(examId)
    ) { planner, subject, exam->

        DetailedExamUiState(
            planner = planner,
            subject = subject.first { it.id == subjectId },
            exam = exam,
            isLoading = false
        )

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = _currentExam.value
    )
}