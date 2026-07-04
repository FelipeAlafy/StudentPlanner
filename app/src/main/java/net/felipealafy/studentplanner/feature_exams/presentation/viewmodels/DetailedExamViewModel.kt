package net.felipealafy.studentplanner.feature_exams.presentation.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import net.felipealafy.studentplanner.feature_exams.data.local.Exam
import net.felipealafy.studentplanner.feature_planner.domain.model.Planner
import net.felipealafy.studentplanner.feature_subject.domain.model.Subject
import net.felipealafy.studentplanner.feature_exams.data.repository.ExamRepository
import net.felipealafy.studentplanner.feature_planner.data.repository.PlannerRepositoryImpl
import net.felipealafy.studentplanner.feature_subject.data.repository.SubjectRepositoryImpl
import java.time.LocalDateTime
import javax.inject.Inject

data class DetailedExamUiState(
    val planner: Planner? = null,
    val subject: Subject? = null,
    val exam: Exam? = null,
    var isLoading: Boolean = false
)

@HiltViewModel
class DetailedExamViewModel @Inject constructor(
    savedStateHandler: SavedStateHandle,
    plannerRepositoryImpl: PlannerRepositoryImpl,
    subjectRepositoryImpl: SubjectRepositoryImpl,
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
        emit(plannerRepositoryImpl.getPlannerById(plannerId))
    }.flowOn(Dispatchers.IO)


    val uiState: StateFlow<DetailedExamUiState> = combine(
        plannerFlow,
        subjectRepositoryImpl.getSubjectById(subjectId),
        examRepository.getExamById(examId)
    ) { planner, subject, examRecovered ->

        val exam = examRecovered.firstOrNull()?: Exam(
            subjectId = "",
            name = "Error while loading, reopen the app.",
            grade = 0F,
            gradeWeight = 0F,
            start = LocalDateTime.now(),
            end = LocalDateTime.now()
        )

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