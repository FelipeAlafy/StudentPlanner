package net.felipealafy.studentplanner.core.ui.extensions

import net.felipealafy.studentplanner.R
import net.felipealafy.studentplanner.feature_exams.domain.use_case.GradeStyle
import net.felipealafy.studentplanner.feature_exams.domain.use_case.GradeStyle.FROM_A_TO_F
import net.felipealafy.studentplanner.feature_exams.domain.use_case.GradeStyle.FROM_A_TO_F_WITH_E
import net.felipealafy.studentplanner.feature_exams.domain.use_case.GradeStyle.FROM_ZERO_TO_ONE_HUNDRED
import net.felipealafy.studentplanner.feature_exams.domain.use_case.GradeStyle.FROM_ZERO_TO_TEN
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

fun Float.getPercentageFromZeroToOneHundred(): Float {
    if (this <= 1) return this * 100
    return this
}

fun GradeStyle.getValueInDisplayStyle(value: Float) = when (this) {
    FROM_ZERO_TO_ONE_HUNDRED -> value.toInt().toString()
    FROM_ZERO_TO_TEN -> (value * 10).toInt().toString()
    // A: 90-100, B: 80-89, C: 70-79, D: 60-69, F: <60
    FROM_A_TO_F -> when (value.toInt()) {
        in 90..100 -> "A"
        in 80..89 -> "B"
        in 70..79 -> "C"
        in 60..69 -> "D"
        else -> "F"
    }
    // A: 90-100, B: 80-89, C: 70-79, D: 60-69, E: 50-59, F: <50
    FROM_A_TO_F_WITH_E -> when (value.toInt()) {
        in 90..100 -> "A"
        in 80..89 -> "B"
        in 70..79 -> "C"
        in 60..69 -> "D"
        in 50..59 -> "E"
        else -> "F"
    }
}

fun GradeStyle.getGradePhrase(value: Float) = when (this) {
    FROM_ZERO_TO_ONE_HUNDRED -> when (value.toInt()) {
        in 90..100 -> R.string.congratulations
        in 80..89 -> R.string.solid_work
        in 70..79 -> R.string.great
        in 60..69 -> R.string.get_better_grade_next_time
        else -> R.string.you_must_work_on_this_subject
    }
    FROM_ZERO_TO_TEN -> when (value) {
        in 0.9F..1F -> R.string.congratulations
        in 0.8F..0.89F -> R.string.solid_work
        in 0.7F..0.79F -> R.string.great
        in 0.6F..0.69F -> R.string.get_better_grade_next_time
        else -> R.string.you_must_work_on_this_subject
    }
    // A: 90-100, B: 80-89, C: 70-79, D: 60-69, F: <60
    FROM_A_TO_F -> when (value.toInt()) {
        in 90..100 -> R.string.congratulations
        in 80..89 -> R.string.solid_work
        in 70..79 -> R.string.great
        in 60..69 -> R.string.get_better_grade_next_time
        else -> R.string.you_must_work_on_this_subject
    }
    // A: 90-100, B: 80-89, C: 70-79, D: 60-69, E: 50-59, F: <50
    FROM_A_TO_F_WITH_E -> when (value.toInt()) {
        in 90..100 -> R.string.congratulations
        in 80..89 -> R.string.solid_work
        in 70..79 -> R.string.great
        in 60..69 -> R.string.keep_trying
        in 50..59 -> R.string.get_better_grade_next_time
        else -> R.string.you_must_work_on_this_subject
    }
}
fun GradeStyle.getValueInDisplayStyleForAverage(average: Float) = when (this) {
    FROM_ZERO_TO_ONE_HUNDRED -> "%.0f".format(average.getPercentageFromZeroToOneHundred())
    FROM_ZERO_TO_TEN -> "%.2f".format(average.getPercentageFromZeroToOneHundred())
    // A: 90-100, B: 80-89, C: 70-79, D: 60-69, F: <60
    FROM_A_TO_F -> when ((average.getPercentageFromZeroToOneHundred()).toInt()) {
        in 90..100 -> "A"
        in 80..89 -> "B"
        in 70..79 -> "C"
        in 60..69 -> "D"
        else -> "F"
    }
    // A: 90-100, B: 80-89, C: 70-79, D: 60-69, E: 50-59, F: <50
    FROM_A_TO_F_WITH_E -> when ((average.getPercentageFromZeroToOneHundred()).toInt()) {
        in 90..100 -> "A"
        in 80..89 -> "B"
        in 70..79 -> "C"
        in 60..69 -> "D"
        in 50..59 -> "E"
        else -> "F"
    }
}

fun LocalDate.formattedValue() =
    "${this.dayOfMonth}, ${this.month.name.lowercase()}"

fun LocalDateTime.formattedValue(): String =
    this.format(DateTimeFormatter.ofPattern("dd, MMM HH:mm"))?: ""


fun parseToDateTime(
    dateMillis: Long,
    hour: Int,
    minute: Int
): LocalDateTime {
    val date = Instant.ofEpochMilli(dateMillis).atZone(ZoneOffset.UTC).toLocalDate()
    val time = LocalTime.of(hour, minute)
    return LocalDateTime.of(date, time)
}