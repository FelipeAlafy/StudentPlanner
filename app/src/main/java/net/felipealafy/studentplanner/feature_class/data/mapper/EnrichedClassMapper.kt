package net.felipealafy.studentplanner.feature_class.data.mapper

import net.felipealafy.studentplanner.feature_class.data.local.EnrichedDetailedClassTable
import net.felipealafy.studentplanner.feature_class.domain.model.EnrichedDetailedClass
import net.felipealafy.studentplanner.feature_subject.data.mapper.toDomainModel

fun EnrichedDetailedClassTable.toDomainModel(): EnrichedDetailedClass {
    return EnrichedDetailedClass(
        studentClass = this.studentClass.toDomainModel(),
        subject = this.subject.toDomainModel()
    )
}