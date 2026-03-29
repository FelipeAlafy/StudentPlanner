package net.felipealafy.studentplanner.feature_subject.domain.model

import net.felipealafy.studentplanner.datamodels.Exam
import net.felipealafy.studentplanner.datamodels.StudentClass
import net.felipealafy.studentplanner.feature_planner.domain.exception.InvalidPlannerException

data class DetailedSubject(
    val subject: Subject,
    val studentClasses: List<StudentClass>,
    val exams: List<Exam>
) {
    val averageGrade: Float
        get() {
            if(exams.isEmpty()) return 0F
            return exams.sumOf { it.grade.toDouble() * it.gradeWeight.toDouble() }.toFloat()
        }

    val countClassesTaken: Int
        get() = studentClasses.size

    val countExamsTaken: Int
        get() = exams.size

    fun isApproved(minimumGradeToPass: Float): Boolean =  averageGrade >= minimumGradeToPass
}
