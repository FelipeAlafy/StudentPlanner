package net.felipealafy.studentplanner.feature_subject.data.local

import androidx.room.Embedded
import androidx.room.Relation
import net.felipealafy.studentplanner.tablemodels.ClassTable
import net.felipealafy.studentplanner.tablemodels.ExamTable

data class SubjectWithDetailsTable (
    @Embedded val subject: SubjectTable,

    @Relation(
        parentColumn = "id",
        entityColumn = "subjectId"
    )
    val exams: List<ExamTable>,

    @Relation(
        parentColumn = "id",
        entityColumn = "subjectId"
    )
    val classes: List<ClassTable>
)

