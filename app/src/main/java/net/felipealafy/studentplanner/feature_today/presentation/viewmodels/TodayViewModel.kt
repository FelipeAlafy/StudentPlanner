package net.felipealafy.studentplanner.feature_today.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import net.felipealafy.studentplanner.feature_planner.domain.model.DetailedPlanner
import net.felipealafy.studentplanner.feature_planner.domain.model.Planner
import net.felipealafy.studentplanner.feature_today.domain.use_case.GetAllPlannersUseCase
import net.felipealafy.studentplanner.feature_today.domain.use_case.GetDailyAgendaUseCase
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import javax.inject.Inject

sealed interface TodayUiState {
    data object Loading : TodayUiState
    data object Empty : TodayUiState

    data class Success(
        val selectedPlanner: DetailedPlanner,
        val allPlanners: List<Planner>,
        val currentDate: LocalDate
    ) : TodayUiState
}

@HiltViewModel
class TodayViewModel @Inject constructor(
    private val getAllPlannersUseCase: GetAllPlannersUseCase,
    private val getDailyAgendaUseCase: GetDailyAgendaUseCase
) : ViewModel() {

    private val _selectedPlannerId = MutableStateFlow<String?>(null)
    private val _selectedDate = MutableStateFlow(LocalDate.now())

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<TodayUiState> = combine(
        getAllPlannersUseCase(),
        _selectedPlannerId,
        _selectedDate
    ) { planners, selectedId, date ->
        Triple(planners, selectedId, date)

    }.flatMapLatest { (planners, selectedId, date) ->
        if (planners.isEmpty()) {
            return@flatMapLatest flowOf(TodayUiState.Empty as TodayUiState)
        }
        val activePlannerId = selectedId ?: planners.first().id
        getDailyAgendaUseCase(activePlannerId, date).map { detailedDailyPlanner ->
            TodayUiState.Success(
                selectedPlanner = detailedDailyPlanner,
                allPlanners = planners,
                currentDate = date
            ) as TodayUiState
        }

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = TodayUiState.Loading
    )

    fun updateTodaySelection(dateMillis: Long?) {
        if (dateMillis == null) return
        val date = Instant.ofEpochMilli(dateMillis).atZone(ZoneOffset.UTC).toLocalDate()
        _selectedDate.update { date }
    }

    fun selectPlanner(plannerId: String) {
        _selectedPlannerId.update { plannerId }
    }
}