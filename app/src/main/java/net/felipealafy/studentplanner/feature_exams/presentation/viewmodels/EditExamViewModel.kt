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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import net.felipealafy.studentplanner.R
import net.felipealafy.studentplanner.feature_exams.domain.use_case.UpdateExamUseCase
import net.felipealafy.studentplanner.feature_exams.domain.use_case.GradeAToF
import net.felipealafy.studentplanner.feature_exams.domain.use_case.GradeAToFWithE
import net.felipealafy.studentplanner.feature_exams.domain.exception.InvalidExamExceptions
import net.felipealafy.studentplanner.feature_exams.domain.model.ExamParams
import net.felipealafy.studentplanner.feature_exams.domain.use_case.GetExamUseCase
import net.felipealafy.studentplanner.feature_planner.domain.model.DetailedPlanner
import net.felipealafy.studentplanner.feature_planner.domain.use_case.GetDetailedPlannerUseCase
import net.felipealafy.studentplanner.ui.forms.ExamForm
import net.felipealafy.studentplanner.ui.views.parseToDateTime
import javax.inject.Inject

sealed interface EditExamEvents {
    data class ShowError(@param:StringRes val messageResId: Int): EditExamEvents
    data object ExamUpdatedSuccessfully: EditExamEvents
}

sealed interface EditExamUiState {
    data object Loading: EditExamUiState
    data class Error(@param:StringRes val messageResId: Int): EditExamUiState
    data class Success(
        val examForm: ExamForm,
        val detailedPlanner: DetailedPlanner
    ) : EditExamUiState
}

//TODO("For next update, is need to migrate the use_cases shared by more than one component, like GetDetailedPlannerUseCase to the package Core")
@HiltViewModel
class EditExamViewModel @Inject constructor (
    savedStateHandle: SavedStateHandle,
    private val updateExamUseCase: UpdateExamUseCase,
    private val getExamUseCase: GetExamUseCase,
    private val getDetailedPlannerUseCase: GetDetailedPlannerUseCase
): ViewModel() {
    private val plannerId: String = checkNotNull(savedStateHandle["plannerId"])
    private val examId: String = checkNotNull(savedStateHandle["examId"])
    private val _formState = MutableStateFlow<ExamForm?>(null)

    private val _events = MutableSharedFlow<EditExamEvents>()
    val events = _events.asSharedFlow()

    init {
        viewModelScope.launch {
            try {
                val form = getExamUseCase(examId).first()

                _formState.update {
                    ExamForm(
                        id = form.id,
                        name = form.name,
                        grade = form.grade.toString(),
                        gradeWeight = (form.gradeWeight * 100).toInt().toString(),
                        start = form.start,
                        end = form.end,
                        subjectId = form.subjectId
                    )
                }
            } catch (_: Exception) {
                _events.emit(EditExamEvents.ShowError(R.string.exam_load_error))
            }
        }
    }


    val uiState: StateFlow<EditExamUiState> = combine(
        _formState,
        getDetailedPlannerUseCase(plannerId = plannerId)
    ) { formState, detailedPlanner ->
        if (detailedPlanner.subjects.isEmpty()) {
            EditExamUiState.Error(R.string.no_subjects_available_error_message)
        }
        if (formState == null) { EditExamUiState.Loading as EditExamUiState }
        else {
            EditExamUiState.Success(
                examForm = formState,
                detailedPlanner = detailedPlanner
            ) as EditExamUiState
        }
    }.catch {
        emit(EditExamUiState.Error(R.string.exam_load_error))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = EditExamUiState.Loading
    )

    fun updateSubject(subjectId: String) {
        _formState.update { it?.copy(subjectId = subjectId) }
    }

    fun updateName(newName: String) {
        _formState.update { it?.copy(name = newName) }
    }

    fun updateGradeFrom0to100(newGrade: String) {
        _formState.update {
            it?.copy(grade = newGrade)
        }
    }

    fun updateGradeFrom0to10(newGrade: String) {
        _formState.update {
            it?.copy(grade = newGrade)
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

        _formState.update {
            it?.copy(grade = grade.toString())
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

        _formState.update {
            it?.copy(grade = grade.toString())
        }
    }

    fun updateGradeWeight(newGradeWeight: String) {
        _formState.update {
            it?.copy(gradeWeight = newGradeWeight)
        }
    }

    fun updateExam() {
        val currentState = uiState.value
        if (currentState !is EditExamUiState.Success) return

        viewModelScope.launch {
            try {
                val form = currentState.examForm
                val plannerStyle = currentState.detailedPlanner.planner.gradeDisplayStyle

                updateExamUseCase(
                    ExamParams(
                        examId = form.id,
                        subjectId = form.subjectId,
                        name = form.name,
                        gradeString = form.grade,
                        gradeWeightString = form.gradeWeight,
                        start = form.start,
                        end = form.end,
                        gradeStyle = plannerStyle
                    )
                )

                _events.emit(EditExamEvents.ExamUpdatedSuccessfully)
            } catch (e: InvalidExamExceptions) {
                val errorMessageId = when (e) {
                    is InvalidExamExceptions.EmptyName -> R.string.empty_name_error
                    is InvalidExamExceptions.SubjectNotSelected -> R.string.empty_subject_error
                    is InvalidExamExceptions.InvalidDateSelection -> R.string.invalid_date_time_selection_error
                    is InvalidExamExceptions.InvalidGradeFormat -> R.string.insert_number_based_on_grade_style
                    is InvalidExamExceptions.InvalidWeightFormat -> R.string.invalid_grade_input_0_to_100
                    else -> R.string.error_updating_exam // Fallback de segurança
                }

                _events.emit(EditExamEvents.ShowError(errorMessageId))
            }
        }
    }

    fun updateStartDate(dateMillis: Long?, hour: Int, minute: Int) {
        if (dateMillis == null) return
        val newDateTime = parseToDateTime(dateMillis, hour, minute)
        _formState.update { it?.copy(start = newDateTime) }
    }

    fun updateEndDate(dateMillis: Long?, hour: Int, minute: Int) {
        if (dateMillis == null) return
        val newDateTime = parseToDateTime(dateMillis, hour, minute)
        _formState.update { it?.copy(end = newDateTime) }
    }
}