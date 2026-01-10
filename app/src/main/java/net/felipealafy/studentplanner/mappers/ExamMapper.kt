package net.felipealafy.studentplanner.mappers

import net.felipealafy.studentplanner.datamodels.Exam
import net.felipealafy.studentplanner.tablemodels.ExamTable

fun ExamTable.toDomainModel(): Exam {
    return Exam(
        id = this.id,
        subjectId = this.subjectId,
        name = this.name,
        grade = this.grade,
        gradeWeight = this.gradeWeight,
        start = this.start,
        end = this.end
    )
}

fun Exam.toDatabaseEntry(): ExamTable {
    return ExamTable(
        id = this.id,
        subjectId = this.subjectId,
        name = this.name,
        grade = this.grade,
        gradeWeight = this.gradeWeight,
        start = this.start,
        end = this.end
    )
}

fun List<ExamTable>.toDomainModel(): List<Exam> =
    this.map { it.toDomainModel() }

fun Array<Exam>.toDatabaseEntries(): Array<ExamTable> =
    this.map { it.toDatabaseEntry() }.toTypedArray()