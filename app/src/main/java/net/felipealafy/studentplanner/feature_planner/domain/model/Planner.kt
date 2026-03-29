package net.felipealafy.studentplanner.feature_planner.domain.model

import net.felipealafy.studentplanner.datamodels.GradeStyle
import net.felipealafy.studentplanner.feature_subject.domain.model.Subject
import java.util.UUID

data class Planner(
    val id: String,
    val name: String,
    var color: Long,
    val minimumGradeToPass: Float,
    val gradeDisplayStyle: GradeStyle = GradeStyle.FROM_ZERO_TO_ONE_HUNDRED
)

