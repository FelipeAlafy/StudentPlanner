package net.felipealafy.studentplanner.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import net.felipealafy.studentplanner.datamodels.Planner
import net.felipealafy.studentplanner.datamodels.StudentClass
import net.felipealafy.studentplanner.repositories.ClassRepository
import net.felipealafy.studentplanner.repositories.PlannerRepository
import java.time.LocalDateTime

data class TodayUiState(
    val selectedPlanner: Planner? = null,
    val allPlanners: List<Planner> = emptyList(),
    val classesToday: List<StudentClass> = emptyList(),
    val isLoading: Boolean = true
)

class TodayModel(
    plannerRepository: PlannerRepository,
    classRepository: ClassRepository
) : ViewModel() {
    private val _selectedPlannerId = MutableStateFlow<String?>(null)

    private val todayDateTime = LocalDateTime.now()
    private val todayStart = todayDateTime.withHour(0).withMinute(0).withSecond(0)
    private val todayEnd = todayDateTime.withHour(23).withMinute(59).withSecond(59)

    val uiState: StateFlow<TodayUiState> = combine(
        plannerRepository.getAllPlanners(),
        classRepository.getClassesByDateTime(todayStart, todayEnd),
        _selectedPlannerId
    ) { planners, classes, selectedId ->

        val activePlanner = if (selectedId != null) {
            planners.find { it.id == selectedId } ?: planners.firstOrNull()
        } else {
            planners.firstOrNull()
        }

        TodayUiState(
            selectedPlanner = activePlanner,
            allPlanners = planners,
            classesToday = classes,
            isLoading = planners.isEmpty() && activePlanner == null
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = TodayUiState()
    )

}