package net.felipealafy.studentplanner.feature_class.data.mapper

import net.felipealafy.studentplanner.feature_class.domain.model.StudentClass
import net.felipealafy.studentplanner.feature_class.data.local.ClassTable

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