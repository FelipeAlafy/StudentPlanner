package net.felipealafy.studentplanner.ui.views

import androidx.compose.ui.graphics.Color
import net.felipealafy.studentplanner.R
import net.felipealafy.studentplanner.datamodels.Exam
import net.felipealafy.studentplanner.datamodels.GradeStyle
import net.felipealafy.studentplanner.datamodels.GradeStyle.FROM_A_TO_F
import net.felipealafy.studentplanner.datamodels.GradeStyle.FROM_A_TO_F_WITH_E
import net.felipealafy.studentplanner.datamodels.GradeStyle.FROM_ZERO_TO_ONE_HUNDRED
import net.felipealafy.studentplanner.datamodels.GradeStyle.FROM_ZERO_TO_TEN
import net.felipealafy.studentplanner.ui.theme.bluePallet
import net.felipealafy.studentplanner.ui.theme.greenPallet
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

fun Float.getPercentageFromZeroToOne(): Float {
    if (this < 1) return this
    return this / 100
}

fun Float.getPercentageFromZeroToOneHundred(): Float {
    if (this <= 1) return this * 100
    return this
}

fun Long.getContrastingColorForProgressIndicator(): Long {
    val color = Color(this)
    val luminance = (0.2126 * color.red + 0.7152 * color.green + 0.0722 * color.blue)
    return if (luminance > 0.5) {
        bluePallet[0]
    } else {
        greenPallet[4]
    }
}

fun Long.getContrastingColorForText(): Long {
    val color = Color(this)
    val luminance = (0.2126 * color.red + 0.7152 * color.green + 0.0722 * color.blue)
    return if (luminance > 0.5) {
        0xFF373737
    } else {
        0xFFFFFFFF
    }
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
    FROM_ZERO_TO_ONE_HUNDRED -> (average.getPercentageFromZeroToOneHundred()).toString()
    FROM_ZERO_TO_TEN -> average.getPercentageFromZeroToOne().toString()
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

fun Array<Exam>.getExamsAverage(): Float {
    var average = 0.0F
    this.forEach {
        average += (it.grade * it.gradeWeight)
    }
    return average
}

fun Long.getContrastingButtonColor(): Long {
    val color = Color(this)
    val luminance = 0.2126 * color.red + 0.7152 * color.green + 0.0722 * color.blue

    return if (luminance > 0.5) {
        this.darken(0.6f)
    } else {
        this.lighten(1.4f)
    }
}

fun Long.getForBackgroundBasedOnTitleBarColor(): Long {
    val color = Color(this)
    val luminance = 0.2126 * color.red + 0.7152 * color.green + 0.0722 * color.blue

    return if (luminance > 0.5) {
        this.darken(0.8f)
    } else {
        this.lighten(1.2f)
    }
}

fun Long.darken(factor: Float = 0.8f): Long {
    val a = (this shr 24) and 0xFF
    val r = (this shr 16) and 0xFF
    val g = (this shr 8) and 0xFF
    val b = this and 0xFF

    val newR = (r * factor).toInt().coerceIn(0, 255).toLong()
    val newG = (g * factor).toInt().coerceIn(0, 255).toLong()
    val newB = (b * factor).toInt().coerceIn(0, 255).toLong()

    return (a shl 24) or (newR shl 16) or (newG shl 8) or newB
}

fun Long.lighten(factor: Float = 1.2f): Long {
    val a = (this shr 24) and 0xFF
    val r = (this shr 16) and 0xFF
    val g = (this shr 8) and 0xFF
    val b = this and 0xFF

    val newR = (r * factor).toInt().coerceIn(0, 255).toLong()
    val newG = (g * factor).toInt().coerceIn(0, 255).toLong()
    val newB = (b * factor).toInt().coerceIn(0, 255).toLong()

    // Reconstrói o Long: AARRGGBB
    return (a shl 24) or (newR shl 16) or (newG shl 8) or newB
}

fun parseToDateTime(
    dateMillis: Long,
    hour: Int,
    minute: Int
): LocalDateTime {
    val date = Instant.ofEpochMilli(dateMillis).atZone(ZoneOffset.UTC).toLocalDate()
    val time = LocalTime.of(hour, minute)
    return LocalDateTime.of(date, time)
}