package net.felipealafy.studentplanner.ui.extensions

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun LocalDateTime.getFormattedDateTime(): String {
    val pattern = DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm")
    return pattern.format(this)
}