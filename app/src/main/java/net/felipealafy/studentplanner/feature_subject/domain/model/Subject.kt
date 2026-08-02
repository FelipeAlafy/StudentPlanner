package net.felipealafy.studentplanner.feature_subject.domain.model

import java.time.LocalDateTime
import java.util.UUID


data class Subject(
    val id: String = UUID.randomUUID().toString(),
    val plannerId: String,
    val name: String,
    var color: Long,
    val start: LocalDateTime,
    val end: LocalDateTime
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Subject

        if (id != other.id) return false
        if (name != other.name) return false
        if (color != other.color) return false
        if (plannerId != other.plannerId) return false
        if (start != other.start) return false
        if (end != other.end) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + color.hashCode()
        result = 31 * result + plannerId.hashCode()
        result = 31 * result + start.hashCode()
        result = 31 * result + end.hashCode()
        return result
    }
}