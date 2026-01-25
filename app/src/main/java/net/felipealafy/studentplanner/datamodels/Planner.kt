package net.felipealafy.studentplanner.datamodels

import java.util.UUID

data class Planner(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    var color: Long,
    val minimumGradeToPass: Float,
    val gradeDisplayStyle: GradeStyle = GradeStyle.FROM_ZERO_TO_ONE_HUNDRED,
    var subjects: Array<Subject> = emptyArray()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Planner

        if (color != other.color) return false
        if (minimumGradeToPass != other.minimumGradeToPass) return false
        if (id != other.id) return false
        if (name != other.name) return false
        if (gradeDisplayStyle != other.gradeDisplayStyle) return false
        if (!subjects.contentEquals(other.subjects)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = color.hashCode()
        result = 31 * result + minimumGradeToPass.hashCode()
        result = 31 * result + id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + gradeDisplayStyle.hashCode()
        result = 31 * result + subjects.contentHashCode()
        return result
    }
}
