package net.felipealafy.studentplanner.ui.forms

import java.time.LocalDateTime
import java.util.UUID

data class ExamForm(
    val id: String = UUID.randomUUID().toString(),
    val subjectId: String = "",
    val name: String = "",
    val grade: String = "0",
    val gradeWeight: String = "0",
    val start: LocalDateTime = LocalDateTime.now(),
    val end: LocalDateTime = LocalDateTime.now().plusMinutes(50),
)
