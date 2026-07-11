package net.felipealafy.studentplanner.feature_class.domain.model

import net.felipealafy.studentplanner.feature_subject.domain.model.Subject

data class EnrichedDetailedClass (
    val studentClass: StudentClass,
    val subject: Subject
) {

}