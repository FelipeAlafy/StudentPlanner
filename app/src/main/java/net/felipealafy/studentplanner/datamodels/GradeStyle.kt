package net.felipealafy.studentplanner.datamodels

import net.felipealafy.studentplanner.R

enum class GradeStyle {
    FROM_ZERO_TO_ONE_HUNDRED,
    FROM_ZERO_TO_TEN,
    FROM_A_TO_F,
    FROM_A_TO_F_WITH_E
}

fun GradeStyle.getResourceLocation(): Int {
    return when (this) {
        GradeStyle.FROM_ZERO_TO_ONE_HUNDRED -> R.string.from_zero_to_one_hundred
        GradeStyle.FROM_ZERO_TO_TEN -> R.string.from_zero_to_ten
        GradeStyle.FROM_A_TO_F -> R.string.from_a_to_f
        GradeStyle.FROM_A_TO_F_WITH_E -> R.string.from_a_to_f_with_e
    }
}