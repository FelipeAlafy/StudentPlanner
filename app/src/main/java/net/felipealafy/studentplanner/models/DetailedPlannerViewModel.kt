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
import net.felipealafy.studentplanner.repositories.ClassRepository
import net.felipealafy.studentplanner.repositories.ExamRepository
import net.felipealafy.studentplanner.repositories.PlannerRepository
import net.felipealafy.studentplanner.repositories.SubjectRepository
import net.felipealafy.studentplanner.ui.views.getExamsAverage

data class UiStateDetailedPlanner(
    val planner: Planner? = null,
    val plannerProgress: Float = 0F,
    val isLoading: Boolean = false
)


class DetailedPlannerViewModel(
    savedStateHandle: SavedStateHandle,
    private val plannerRepository: PlannerRepository,
    private val subjectRepository: SubjectRepository,
    private val classRepository: ClassRepository,
    private val examRepository: ExamRepository
) : ViewModel() {
    private val plannerId: String = checkNotNull(savedStateHandle["plannerId"])

    private val plannerFlow: Flow<Planner> = flow {
        emit(plannerRepository.getPlannerById(plannerId))
    }.flowOn(Dispatchers.IO)

    private val _uiState = combine(
        plannerFlow,
        subjectRepository.getAllSubjectsOfAPlanner(plannerId),
        classRepository.getAllClassesOfAPlanner(plannerId),
        examRepository.getAllExamsOfAPlanner(plannerId)
    ) { planner, subjects, classes, exams ->

        subjects.forEach { subject ->
            subject.studentClasses = classes.filter { it.subjectId == subject.id }.toTypedArray()
            subject.exams = exams.filter { it.subjectId == subject.id }.toTypedArray()
        }

        val enrichedPlanner = planner.copy(subjects = subjects.toTypedArray())

        var totalSubjects = subjects.size
        if (totalSubjects == 0) totalSubjects = 1

        val passedSubjectsCount = subjects.count { subject ->
            val subjectFinalGrade = subject.exams.sumOf {
                (it.grade * it.gradeWeight).toDouble()
            }.toFloat()
            subjectFinalGrade >= planner.minimumGradeToPass
        }
        val plannerProgress = passedSubjectsCount.toFloat() / totalSubjects



        UiStateDetailedPlanner(
            planner = enrichedPlanner,
            plannerProgress = plannerProgress,
            isLoading = false
        )

    }.stateIn(
        scope = viewModelScope,
        initialValue = UiStateDetailedPlanner(),
        started = SharingStarted.WhileSubscribed(5000)
    )

    val uiState = _uiState
}