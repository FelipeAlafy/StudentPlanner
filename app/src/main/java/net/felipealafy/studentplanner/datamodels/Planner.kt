package net.felipealafy.studentplanner.datamodels

import java.util.UUID

data class Planner(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    var color: Long,
    val minimumGradeToPass: Float,
    val gradeDisplayStyle: GradeStyle = GradeStyle.FROM_ZERO_TO_ONE_HUNDRED,
)
