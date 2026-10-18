package net.felipealafy.studentplanner.feature_class.data.local

import java.time.LocalDateTime

data class SubjectDatesProjection(
    val start: LocalDateTime,
    val end: LocalDateTime
)
