package net.felipealafy.studentplanner.feature_planner.data.local

import androidx.room.Embedded
import androidx.room.Relation
import net.felipealafy.studentplanner.feature_subject.data.local.SubjectTable
import net.felipealafy.studentplanner.feature_subject.data.local.SubjectWithDetailsTable

data class PlannerWithDetails (
    @Embedded  val planner: PlannerTable,

    @Relation(
        entity = SubjectTable::class,
        parentColumn = "id",
        entityColumn =  "plannerId"
    )
    val subjects: List<SubjectWithDetailsTable>
)

