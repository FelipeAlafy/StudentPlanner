package net.felipealafy.studentplanner.feature_subject.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey
import net.felipealafy.studentplanner.feature_planner.data.local.PlannerTable
import java.time.LocalDateTime
import java.util.UUID

@Entity(tableName = "subject",
        foreignKeys = [
            ForeignKey(
                entity = PlannerTable::class,
                parentColumns = ["id"],
                childColumns = ["plannerId"],
                onDelete = CASCADE
            )
        ],
        indices = [Index(value = ["plannerId"])]
    )
data class SubjectTable(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val plannerId: String,
    val name: String,
    val color: Long,
    val start: LocalDateTime,
    val end: LocalDateTime,
)