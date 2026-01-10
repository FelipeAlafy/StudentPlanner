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
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.util.UUID

data class StudentClassUiState(
    val currentClassEntry: StudentClass,
    val planner: Planner? = null,
    val availableSubjects: List<Subject> = emptyList(),
    val isEntryValid: Boolean = false,
    val isLoading: Boolean = true
)

class StudentClassViewModel(
    savedStateHandle: SavedStateHandle,
    private val classRepository: ClassRepository,
    private val plannerRepository: PlannerRepository,
    private val subjectsRepository: SubjectRepository
): ViewModel() {
    private val plannerId: String = checkNotNull(savedStateHandle["plannerId"])
    private val _currentClassEntry = MutableStateFlow(getNewStudentClass())

    private val plannerFlow = flow {
        emit(plannerRepository.getPlannerById(plannerId))
    }.flowOn(Dispatchers.IO)

    private val _uiState: StateFlow<StudentClassUiState> = combine(
        subjectsRepository.getAllSubjectsOfAPlanner(plannerId = plannerId),
        _currentClassEntry,
        plannerFlow
    ) {subjects, currentEntry, currentPlanner ->
        val isCurrentEntryValid = currentEntry.title.isNotEmpty() && currentEntry.subjectId.isNotEmpty()
        StudentClassUiState(
            currentClassEntry = currentEntry,
            planner = currentPlanner,
            availableSubjects = subjects,
            isEntryValid = isCurrentEntryValid,
            isLoading = false

        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = StudentClassUiState(currentClassEntry = getNewStudentClass())
    )

    val uiState = _uiState

    fun updateTitle(newTitle: String) {
        _currentClassEntry.update {
            it.copy(title = newTitle)
        }
    }

    fun updateNoteTakingLink(newLink: String) {
        _currentClassEntry.update {
            it.copy(noteTakingLink = newLink)
        }
    }

    fun updateAssociatedSubject(newSubjectId: String) {
        _currentClassEntry.update {
            it.copy(subjectId = newSubjectId)
        }
    }

    fun updateStartDate(dateMillis: Long?, hour: Int, minute: Int) {
        if (dateMillis == null) return
        val date = Instant.ofEpochMilli(dateMillis).atZone(ZoneOffset.UTC).toLocalDate()
        val time = LocalTime.of(hour, minute)
        val dateTime = LocalDateTime.of(date, time)

        _currentClassEntry.update {
            it.copy(start = parseToDateTime(dateMillis, hour, minute))
        }
    }

    fun updateEndDate(dateMillis: Long?, hour: Int, minute: Int) {
        if (dateMillis == null) return
        val date = Instant.ofEpochMilli(dateMillis).atZone(ZoneOffset.UTC).toLocalDate()
        val time = LocalTime.of(hour, minute)
        val dateTime = LocalDateTime.of(date, time)

        _currentClassEntry.update {
            it.copy(start = dateTime)
        }
    }

    fun updateObservation(newObs: String) {
        _currentClassEntry.update {
            it.copy(observation = newObs)
        }
    }

    fun saveStudentClass() {
        if (uiState.value.isEntryValid) {
            viewModelScope.launch {
                classRepository.insert(_currentClassEntry.value)
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