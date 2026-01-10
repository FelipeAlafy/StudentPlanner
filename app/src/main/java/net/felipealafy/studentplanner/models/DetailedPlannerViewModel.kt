package net.felipealafy.studentplanner.models

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import net.felipealafy.studentplanner.datamodels.Planner
import net.felipealafy.studentplanner.repositories.PlannerRepository

class DetailedPlannerViewModel(
    savedStateHandle: SavedStateHandle,
    private val plannerRepository: PlannerRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<Planner?>(null)
    val uiState: StateFlow<Planner?> = _uiState.asSharedFlow() as StateFlow<Planner?>
    init {
        val plannerId: String? = savedStateHandle["plannerId"]
        if (plannerId != null) {
            loadPlanner(plannerId)
        }
    }
    private fun loadPlanner(id: String) {
        viewModelScope.launch {
            val planner = plannerRepository.getPlannerById(id)
            _uiState.update { planner }
        }
    }
}