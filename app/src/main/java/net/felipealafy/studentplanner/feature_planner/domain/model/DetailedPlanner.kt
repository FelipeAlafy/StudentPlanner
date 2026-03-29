package net.felipealafy.studentplanner.feature_planner.domain.model

import net.felipealafy.studentplanner.feature_subject.domain.model.DetailedSubject

data class DetailedPlanner(
    val planner: Planner,
    val subjects: List<DetailedSubject>
) {
    val subjectsCount: Int
        get() = if (subjects.isEmpty()) 1 else subjects.size

    val passedSubjectsCount: Int
        get() = subjects.count { subject -> subject.averageGrade >= planner.minimumGradeToPass }

    val plannerProgress: Float
        get() = if (subjects.isEmpty()) 0f else passedSubjectsCount.toFloat() / subjectsCount

    val globalAverage: Float
        get() {
            val allExams = subjects.flatMap { it.exams.toList() }
            if (allExams.isEmpty()) return 0F
            return allExams.sumOf { it.grade.toDouble() }.toFloat() / allExams.size
        }

    val isApproved: Boolean
        get() {
            val allExams = subjects.flatMap { it.exams.toList() }
            val finalScore = allExams.sumOf { it.grade.toDouble() * it.gradeWeight.toDouble() }.toFloat()
            return finalScore >= planner.minimumGradeToPass
        }
}
