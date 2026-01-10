package net.felipealafy.studentplanner.mappers

import net.felipealafy.studentplanner.datamodels.Exam
import net.felipealafy.studentplanner.datamodels.StudentClass
import net.felipealafy.studentplanner.datamodels.Subject
import net.felipealafy.studentplanner.tablemodels.SubjectTable

fun SubjectTable.toDomainModel(): Subject {
    return Subject(
        id = this.id,
        plannerId = this.plannerId,
        name = this.name,
        color = this.color,
        start = this.start,
        end = this.end,
        studentClasses = emptyArray<StudentClass>(),
        exams = emptyArray<Exam>(),
    )
}

fun Subject.toDatabaseEntry(): SubjectTable {
    return SubjectTable(
        id = this.id,
        plannerId = this.plannerId,
        name = this.name,
        color = this.color,
        start = this.start,
        end = this.end,
    )
}

fun List<SubjectTable>.toDomainModel(): List<Subject> {
    return this.map { it.toDomainModel() }
}

fun Array<Subject>.toDatabaseEntries() : Array<SubjectTable> =
    this.map { it.toDatabaseEntry() }.toTypedArray()