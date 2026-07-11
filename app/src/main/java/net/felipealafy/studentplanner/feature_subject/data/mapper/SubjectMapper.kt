package net.felipealafy.studentplanner.feature_subject.data.mapper

import net.felipealafy.studentplanner.feature_exams.data.local.Exam
import net.felipealafy.studentplanner.feature_class.domain.model.StudentClass
import net.felipealafy.studentplanner.feature_subject.domain.model.Subject
import net.felipealafy.studentplanner.feature_subject.data.local.SubjectTable

fun SubjectTable.toDomainModel(): Subject {
    return Subject(
        id = this.id,
        plannerId = this.plannerId,
        name = this.name,
        color = this.color,
        start = this.start,
        end = this.end
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