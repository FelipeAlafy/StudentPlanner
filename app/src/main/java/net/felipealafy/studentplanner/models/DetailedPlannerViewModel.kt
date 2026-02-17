package net.felipealafy.studentplanner.models

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import net.felipealafy.studentplanner.datamodels.Planner
import net.felipealafy.studentplanner.repositories.PlannerRepository
import net.felipealafy.studentplanner.repositories.SubjectRepository

data class UiStateDetailedPlanner(
    val planner: Planner? = null,
    val isLoading: Boolean = false
)


class DetailedPlannerViewModel(
    savedStateHandle: SavedStateHandle,
    private val plannerRepository: PlannerRepository,
    private val subjectRepository: SubjectRepository
) : ViewModel() {
    private val plannerId: String = checkNotNull(savedStateHandle["plannerId"])

    private val plannerFlow: Flow<Planner> = flow {
        emit(plannerRepository.getPlannerById(plannerId))
    }.flowOn(Dispatchers.IO)

    private val _uiState = combine(
        plannerFlow,
        subjectRepository.getAllSubjectsOfAPlanner(plannerId)
    ) { planner, subjects ->

        val enrichedPlanner = planner.copy(subjects = subjects.toTypedArray())

        UiStateDetailedPlanner(
            planner = enrichedPlanner,
            isLoading = false
        )

    }.stateIn(
        scope = viewModelScope,
        initialValue = UiStateDetailedPlanner(),
        started = SharingStarted.WhileSubscribed(5000)
    )

    val uiState = _uiState
}