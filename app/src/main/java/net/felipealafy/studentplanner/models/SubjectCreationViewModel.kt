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
import net.felipealafy.studentplanner.datamodels.Subject
import net.felipealafy.studentplanner.repositories.PlannerRepository
import net.felipealafy.studentplanner.repositories.SubjectRepository
import net.felipealafy.studentplanner.ui.theme.colorPallet
import net.felipealafy.studentplanner.ui.views.parseToDateTime
import java.time.LocalDateTime
import java.util.UUID

data class SubjectCreationUiState(
    val currentSubjectEntry: Subject,
    val planner: Planner? = null,
    val isEntryValid: Boolean = false,
    val isDataLoading: Boolean = true,
    var showingColorChooserDialog: Boolean = false,
    var showingStartDateTimeSelectionDialog: Boolean = false,
    var showingEndDateTimeSelectionDialog: Boolean = false
)

class SubjectCreationViewModel(
    val savedStateHandle: SavedStateHandle,
    private val subjectRepository: SubjectRepository,
    private val plannerRepository: PlannerRepository
) : ViewModel() {
    private val plannerId: String = checkNotNull(savedStateHandle["plannerId"])
    private val _currentSubjectEntry = MutableStateFlow(getNewSubject())
    private val plannerFlow = flow {
        emit(plannerRepository.getPlannerById(plannerId))
    }.flowOn(Dispatchers.IO)

    private fun getNewSubject(): Subject {
        return Subject(
            id = UUID.randomUUID().toString(),
            plannerId = "",
            name = "",
            color = colorPallet[0][1],
            start = LocalDateTime.now(),
            end = LocalDateTime.now(),
        )
    }

    private val _uiState: StateFlow<SubjectCreationUiState> =
        combine(
            _currentSubjectEntry,

            plannerFlow

        ) { currentSubjectEntry, currentPlanner ->

            val isEntryValid =
                currentSubjectEntry.name.isNotEmpty() && currentSubjectEntry.plannerId.isNotEmpty()

            SubjectCreationUiState(
                currentSubjectEntry = currentSubjectEntry,
                planner = currentPlanner,
                isEntryValid = isEntryValid,
                isDataLoading = false,
                showingColorChooserDialog = false
            )

        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SubjectCreationUiState(
                currentSubjectEntry = getNewSubject(),
            )
        )

    val uiState = _uiState

    fun updateName(newName: String) {
        _currentSubjectEntry.update {
            it.copy(name = newName)
        }
    }

    fun updateStartDateTime(dateMillis: Long?, hour: Int, minute: Int) {
        if (dateMillis == null) return
        _currentSubjectEntry.update {
            it.copy(start = parseToDateTime(dateMillis, hour, minute))
        }
    }

    fun updateEndDateTime(dateMillis: Long?, hour: Int, minute: Int) {
        if (dateMillis == null) return
        _currentSubjectEntry.update {
            it.copy(end = parseToDateTime(dateMillis, hour, minute))
        }
    }

    fun updateColor(newColor: Long) {
        _currentSubjectEntry.update {
            it.copy(color = newColor)
        }
    }

    fun saveSubject() {
        if (_currentSubjectEntry.value.plannerId.isEmpty()) {
            _currentSubjectEntry.update {
                it.copy(plannerId = plannerId)
            }
        }
        if (_currentSubjectEntry.value.name.isEmpty()) {
            return
        }
        if (!_uiState.value.isEntryValid) {
            return
        }

        viewModelScope.launch {
            subjectRepository.insert(_currentSubjectEntry.value)
        }
    }
}