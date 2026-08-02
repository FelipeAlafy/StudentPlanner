package net.felipealafy.studentplanner.feature_class.presentation.viewmodels

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
import net.felipealafy.studentplanner.feature_class.domain.exception.InvalidClassExceptions
import net.felipealafy.studentplanner.feature_class.domain.use_case.GetClassUseCase
import net.felipealafy.studentplanner.feature_class.domain.use_case.UpdateClassParams
import net.felipealafy.studentplanner.feature_class.domain.use_case.UpdateClassUseCase
import net.felipealafy.studentplanner.feature_planner.domain.model.DetailedPlanner
import net.felipealafy.studentplanner.feature_planner.domain.use_case.GetDetailedPlannerUseCase
import net.felipealafy.studentplanner.core.ui.extensions.parseToDateTime
import javax.inject.Inject

sealed interface EditStudentClassEvents {
    data class ShowError(@param:StringRes val messageResId: Int): EditStudentClassEvents
    data object ClassUpdatedSuccessfully: EditStudentClassEvents
}

sealed interface EditStudentClassUiState {
    data object Loading: EditStudentClassUiState
    data class Error(@param:StringRes val messageResId: Int): EditStudentClassUiState
    data class Success(
        val formState: ClassFormState,
        val detailedPlanner: DetailedPlanner
    ) : EditStudentClassUiState
}

@HiltViewModel
class EditStudentClassViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getDetailedPlannerUseCase: GetDetailedPlannerUseCase,
    private val getClassUseCase: GetClassUseCase,
    private val updateClassUseCase: UpdateClassUseCase

): ViewModel() {
    private val plannerId: String = checkNotNull(savedStateHandle["plannerId"])
    private val classId: String = checkNotNull(savedStateHandle["classId"])

    private val _formState = MutableStateFlow<ClassFormState?>(null)
    private val _events = MutableSharedFlow<EditStudentClassEvents>()
    val events = _events.asSharedFlow()

    init {
        viewModelScope.launch {
            try {
                val existingClass = getClassUseCase(classId = classId).first()

                _formState.update {
                    ClassFormState(
                        subjectId = existingClass.studentClass.subjectId,
                        title = existingClass.studentClass.title,
                        noteTakingLink = existingClass.studentClass.noteTakingLink,
                        observation = existingClass.studentClass.observation,
                        start = existingClass.studentClass.start,
                        end = existingClass.studentClass.end
                    )
                }
            } catch (_: Exception) {
                _events.emit(EditStudentClassEvents.ShowError(R.string.class_loading_error))
            }
        }
    }

    private val _uiState: StateFlow<EditStudentClassUiState> = combine(
        _formState,
        getDetailedPlannerUseCase(plannerId)
    ) { formState, detailedPlanner ->

        if (formState == null) {
            EditStudentClassUiState.Loading as EditStudentClassUiState
        } else {
            val subjectColor = detailedPlanner.subjects.first {it.subject.id == formState.subjectId}.subject.color
            EditStudentClassUiState.Success(
                formState = formState.copy(selectedColor = subjectColor),
                detailedPlanner = detailedPlanner
            ) as EditStudentClassUiState
        }
    }.catch {
        emit(EditStudentClassUiState.Error(R.string.class_loading_error))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = EditStudentClassUiState.Loading
    )

    val uiState = _uiState

    fun updateTitle(newTitle: String) {
        _formState.update { it?.copy(title = newTitle) }
    }

    fun updateNoteTakingLink(newLink: String) {
        _formState.update { it?.copy(noteTakingLink = newLink) }
    }

    fun updateAssociatedSubject(newSubjectId: String) {
        _formState.update { it?.copy(subjectId = newSubjectId) }
    }

    fun updateStartDate(dateMillis: Long?, hour: Int, minute: Int) {
        if (dateMillis == null) return
        _formState.update { it?.copy(start = parseToDateTime(dateMillis, hour, minute)) }
    }

    fun updateEndDate(dateMillis: Long?, hour: Int, minute: Int) {
        if (dateMillis == null) return
        _formState.update { it?.copy(end = parseToDateTime(dateMillis, hour, minute)) }
    }

    fun updateObservation(newObs: String) {
        _formState.update { it?.copy(observation = newObs) }
    }

    fun saveStudentClass() {
        val currentForm = _formState.value ?: return
        if (currentForm.isValid) {
            viewModelScope.launch {
                try {
                    updateClassUseCase.invoke(
                        params = UpdateClassParams(
                            classId = classId,
                            title = currentForm.title,
                            subjectId = currentForm.subjectId,
                            start = currentForm.start,
                            end = currentForm.end,
                            noteTakingLink = currentForm.noteTakingLink,
                            observation = currentForm.observation
                        ),
                    )
                    _events.emit(EditStudentClassEvents.ClassUpdatedSuccessfully)
                } catch (e: InvalidClassExceptions){
                    val errorMessageId = when (e) {
                        is InvalidClassExceptions.ClassNotFound -> R.string.class_not_founded_error
                        is InvalidClassExceptions.EmptyName -> R.string.empty_name_error
                        is InvalidClassExceptions.EmptySubject -> R.string.empty_subject_error
                        is InvalidClassExceptions.InvalidDateTime -> R.string.invalid_date_time_selection_error
                    }
                    _events.emit(EditStudentClassEvents.ShowError(errorMessageId))
                }
            }
        }
    }
}