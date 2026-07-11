package net.felipealafy.studentplanner.feature_subject.presentation.viewmodels

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
import net.felipealafy.studentplanner.core.ui.theme.colorPallet
import net.felipealafy.studentplanner.core.ui.extensions.parseToDateTime
import net.felipealafy.studentplanner.feature_planner.domain.model.DetailedPlanner
import net.felipealafy.studentplanner.feature_planner.domain.use_case.GetDetailedPlannerUseCase
import net.felipealafy.studentplanner.feature_subject.domain.exception.InvalidSubjectExceptions
import net.felipealafy.studentplanner.feature_subject.domain.use_case.CreateSubjectParams
import net.felipealafy.studentplanner.feature_subject.domain.use_case.CreateSubjectUseCase
import java.time.LocalDateTime
import javax.inject.Inject

sealed interface SubjectCreationEvent {
    data class ShowError(@param:StringRes val messageResId: Int) : SubjectCreationEvent
    data object SubjectCreatedSuccessfully : SubjectCreationEvent
}

data class SubjectForm(
    val plannerId: String = "",
    val name: String = "",
    val color: Long = colorPallet[0][1],
    val start: LocalDateTime = LocalDateTime.now(),
    val end: LocalDateTime = LocalDateTime.now().plusMinutes(50)
)

sealed interface SubjectCreationUiState {
    data object Loading : SubjectCreationUiState
    data class Error(@param:StringRes val messageResId: Int) : SubjectCreationUiState
    data class Success(
        val form: SubjectForm,
        val detailedPlanner: DetailedPlanner
    ) : SubjectCreationUiState
}

@HiltViewModel
class SubjectCreationViewModel @Inject constructor(
    val savedStateHandle: SavedStateHandle,
    private val getDetailedPlanner: GetDetailedPlannerUseCase,
    private val createSubjectUseCase: CreateSubjectUseCase
) : ViewModel() {
    private val plannerId: String = checkNotNull(savedStateHandle["plannerId"])

    private val _formState = MutableStateFlow(SubjectForm())

    private val _events = MutableSharedFlow<SubjectCreationEvent>()
    val events = _events.asSharedFlow()

    private var firstCombineIteration: Boolean = false

    private val _uiState: StateFlow<SubjectCreationUiState> =
        combine(
            getDetailedPlanner(plannerId),
            _formState

        ) { detailedPlanner, currentForm ->
            if (detailedPlanner.planner.id.isEmpty()) {
                return@combine SubjectCreationUiState.Error(R.string.planner_not_found)
            }

            if (!firstCombineIteration) {
                _formState.update { it.copy(color = detailedPlanner.planner.color) }
                firstCombineIteration = true
            }

            SubjectCreationUiState.Success(
                form = currentForm,
                detailedPlanner = detailedPlanner
            ) as SubjectCreationUiState

        }.catch {
            emit(SubjectCreationUiState.Error(R.string.unable_to_load))
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SubjectCreationUiState.Loading
        )

    val uiState = _uiState

    fun updateName(newName: String) {
        _formState.update {
            it.copy(name = newName)
        }
    }

    fun updateStartDateTime(dateMillis: Long?, hour: Int, minute: Int) {
        if (dateMillis == null) return
        _formState.update {
            it.copy(start = parseToDateTime(dateMillis, hour, minute))
        }
    }

    fun updateEndDateTime(dateMillis: Long?, hour: Int, minute: Int) {
        if (dateMillis == null) return
        _formState.update {
            it.copy(end = parseToDateTime(dateMillis, hour, minute))
        }
    }

    fun updateColor(newColor: Long) {
        _formState.update {
            it.copy(color = newColor)
        }
    }

    fun saveSubject() {
        val currentState = uiState.value
        if (currentState !is SubjectCreationUiState.Success) return

        viewModelScope.launch {
            try {
                val form = currentState.form
                createSubjectUseCase(
                    params = CreateSubjectParams(
                        plannerId = plannerId,
                        name = form.name,
                        color = form.color,
                        start = form.start,
                        end = form.end
                    )
                )
            } catch (e: Exception) {
                when (e) {
                    is InvalidSubjectExceptions.InvalidDateSelection -> {
                        _events.emit(SubjectCreationEvent.ShowError(R.string.invalid_date_time_selection_error))
                    }
                    is InvalidSubjectExceptions.EmptyName -> {
                        _events.emit(SubjectCreationEvent.ShowError(R.string.empty_name_error))
                    }
                    is InvalidSubjectExceptions.PlannerNotFound -> {
                        _events.emit(SubjectCreationEvent.ShowError(R.string.planner_not_found))
                    }
                }

            }
        }
    }
}