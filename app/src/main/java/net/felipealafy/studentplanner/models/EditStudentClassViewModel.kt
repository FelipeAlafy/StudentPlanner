package net.felipealafy.studentplanner.models

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import net.felipealafy.studentplanner.datamodels.Planner
import net.felipealafy.studentplanner.datamodels.StudentClass
import net.felipealafy.studentplanner.datamodels.Subject
import net.felipealafy.studentplanner.repositories.ClassRepository
import net.felipealafy.studentplanner.repositories.PlannerRepository
import net.felipealafy.studentplanner.repositories.SubjectRepository
import net.felipealafy.studentplanner.ui.views.parseToDateTime
import java.time.LocalDateTime
import java.util.UUID

data class EditStudentClassUiState(
    val currentClassEntry: StudentClass,
    val planner: Planner? = null,
    val availableSubjects: List<Subject> = emptyList(),
    val isEntryValid: Boolean = false,
    val isLoading: Boolean = true
)

class EditStudentClassViewModel(
    savedStateHandle: SavedStateHandle,
    private val classRepository: ClassRepository,
    private val plannerRepository: PlannerRepository,
    subjectsRepository: SubjectRepository
): ViewModel() {
    private val plannerId: String = checkNotNull(savedStateHandle["plannerId"])
    private val classId: String = checkNotNull(savedStateHandle["classId"])

    private val _currentClassEntry = MutableStateFlow(getNewStudentClass().copy(id = classId))

    private var isDataLoaded = false

    private val plannerFlow = flow {
        emit(plannerRepository.getPlannerById(plannerId))
    }.flowOn(Dispatchers.IO)

    init {
        viewModelScope.launch {
            classRepository.getClassById(classId).collect { classes ->
                val foundClass = classes.firstOrNull()
                if (foundClass != null && !isDataLoaded) {
                    _currentClassEntry.update {
                        foundClass
                    }
                    isDataLoaded = true
                }
            }
        }
    }

    private val _uiState: StateFlow<EditStudentClassUiState> = combine(
        _currentClassEntry,
        plannerFlow,
        subjectsRepository.getAllSubjectsOfAPlanner(plannerId)
    ) { entry, currentPlanner, subjects ->

        val isEntryValid = entry.title.isNotEmpty() && entry.subjectId.isNotEmpty()

        val subjectList = subjects.filter {
            it.plannerId == plannerId
        }

        currentPlanner.subjects = subjectList.toTypedArray()

        EditStudentClassUiState(
            planner = currentPlanner,
            currentClassEntry = entry,
            availableSubjects = subjectList,
            isEntryValid = isEntryValid,
            isLoading = !isDataLoaded,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = EditStudentClassUiState(currentClassEntry = _currentClassEntry.value)
    )

    val uiState = _uiState

    fun updateTitle(newTitle: String) {
        _currentClassEntry.update { it.copy(title = newTitle) }
    }

    fun updateNoteTakingLink(newLink: String) {
        _currentClassEntry.update { it.copy(noteTakingLink = newLink) }
    }

    fun updateAssociatedSubject(newSubjectId: String) {
        _currentClassEntry.update { it.copy(subjectId = newSubjectId) }
    }

    fun updateStartDate(dateMillis: Long?, hour: Int, minute: Int) {
        if (dateMillis == null) return
        _currentClassEntry.update { it.copy(start = parseToDateTime(dateMillis, hour, minute)) }
    }

    fun updateEndDate(dateMillis: Long?, hour: Int, minute: Int) {
        if (dateMillis == null) return
        _currentClassEntry.update { it.copy(end = parseToDateTime(dateMillis, hour, minute)) }
    }

    fun updateObservation(newObs: String) {
        _currentClassEntry.update { it.copy(observation = newObs) }
    }

    fun saveStudentClass() {
        if (uiState.value.isEntryValid) {
            viewModelScope.launch {
                classRepository.update(_currentClassEntry.value)
            }
        }
    }

    private fun getNewStudentClass() =
        StudentClass(
            id = UUID.randomUUID().toString(),
            subjectId = "",
            title = "",
            start = LocalDateTime.now(),
            end = LocalDateTime.now(),
            noteTakingLink = "",
            observation = ""
        )
}