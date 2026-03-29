package net.felipealafy.studentplanner.feature_planner.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import net.felipealafy.studentplanner.R
import net.felipealafy.studentplanner.datamodels.GradeStyle
import net.felipealafy.studentplanner.feature_planner.data.repository.PlannerRepositoryImpl
import net.felipealafy.studentplanner.feature_planner.domain.exception.InvalidPlannerException
import net.felipealafy.studentplanner.feature_planner.domain.model.Planner
import net.felipealafy.studentplanner.feature_planner.domain.use_case.CreatePlannerUseCase
import net.felipealafy.studentplanner.ui.theme.colorPallet
import javax.inject.Inject


data class PlannerCreationUiState(
    val name: String = "",
    val color: Long = colorPallet[0][1],
    val minimumGradeToPass: String = "70",
    val gradeDisplayStyle: GradeStyle = GradeStyle.FROM_ZERO_TO_ONE_HUNDRED,
    val isColorDialogVisible: Boolean = false,
    val isSaving: Boolean = false
)

sealed interface PlannerCreationEvent {
    data class ShowErrorToast(val messageResId: Int): PlannerCreationEvent
    data object PlannerCreatedSuccessfully: PlannerCreationEvent
}

@HiltViewModel
class PlannerCreationViewModel @Inject constructor(
    private val createPlannerUseCase: CreatePlannerUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(PlannerCreationUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<PlannerCreationEvent>()
    val events = _events.asSharedFlow()

    fun showColorDialog() {
        _uiState.update {
            it.copy(isColorDialogVisible = true)
        }
    }

    fun hideColorDialog() {
        _uiState.update {
            it.copy(isColorDialogVisible = false)
        }
    }

    fun onNameTextEdit(value: String) {
        _uiState.update {
            it.copy(name = value)
        }
    }

    fun onColorChange(color: Long) {
        _uiState.update {
            it.copy(color = color)
        }
    }

    fun onMinimumGradeToPassChanged(value: String) {
        val regex = Regex("^(100|[1-9][0-9]?)$")
        if (value.isEmpty() || regex.matches(value)) {
            _uiState.update {
                it.copy(minimumGradeToPass = value)
            }
        } else {
            viewModelScope.launch {
                _events.emit(PlannerCreationEvent.ShowErrorToast(R.string.invalid_grade_input_0_to_100))
            }
        }
    }

    fun onSelectGradeStyle(value: GradeStyle) {
        _uiState.update {
            it.copy(gradeDisplayStyle = value)
        }
    }

    fun create() {

        viewModelScope.launch {
            _uiState.update {
                it.copy(isSaving = true)
            }

            try {
                val gradeFloat = _uiState.value.minimumGradeToPass.toFloatOrNull()?: 0F

                createPlannerUseCase(
                    name = _uiState.value.name,
                    color = _uiState.value.color,
                    minimumGradeToPass = gradeFloat,
                    gradeDisplayStyle = _uiState.value.gradeDisplayStyle,
                )

                _events.emit(PlannerCreationEvent.PlannerCreatedSuccessfully)
            } catch (e: InvalidPlannerException) {
                val resException = when(e) {
                    is InvalidPlannerException.EmptyName -> {
                        R.string.empty_name_error
                    }
                    is InvalidPlannerException.InvalidMinimumGradeToPass -> {
                        R.string.invalid_grade_input_0_to_100
                    }
                    is InvalidPlannerException.PlannerNotFound -> {
                        R.string.planner_not_found
                    }
                }
                _events.emit(PlannerCreationEvent.ShowErrorToast(resException))
            } finally {
                _uiState.update {
                    it.copy(isSaving = false)
                }
            }
        }
    }
}