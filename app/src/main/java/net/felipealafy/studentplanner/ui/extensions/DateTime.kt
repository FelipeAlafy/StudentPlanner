package net.felipealafy.studentplanner.ui.extensions

import android.util.Log
import android.icu.text.SimpleDateFormat
import java.time.LocalDateTime

fun LocalDateTime.getFormattedDateTime(): String {
    val pattern = SimpleDateFormat("dd MMMM yyyy HH:mm")
    return pattern.format(this)
}