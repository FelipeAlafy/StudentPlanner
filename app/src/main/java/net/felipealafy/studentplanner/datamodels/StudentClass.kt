package net.felipealafy.studentplanner.datamodels

import java.time.LocalDateTime
import java.util.UUID

data class StudentClass(
    val id: String = UUID.randomUUID().toString(),
    var subjectId: String,
    val title: String,
    val start: LocalDateTime,
    val end: LocalDateTime,
    val noteTakingLink: String,
    val observation: String
)
