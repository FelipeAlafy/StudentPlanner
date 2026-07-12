package net.felipealafy.studentplanner.feature_stopwatch.domain.model

sealed interface StopwatchEvent {
    data class NavigateToCreateClass(
        val plannerId: String,
        val startMillis: Long,
        val endMillis: Long
    ) : StopwatchEvent

    data class NavigateToExam(
        val plannerId: String,
        val startMillis: Long,
        val endMillis: Long
    ): StopwatchEvent
}
