package net.felipealafy.studentplanner.tablemodels

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.util.UUID

@Entity(tableName = "exam",
    foreignKeys = [
        ForeignKey(
            entity = SubjectTable::class,
            parentColumns = ["id"],
            childColumns = ["subjectId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ExamTable(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val subjectId: String,
    val name: String,
    val grade: Float,
    val gradeWeight: Int,
    val start: LocalDateTime,
    val end: LocalDateTime
)
