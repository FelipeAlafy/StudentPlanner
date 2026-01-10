package net.felipealafy.studentplanner.datamodels

import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.util.UUID

data class Exam(
    val id: String = UUID.randomUUID().toString(),
    val subjectId: String,
    val name: String,
    val grade: Float,
    val gradeWeight: Int,
    val start: LocalDateTime,
    val end: LocalDateTime,
)
