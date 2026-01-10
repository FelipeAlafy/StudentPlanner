package net.felipealafy.studentplanner.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import net.felipealafy.studentplanner.datamodels.GradeStyle
import net.felipealafy.studentplanner.datamodels.Planner
import net.felipealafy.studentplanner.repositories.PlannerRepository
import net.felipealafy.studentplanner.ui.theme.colorPallet

class PlannerModel(private val repository: PlannerRepository): ViewModel() {
    private val _planner = MutableStateFlow(Planner(
        name = "",
        color = 0xFFA7C7E7,
        minimumGradeToPass = 70.0F,
        gradeDisplayStyle = GradeStyle.FROM_ZERO_TO_ONE_HUNDRED,
    ))
    private val _minimumGradeToPass = MutableStateFlow("70")
    private val _color = MutableStateFlow(colorPallet[0][1])
    private val _colorDialogState = MutableStateFlow(false)
    private val _toastEvent = MutableSharedFlow<String>()
    private val _gradeStyle = MutableStateFlow(GradeStyle.FROM_ZERO_TO_ONE_HUNDRED)
    val color: StateFlow<Long> = _color
    val colorDialogState: StateFlow<Boolean> = _colorDialogState
    val planner: StateFlow<Planner> = _planner
    val minimumGradeToPass: StateFlow<String> = _minimumGradeToPass
    val toastEvent: SharedFlow<String> = _toastEvent.asSharedFlow()


    fun showColorDialog() {
        _colorDialogState.update {
            true
        }
    }

    fun hideColorDialog() {
        _colorDialogState.update {
            false
        }
    }

    fun selectPlannerColor(color: Long) {
        _planner.update {
            it.copy(color = color)
        }
    }

    fun onNameTextEdit(value: String) {
        _planner.update {
            it.copy(name = value)
        }
    }

    fun onColorChange(color: Long) {
        _planner.update {
            it.copy(color = color)
        }
    }

    fun onMinimumGradeToPassChanged(value: String) {
        //Regex check only numbers with period or full-stops
        val regex = Regex("^(100|[1-9][0-9]?)$")
        if (!regex.matches(value)) {
            viewModelScope.launch {
                _toastEvent.emit("")
            }
            return
        }
        //toast saying the error to the user.
        _minimumGradeToPass.update {
            value
        }
    }

    fun onSelectGradeStyle(value: GradeStyle) {
        _gradeStyle.update {
            value
        }
    }

    private fun getPlanner(): Planner =
        Planner(
            name = _planner.value.name,
            color = _color.value,
            minimumGradeToPass = _minimumGradeToPass.value.toFloatOrNull() ?: 70F,
            gradeDisplayStyle = _gradeStyle.value
        )

    suspend fun create() {
        val planner = getPlanner()
        repository.insert(planner = getPlanner())
    }
}