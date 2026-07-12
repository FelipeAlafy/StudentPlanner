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
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import net.felipealafy.studentplanner.R
import net.felipealafy.studentplanner.feature_class.domain.exception.InvalidClassExceptions
import net.felipealafy.studentplanner.feature_class.domain.use_case.CreateClassParams
import net.felipealafy.studentplanner.feature_class.domain.use_case.CreateClassUseCase
import net.felipealafy.studentplanner.feature_planner.domain.model.DetailedPlanner
import net.felipealafy.studentplanner.feature_planner.domain.use_case.GetDetailedPlannerUseCase
import net.felipealafy.studentplanner.core.ui.extensions.parseToDateTime
import net.felipealafy.studentplanner.core.ui.theme.colorPallet
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.logging.Level
import java.util.logging.Logger
import javax.inject.Inject

sealed class StudentClassCreationEvent {
    data class ShowErrorToast(val messageResId: Int): StudentClassCreationEvent()
    data object ClassCreatedSuccessfully: StudentClassCreationEvent()
}

data class  ClassFormState(
    val subjectId: String = "",
    val title: String = "",
    val noteTakingLink: String = "",
    val observation: String = "",
    val start: LocalDateTime = LocalDateTime.now(),
    val end: LocalDateTime = LocalDateTime.now().plusMinutes(50),
    val selectedColor: Long = colorPallet[0][1]
) {
    val isValid: Boolean get() = title.isNotBlank() && subjectId.isNotBlank() && end.isAfter(start)
}

sealed interface StudentClassUiState {
    data object Loading : StudentClassUiState
    data class Error(@param:StringRes val messageResId: Int) : StudentClassUiState
    data class Success(
        val formState: ClassFormState,
        val detailedPlanner: DetailedPlanner
    ) : StudentClassUiState
}

@HiltViewModel
class StudentClassCreationViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getDetailedPlannerUseCase: GetDetailedPlannerUseCase,
    private val createClassUseCase: CreateClassUseCase,
) : ViewModel() {
    private val plannerId: String = checkNotNull(savedStateHandle["plannerId"])
    private val startMillis: Long = savedStateHandle.get<Long>("startMillis")?: -1L
    private val endMillis: Long = savedStateHandle.get<Long>("endMillis")?: -1L

    private val _formState = MutableStateFlow(ClassFormState(
        start = if (startMillis != -1L) {
            Instant.ofEpochMilli(startMillis).atZone(ZoneId.systemDefault()).toLocalDateTime()
        } else {
            LocalDateTime.now()
        },

        end = if (endMillis != -1L) {
            Instant.ofEpochMilli(endMillis).atZone(ZoneId.systemDefault()).toLocalDateTime()
        } else {
            LocalDateTime.now().plusMinutes(50)
        }
    ))
    private val _events = MutableSharedFlow<StudentClassCreationEvent>()
    val events = _events.asSharedFlow()

    private val _uiState: StateFlow<StudentClassUiState> = combine(
        _formState,
        getDetailedPlannerUseCase(plannerId)
    ) { formState, detailedPlanner ->

        val resolvedColor = if (formState.subjectId.isNotBlank()) {
            val subjectColor = detailedPlanner.subjects.firstOrNull {
                it.subject.id == formState.subjectId
            }?.subject?.color

            subjectColor ?: detailedPlanner.planner.color
        } else {
            detailedPlanner.planner.color
        }

        val formForUi = formState.copy(selectedColor = resolvedColor)

        StudentClassUiState.Success(
            formState = formForUi,
            detailedPlanner = detailedPlanner
        ) as StudentClassUiState
    }.catch { e ->
        emit(StudentClassUiState.Error(R.string.no_planners_available))
        Logger.getGlobal().log(Level.WARNING, e.message)
        Logger.getGlobal().log(Level.WARNING, e.cause.toString())
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = StudentClassUiState.Loading
    )

    val uiState = _uiState

    fun updateTitle(newTitle: String) {
        _formState.update {
            it.copy(title = newTitle)
        }
    }

    fun updateNoteTakingLink(newLink: String) {
        _formState.update {
            it.copy(noteTakingLink = newLink)
        }
    }

    fun updateAssociatedSubject(newSubjectId: String) {
        _formState.update {
            it.copy(subjectId = newSubjectId)
        }
    }

    fun updateStartDate(dateMillis: Long?, hour: Int, minute: Int) {
        if (dateMillis == null) return
        _formState.update {
            it.copy(start = parseToDateTime(dateMillis, hour, minute))
        }
    }

    fun updateEndDate(dateMillis: Long?, hour: Int, minute: Int) {
        if (dateMillis == null) return
        _formState.update {
            it.copy(end = parseToDateTime(dateMillis, hour, minute))
        }
    }

    fun updateObservation(newObs: String) {
        _formState.update {
            it.copy(observation = newObs)
        }
    }

    fun saveStudentClass() {
        if (_formState.value.isValid) {
            viewModelScope.launch {
                try {
                    createClassUseCase(
                        params = CreateClassParams(
                            title = _formState.value.title,
                            subjectId = _formState.value.subjectId,
                            start = _formState.value.start,
                            end = _formState.value.end,
                            noteTakingLink = _formState.value.noteTakingLink,
                            observation = _formState.value.observation
                        )
                    )

                    _events.emit(StudentClassCreationEvent.ClassCreatedSuccessfully)
                } catch (e: InvalidClassExceptions) {
                    when (e) {
                        is InvalidClassExceptions.ClassNotFound -> {
                            _events.emit(StudentClassCreationEvent.ShowErrorToast(R.string.class_not_founded_error))
                        }
                        is InvalidClassExceptions.EmptyName -> {
                            _events.emit(StudentClassCreationEvent.ShowErrorToast(R.string.empty_name_error))
                        }
                        is InvalidClassExceptions.EmptySubject -> {
                            _events.emit(StudentClassCreationEvent.ShowErrorToast(R.string.empty_subject_error))
                        }
                        is InvalidClassExceptions.InvalidDateTime -> {
                            _events.emit(StudentClassCreationEvent.ShowErrorToast(R.string.invalid_date_time_selection_error))
                        }
                    }
                }

            }
        }
    }
}