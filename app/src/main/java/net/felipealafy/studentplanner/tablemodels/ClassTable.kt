package net.felipealafy.studentplanner.tablemodels

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.util.UUID

@Entity(tableName = "class",
    foreignKeys = [
        ForeignKey(
            entity = SubjectTable::class,
            parentColumns = ["id"],
            childColumns = ["subjectId"],
            onDelete = CASCADE
        )
    ],
    indices = [Index(value = ["subjectId"])]
)
data class ClassTable(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val subjectId: String,
    val title: String,
    val start: LocalDateTime,
    val end: LocalDateTime,
    val noteTakingLink: String,
    val observation: String
)
