package net.felipealafy.studentplanner.feature_planner.presentation.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import net.felipealafy.studentplanner.feature_planner.domain.model.DetailedPlanner
import net.felipealafy.studentplanner.feature_planner.domain.use_case.GetDetailedPlannerUseCase
import javax.inject.Inject

sealed interface DetailedPlannerUiState {
    data object Loading: DetailedPlannerUiState
    data class Success(val planner: DetailedPlanner): DetailedPlannerUiState
    data class Error(val exception: String): DetailedPlannerUiState
}

data class SubjectUiModel(
    val id: String,
    val name: String,
    val startDateFormatted: String,
    val endDateFormatted: String,
    val averageGradeFormatted: String,
    val isApproved: Boolean
)

@HiltViewModel
class DetailedPlannerViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getDetailedPlannerUseCase: GetDetailedPlannerUseCase
) : ViewModel() {
    private val plannerId: String = checkNotNull(savedStateHandle["plannerId"])

    val uiState: StateFlow<DetailedPlannerUiState> = getDetailedPlannerUseCase(plannerId).map { planner ->
        DetailedPlannerUiState.Success(planner) as DetailedPlannerUiState
    }.catch { error ->
        emit(DetailedPlannerUiState.Error(exception = error.message ?: "Not able to retrieve information from SQLite."))
    }.stateIn(
        scope = viewModelScope,
        initialValue = DetailedPlannerUiState.Loading,
        started = SharingStarted.WhileSubscribed(5000)
    )
}