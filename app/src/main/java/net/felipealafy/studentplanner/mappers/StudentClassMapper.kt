package net.felipealafy.studentplanner.mappers

import net.felipealafy.studentplanner.datamodels.StudentClass
import net.felipealafy.studentplanner.tablemodels.ClassTable

fun ClassTable.toDomainModel(): StudentClass{
    return StudentClass(
        id = this.id,
        title = this.title,
        subjectId = this.subjectId,
        start = this.start,
        end = this.end,
        noteTakingLink = this.noteTakingLink,
        observation = this.observation,
    )
}

fun StudentClass.toDatabaseEntity(): ClassTable {
    return ClassTable(
        id = this.id,
        title = this.title,
        subjectId = this.subjectId,
        start = this.start,
        end = this.end,
        noteTakingLink = this.noteTakingLink,
        observation = this.observation
    )
}

fun List<ClassTable>.toDomainModel(): List<StudentClass> {
    return map { it.toDomainModel() }
}