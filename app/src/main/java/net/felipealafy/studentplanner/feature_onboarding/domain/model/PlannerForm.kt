package net.felipealafy.studentplanner.feature_onboarding.domain.model

import net.felipealafy.studentplanner.feature_exams.domain.use_case.GradeStyle
import net.felipealafy.studentplanner.core.ui.theme.colorPallet

data class PlannerForm(
    var name: String = "",
    var color: Long = colorPallet[0][1],
    var minimumGradeToPass: String = "",
    var gradeStyle: GradeStyle = GradeStyle.FROM_ZERO_TO_ONE_HUNDRED
)
