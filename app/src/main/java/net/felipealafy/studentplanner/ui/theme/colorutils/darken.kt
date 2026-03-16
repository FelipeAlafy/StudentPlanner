package net.felipealafy.studentplanner.ui.theme.colorutils

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