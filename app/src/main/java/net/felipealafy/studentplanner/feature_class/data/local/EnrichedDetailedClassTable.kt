package net.felipealafy.studentplanner.feature_class.data.local

import androidx.room.Embedded
import androidx.room.Relation
import net.felipealafy.studentplanner.feature_subject.data.local.SubjectTable

data class EnrichedDetailedClassTable (
    @Embedded val studentClass: ClassTable,

    @Relation(
        parentColumn = "subjectId",
        entityColumn = "id"
    )
    val subject: SubjectTable
)