package net.felipealafy.studentplanner.tablemodels

import androidx.room.Entity
import androidx.room.PrimaryKey
import net.felipealafy.studentplanner.datamodels.GradeStyle
import net.felipealafy.studentplanner.datamodels.Subject
import java.util.UUID

@Entity(tableName = "planner")
data class PlannerTable(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val color: Long,
    val minimumGradeToPass: Float,
    val gradeDisplayStyle: GradeStyle = GradeStyle.FROM_ZERO_TO_ONE_HUNDRED,
)