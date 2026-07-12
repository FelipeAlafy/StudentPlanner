package net.felipealafy.studentplanner.feature_stopwatch.presentation.viewmodel

import android.os.SystemClock
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import net.felipealafy.studentplanner.feature_planner.domain.model.Planner
import net.felipealafy.studentplanner.feature_planner.domain.use_case.GetDetailedPlannerUseCase
import net.felipealafy.studentplanner.feature_stopwatch.domain.model.StopwatchEvent
import net.felipealafy.studentplanner.feature_stopwatch.domain.model.StopwatchStates
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

data class StopWatchUiModel(
    val planner: Planner,
    val currentMinutage: String = "00",
    val currentHourage: String = "00",
    val currentSeconds: String = "00",
    val stopwatchState: StopwatchStates
)

sealed interface StopWatchUiState {
    data object Loading: StopWatchUiState
    data class Error(val exception: String): StopWatchUiState
    data class Success(val stopWatchUiModel: StopWatchUiModel): StopWatchUiState
}


@HiltViewModel
class StopWatchViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getDetailedPlannerUseCase: GetDetailedPlannerUseCase
): ViewModel() {
    private val plannerId: String = checkNotNull(savedStateHandle["plannerId"])
    private val _events = MutableSharedFlow<StopwatchEvent>()
    val events = _events.asSharedFlow()

    //StopWatch Controls
    private val _elapsedSeconds = MutableStateFlow(0L)
    private val _elapsedMinutes = MutableStateFlow(0L)
    private val _elapsedHours = MutableStateFlow(0L)
    private val _stopwatchState = MutableStateFlow(StopwatchStates.NotStarted)
    private var timerJob: Job? = null
    private var startTimeMillis = 0L
    private var accumulatedTimeMillis = 0L


    val uiState: StateFlow<StopWatchUiState> = combine(
        getDetailedPlannerUseCase(plannerId),
        _elapsedSeconds,
        _elapsedMinutes,
        _elapsedHours,
        _stopwatchState
    ) { detailedPlanner, elapsedSeconds, elapsedMinutes, elapsedHours, stopwatchState ->
        val model = StopWatchUiModel(
            planner = detailedPlanner.planner,
            currentSeconds = "%02d".format(elapsedSeconds),
            currentHourage = "%02d".format(elapsedHours),
            currentMinutage = "%02d".format(elapsedMinutes),
            stopwatchState = stopwatchState
        )

        StopWatchUiState.Success(model) as StopWatchUiState
    }.catch {
        emit(value = StopWatchUiState.Error("Not able to retrive data from the database."))
    }.stateIn(
        scope = viewModelScope,
        initialValue = StopWatchUiState.Loading,
        started = SharingStarted.WhileSubscribed(5_000)
    )



    fun startStopWatch() {
        _stopwatchState.value = StopwatchStates.Running

        startTimeMillis = SystemClock.elapsedRealtime()

        timerJob = viewModelScope.launch {
            while (isActive) {
                val currentElapsed = accumulatedTimeMillis + (SystemClock.elapsedRealtime() - startTimeMillis)

                val currentSeconds = (currentElapsed / 1_000L) % 60

                val currentMinutes = (currentElapsed / 60_000L) % 60

                val currentHours = (currentElapsed / 3_600_000L)

                if(_elapsedSeconds.value != currentSeconds) {
                    _elapsedSeconds.value = currentSeconds
                }

                if (_elapsedMinutes.value != currentMinutes) {
                    _elapsedMinutes.value = currentMinutes
                }

                if (_elapsedHours.value != currentHours) {
                    _elapsedHours.value = currentHours
                }

                delay(500L.milliseconds)
            }
        }
    }

    fun pauseStopWatch() {
        timerJob?.cancel()

        accumulatedTimeMillis += SystemClock.elapsedRealtime() - startTimeMillis
        _stopwatchState.value = StopwatchStates.Paused
    }

    fun finishStopWatch() {
        if (_stopwatchState.value == StopwatchStates.Running) pauseStopWatch()
        _stopwatchState.value = StopwatchStates.Finished
    }

    fun resetStopwatch() {
        timerJob?.cancel()
        accumulatedTimeMillis = 0L
        _elapsedSeconds.value = 0L
        _elapsedMinutes.value = 0L
        _elapsedHours.value = 0L
        _stopwatchState.value = StopwatchStates.NotStarted
    }

    fun addTimeToStudentClass() {
        viewModelScope.launch {
            val (startMillis, endMillis) = calculateStartAndEndTimes()
            _events.emit(StopwatchEvent.NavigateToCreateClass(plannerId, startMillis, endMillis))
        }
    }

    fun addTimeToExam() {
        viewModelScope.launch {
            val (startMillis, endMillis) = calculateStartAndEndTimes()
            _events.emit(StopwatchEvent.NavigateToExam(plannerId, startMillis, endMillis))
        }
    }

    private fun calculateStartAndEndTimes(): Pair<Long, Long> {
        val endMillis = System.currentTimeMillis()

        val finalElapsed = if (_stopwatchState.value == StopwatchStates.Running) {
            accumulatedTimeMillis + (SystemClock.elapsedRealtime() - startTimeMillis)
        } else {
            accumulatedTimeMillis
        }

        val startMillis = endMillis - finalElapsed

        return Pair(startMillis, endMillis)
    }
}