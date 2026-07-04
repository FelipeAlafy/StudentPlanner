package net.felipealafy.studentplanner.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import net.felipealafy.studentplanner.feature_planner.domain.model.Planner
import net.felipealafy.studentplanner.feature_subject.domain.model.Subject
import net.felipealafy.studentplanner.feature_class.data.repository.ClassRepositoryImpl
import net.felipealafy.studentplanner.feature_exams.data.repository.ExamRepository
import net.felipealafy.studentplanner.feature_planner.data.repository.PlannerRepositoryImpl
import net.felipealafy.studentplanner.feature_subject.data.repository.SubjectRepositoryImpl
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneOffset
import javax.inject.Inject

data class TodayUiState(
    val selectedPlanner: Planner? = null,
    val allPlanners: List<Planner> = emptyList(),
    val subjects: List<Subject> = emptyList(),
    val currentDate: LocalDate = LocalDate.now(),
    val isLoading: Boolean = true
)

@HiltViewModel
class TodayViewModel @Inject constructor(
    plannerRepositoryImpl: PlannerRepositoryImpl,
    subjectRepositoryImpl: SubjectRepositoryImpl,
    classRepositoryImpl: ClassRepositoryImpl,
    examRepository: ExamRepository
) : ViewModel() {
    private val _selectedPlannerId = MutableStateFlow<String?>(null)
    private val _selectedDate = MutableStateFlow(LocalDate.now())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _classesAndExamsFlow = _selectedDate.flatMapLatest { date ->
        val startOfDay = date.atStartOfDay()
        val endOfDay = date.atTime(LocalTime.MAX)

        combine(
            classRepositoryImpl.getClassesByDateTime(startOfDay, endOfDay),
            examRepository.getExamsByDateTime(startOfDay, endOfDay)
        ) { classes, exams ->
            Pair(classes, exams)
        }
    }

    val uiState: StateFlow<TodayUiState> = combine(
        plannerRepositoryImpl.getAllPlanners(),
        subjectRepositoryImpl.getAllSubjects(),
        _classesAndExamsFlow,
        _selectedPlannerId,
        _selectedDate
    ) { planners, rawSubjects, classesAndExams, selectedId, selectedDate ->
        val enrichedSubjects = rawSubjects.map { subject ->
            subject.copy().apply {
                studentClasses = classesAndExams.first.filter { c -> c.subjectId == subject.id }.toTypedArray()
                exams = classesAndExams.second.filter { exam -> exam.subjectId == subject.id }.toTypedArray()
            }
        }

        val enrichedPlanners = planners.map { planner ->
            planner.copy().apply {
                subjects = enrichedSubjects.filter { s -> s.plannerId == planner.id }.toTypedArray()
            }
        }

        var activePlanner: Planner? = enrichedPlanners.find { it.id == selectedId }

        if (activePlanner == null) {
            activePlanner = enrichedPlanners.firstOrNull()
        }


        TodayUiState(
            selectedPlanner = activePlanner,
            allPlanners = enrichedPlanners,
            subjects = enrichedSubjects,
            currentDate = selectedDate,
            isLoading = planners.isEmpty() && activePlanner == null && rawSubjects.isEmpty()
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = TodayUiState()
    )

    fun updateTodaySelection(dateMillis: Long?) {
        if (dateMillis == null) return
        val date = Instant.ofEpochMilli(dateMillis).atZone(ZoneOffset.UTC).toLocalDate()

        _selectedDate.update {
            date
        }
    }

    fun selectPlanner(plannerId: String) {
        _selectedPlannerId.update {
            plannerId
        }
    }
}