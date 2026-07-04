package net.felipealafy.studentplanner.feature_exams.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import net.felipealafy.studentplanner.feature_subject.data.local.SubjectTable
import java.time.LocalDateTime
import java.util.UUID

@Entity(tableName = "exam",
    foreignKeys = [
        ForeignKey(
            entity = SubjectTable::class,
            parentColumns = ["id"],
            childColumns = ["subjectId"],
            onDelete = ForeignKey.Companion.CASCADE
        )
    ],
    indices = [
        Index(value = ["subjectId"]),
        Index(value = ["start", "end"])
    ]
)
data class ExamTable(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val subjectId: String,
    val name: String,
    val grade: Float,
    val gradeWeight: Float,
    val start: LocalDateTime,
    val end: LocalDateTime
)