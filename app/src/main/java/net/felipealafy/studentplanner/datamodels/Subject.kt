package net.felipealafy.studentplanner.datamodels

import java.time.LocalDateTime
import java.util.UUID


data class Subject(
    val id: String = UUID.randomUUID().toString(),
    val plannerId: String,
    val name: String,
    var color: Long,
    val start: LocalDateTime,
    val end: LocalDateTime,
    var studentClasses: Array<StudentClass> = emptyArray<StudentClass>(),
    var exams: Array<Exam> = emptyArray<Exam>()
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
        if (!exams.contentEquals(other.exams)) return false
        if (!studentClasses.contentEquals(other.studentClasses)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + color.hashCode()
        result = 31 * result + plannerId.hashCode()
        result = 31 * result + start.hashCode()
        result = 31 * result + end.hashCode()
        result = 31 * result + exams.contentHashCode()
        result = 31 * result + studentClasses.contentHashCode()
        return result
    }
}