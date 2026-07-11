package net.felipealafy.studentplanner.feature_exams.domain.model

import net.felipealafy.studentplanner.feature_exams.domain.use_case.GradeStyle
import java.time.LocalDateTime

data class ExamParams (
    val examId: String,
    val subjectId: String,
    val name: String,
    val gradeString: String,
    val gradeWeightString: String,
    val start: LocalDateTime,
    val end: LocalDateTime,
    val gradeStyle: GradeStyle
)
