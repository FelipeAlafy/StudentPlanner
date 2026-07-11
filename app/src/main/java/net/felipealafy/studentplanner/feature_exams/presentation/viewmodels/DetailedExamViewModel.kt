package net.felipealafy.studentplanner.feature_exams.presentation.viewmodels

import androidx.annotation.StringRes
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import net.felipealafy.studentplanner.R
import net.felipealafy.studentplanner.feature_exams.data.local.Exam
import net.felipealafy.studentplanner.feature_planner.domain.model.Planner
import net.felipealafy.studentplanner.feature_subject.domain.model.Subject
import net.felipealafy.studentplanner.feature_exams.domain.use_case.GetExamUseCase
import net.felipealafy.studentplanner.feature_planner.domain.use_case.GetDetailedPlannerUseCase
import javax.inject.Inject

//data class DetailedExamUiState(
//    val planner: Planner? = null,
//    val subject: Subject? = null,
//    val exam: Exam? = null,
//    var isLoading: Boolean = false
//)

sealed interface DetailedExamUiState {
    data object Loading: DetailedExamUiState
    data class  Error(@param:StringRes val messageResId: Int): DetailedExamUiState
    data class Success(
        val planner: Planner,
        val subject: Subject,
        val exam: Exam
    ): DetailedExamUiState
}

@HiltViewModel
class DetailedExamViewModel @Inject constructor(
    savedStateHandler: SavedStateHandle,
    getDetailedPlannerUseCase: GetDetailedPlannerUseCase,
    getExamUseCase: GetExamUseCase
) : ViewModel() {
    private val plannerId: String = checkNotNull(savedStateHandler["plannerId"])
    private val subjectId: String = checkNotNull(savedStateHandler["subjectId"])
    private val examId: String = checkNotNull(savedStateHandler["examId"])


    val uiState: StateFlow<DetailedExamUiState> = combine(
        getDetailedPlannerUseCase(plannerId),
        getExamUseCase(examId)
    ) { planner, exam ->

        val targetSubject = planner.pureSubjects.find { it.id == subjectId }

        if (targetSubject == null) {
            return@combine DetailedExamUiState.Error(R.string.no_subjects_available_error_message)
        }

        DetailedExamUiState.Success(
            planner = planner.planner,
            subject = targetSubject,
            exam = exam,
        )

    }.catch {
        emit(DetailedExamUiState.Error(R.string.unable_to_load))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = DetailedExamUiState.Loading
    )
}