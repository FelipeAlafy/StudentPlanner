package net.felipealafy.studentplanner.feature_exams.presentation.viewmodels

import androidx.annotation.StringRes
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import net.felipealafy.studentplanner.R
import net.felipealafy.studentplanner.feature_exams.domain.use_case.GradeAToF
import net.felipealafy.studentplanner.feature_exams.domain.use_case.GradeAToFWithE
import net.felipealafy.studentplanner.feature_exams.domain.exception.InvalidExamExceptions
import net.felipealafy.studentplanner.feature_exams.domain.model.ExamParams
import net.felipealafy.studentplanner.feature_exams.domain.use_case.CreateExamUseCase
import net.felipealafy.studentplanner.feature_planner.domain.model.DetailedPlanner
import net.felipealafy.studentplanner.feature_planner.domain.use_case.GetDetailedPlannerUseCase
import net.felipealafy.studentplanner.feature_exams.domain.model.ExamForm
import net.felipealafy.studentplanner.core.ui.extensions.parseToDateTime
import javax.inject.Inject

sealed interface ExamCreationEvent {
    data class ShowError(@param:StringRes val messageResId: Int) : ExamCreationEvent
    data object ExamCreatedSuccessfully : ExamCreationEvent
}

sealed interface ExamCreationUiState {
    data object Loading : ExamCreationUiState
    data class Error(@param:StringRes val messageResId: Int) : ExamCreationUiState
    data class Success(
        val detailedPlanner: DetailedPlanner,
        val examForm: ExamForm
    ) : ExamCreationUiState
}

@HiltViewModel
class ExamCreationViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getDetailedPlannerUseCase: GetDetailedPlannerUseCase,
    private val createExamUseCase: CreateExamUseCase
) : ViewModel() {
    private val plannerId: String = checkNotNull(savedStateHandle["plannerId"])

    private val _examForm = MutableStateFlow<ExamForm>(ExamForm())

    private val _events = MutableSharedFlow<ExamCreationEvent>()
    val events = _events.asSharedFlow()

    val uiState: StateFlow<ExamCreationUiState> = combine(
        getDetailedPlannerUseCase(plannerId),
        _examForm
    ) { detailedPlanner, examForm ->
        if (detailedPlanner.planner.id.isEmpty()) return@combine ExamCreationUiState.Error(R.string.planner_not_found)
        if (detailedPlanner.subjects.isEmpty()) return@combine ExamCreationUiState.Error(R.string.no_subjects_available_error_message)

        val resolvedColor = if (examForm.subjectId.isNotBlank()) {
            val subjectColor = detailedPlanner.subjects.firstOrNull {
                it.subject.id == examForm.subjectId
            }?.subject?.color

            subjectColor ?: detailedPlanner.planner.color
        } else {
            detailedPlanner.planner.color
        }

        val formForUi = examForm.copy(color = resolvedColor)

        ExamCreationUiState.Success(
            detailedPlanner = detailedPlanner,
            examForm = formForUi
        )
    }.catch {
        emit(ExamCreationUiState.Error(R.string.unable_to_load))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ExamCreationUiState.Loading
    )

    fun updateSubject(subjectId: String) {
        _examForm.update {
            it.copy(subjectId = subjectId)
        }
    }

    fun updateName(newName: String) {
        _examForm.update {
            it.copy(name = newName)
        }
    }

    fun updateGradeFrom0to100(newGrade: String) {
        _examForm.update {
            it.copy(grade = newGrade)
        }
    }

    fun updateGradeFrom0to10(newGrade: String) {
        _examForm.update {
            it.copy(grade = newGrade)
        }
    }

    fun updateGradeFromAToF(newGrade: GradeAToF) {
        val grade = when (newGrade) {
            GradeAToF.A -> 100F
            GradeAToF.B -> 80F
            GradeAToF.C -> 70F
            GradeAToF.D -> 60F
            GradeAToF.F -> 0F
        }

        _examForm.update {
            it.copy(grade = grade.toString())
        }
    }

    fun updateGradeFromAToFWithE(newGrade: GradeAToFWithE) {
        val grade = when (newGrade) {
            GradeAToFWithE.A -> 100F
            GradeAToFWithE.B -> 80F
            GradeAToFWithE.C -> 70F
            GradeAToFWithE.D -> 60F
            GradeAToFWithE.E -> 50F
            GradeAToFWithE.F -> 0F
        }

        _examForm.update {
            it.copy(grade = grade.toString())
        }
    }

    fun updateGradeWeight(newGradeWeight: String) {
        _examForm.update {
            it.copy(gradeWeight = newGradeWeight)
        }
    }

    fun saveExam() {
        val currentState = uiState.value
        if (currentState !is ExamCreationUiState.Success) return


        viewModelScope.launch {
            try {
                val form = currentState.examForm
                val plannerStyle = currentState.detailedPlanner.planner.gradeDisplayStyle

                createExamUseCase(
                    params = ExamParams(
                        examId = "",
                        subjectId = form.subjectId,
                        name = form.name,
                        gradeString = form.grade, // Mandando String!
                        gradeWeightString = form.gradeWeight, // Mandando String!
                        start = form.start,
                        end = form.end,
                        gradeStyle = plannerStyle
                    )
                )
            } catch (e: Exception) {
                val errorMessageId = when (e) {
                    is InvalidExamExceptions.EmptyName -> R.string.empty_name_error
                    is InvalidExamExceptions.SubjectNotSelected -> R.string.empty_subject_error
                    is InvalidExamExceptions.InvalidDateSelection -> R.string.invalid_date_time_selection_error
                    is InvalidExamExceptions.InvalidGradeFormat -> R.string.invalid_number_input
                    is InvalidExamExceptions.InvalidWeightFormat -> R.string.invalid_grade_input_0_to_100
                    else -> R.string.unable_to_load
                }
                _events.emit(ExamCreationEvent.ShowError(errorMessageId))
            }
        }
    }

    fun updateStartDate(dateMillis: Long?, hour: Int, minute: Int) {
        if (dateMillis == null) return
        val newDateTime = parseToDateTime(dateMillis, hour, minute)
        _examForm.update {
            it.copy(start = newDateTime)
        }
    }

    fun updateEndDate(dateMillis: Long?, hour: Int, minute: Int) {
        if (dateMillis == null) return
        val newDateTime = parseToDateTime(dateMillis, hour, minute)
        _examForm.update {
            it.copy(end = newDateTime)
        }
    }
}