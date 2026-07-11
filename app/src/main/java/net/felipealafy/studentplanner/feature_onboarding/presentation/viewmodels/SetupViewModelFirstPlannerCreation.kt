package net.felipealafy.studentplanner.feature_onboarding.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import net.felipealafy.studentplanner.R
import net.felipealafy.studentplanner.feature_exams.domain.use_case.GradeStyle
import net.felipealafy.studentplanner.feature_onboarding.domain.model.PlannerForm
import net.felipealafy.studentplanner.feature_onboarding.domain.use_case.CreateFirstPlanner
import net.felipealafy.studentplanner.feature_planner.domain.exception.InvalidPlannerException
import net.felipealafy.studentplanner.feature_planner.presentation.viewmodels.PlannerCreationEvent
import javax.inject.Inject

@HiltViewModel
class SetupViewModelFirstPlannerCreation @Inject constructor(
    private val createFirstPlanner: CreateFirstPlanner
): ViewModel() {
    private val _uiState = MutableStateFlow(PlannerForm())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<PlannerCreationEvent>()
    val events = _events.asSharedFlow()

    fun updateName(newName: String) {
        _uiState.update {
            it.copy(name = newName)
        }
    }

    fun updateColor(newColor: Long) {
        _uiState.update {
            it.copy(color = newColor)
        }
    }

    fun updateMinimumGradeToPassChanged(newMinimumGradeToPass: String) {
        _uiState.update {
            it.copy(minimumGradeToPass = newMinimumGradeToPass)
        }
    }

    fun updateSelectedGradeStyle(newGradeStyle: GradeStyle) {
        _uiState.update {
            it.copy(gradeStyle = newGradeStyle)
        }
    }

    fun create() {
        viewModelScope.launch {
            try {
                createFirstPlanner(
                    uiState.value
                )
            } catch (e: InvalidPlannerException) {
                val resException = when (e) {
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
            }
        }
    }
}