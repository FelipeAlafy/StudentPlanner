package net.felipealafy.studentplanner.feature_subject.data.mapper

import net.felipealafy.studentplanner.feature_class.data.mapper.toDomainModel
import net.felipealafy.studentplanner.feature_subject.data.local.SubjectWithDetailsTable
import net.felipealafy.studentplanner.feature_subject.domain.model.DetailedSubject
import net.felipealafy.studentplanner.feature_exams.data.mapper.toDomainModel

fun SubjectWithDetailsTable.toDomainModel(): DetailedSubject {
    val domainSubject = this.subject.toDomainModel()

    val domainClasses = this.classes.map { it.toDomainModel() }
    val domainExams = this.exams.map { it.toDomainModel() }

    return DetailedSubject(
        subject = domainSubject,
        studentClasses = domainClasses,
        exams = domainExams
    )
}