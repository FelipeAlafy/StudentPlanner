package net.felipealafy.studentplanner.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import net.felipealafy.studentplanner.datamodels.GradeStyle
import net.felipealafy.studentplanner.datamodels.Planner
import net.felipealafy.studentplanner.datamodels.StudentClass
import net.felipealafy.studentplanner.datamodels.Subject
import net.felipealafy.studentplanner.repositories.ClassRepository
import net.felipealafy.studentplanner.repositories.PlannerRepository
import net.felipealafy.studentplanner.repositories.SubjectRepository
import net.felipealafy.studentplanner.ui.theme.colorPallet
import java.time.LocalDateTime

data class TodayUiState(
    val selectedPlanner: Planner? = null,
    val allPlanners: List<Planner> = emptyList(),
    val subjects: List<Subject> = emptyList(),
    val isLoading: Boolean = true
)

class TodayViewModel(
    plannerRepository: PlannerRepository,
    subjectRepository: SubjectRepository,
    classRepository: ClassRepository
) : ViewModel() {
    private val _selectedPlannerId = MutableStateFlow<String?>(null)
    private val todayDateTime = LocalDateTime.now()
    private val todayStart = todayDateTime.withHour(0).withMinute(0).withSecond(0)
    private val todayEnd = todayDateTime.withHour(23).withMinute(59).withSecond(59)

    val uiState: StateFlow<TodayUiState> = combine(
        plannerRepository.getAllPlanners(),
        subjectRepository.getAllSubjects(),
        classRepository.getClassesByDateTime(todayStart, todayEnd),
        _selectedPlannerId
    ) { planners, subjects, classes, selectedId ->
        var activePlanner: Planner? = null

        subjects.forEach {
            it.studentClasses = classes.filter { c -> c.subjectId == it.id }.toTypedArray()
        }

        planners.forEach {
            it.subjects = subjects.filter { s -> s.plannerId == it.id }.toTypedArray()
        }

        planners.forEach {
            if (it.id == selectedId) {
                activePlanner = it
            }
        }

        if (activePlanner == null) {
            activePlanner = planners.firstOrNull()
        }


        TodayUiState(
            selectedPlanner = activePlanner,
            allPlanners = planners,
            subjects = subjects,
            isLoading = planners.isEmpty() && activePlanner == null && subjects.isEmpty()
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = TodayUiState()
    )

    fun selectPlanner(plannerId: String) {
        _selectedPlannerId.update {
            plannerId
        }
    }
}