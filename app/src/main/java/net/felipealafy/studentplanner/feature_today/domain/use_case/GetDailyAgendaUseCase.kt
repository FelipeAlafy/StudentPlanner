package net.felipealafy.studentplanner.feature_today.domain.use_case

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import net.felipealafy.studentplanner.feature_class.data.repository.ClassRepositoryImpl
import net.felipealafy.studentplanner.feature_exams.data.repository.ExamRepositoryImpl
import net.felipealafy.studentplanner.feature_planner.data.repository.PlannerRepositoryImpl
import net.felipealafy.studentplanner.feature_planner.domain.model.DetailedPlanner
import net.felipealafy.studentplanner.feature_subject.data.repository.SubjectRepositoryImpl
import net.felipealafy.studentplanner.feature_subject.domain.model.DetailedSubject
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

class GetDailyAgendaUseCase @Inject constructor(
    private val plannerRepository: PlannerRepositoryImpl,
    private val subjectRepository: SubjectRepositoryImpl,
    private val classRepository: ClassRepositoryImpl,
    private val examRepository: ExamRepositoryImpl
) {
    operator fun invoke(plannerId: String, date: LocalDate): Flow<DetailedPlanner> {
        val startOfDay = date.atStartOfDay()
        val endOfDay = date.atTime(LocalTime.MAX)

        val plannerFlow = flow {
            emit(plannerRepository.getPlannerById(plannerId))
        }

        return combine(
            plannerFlow,
            subjectRepository.getAllSubjects(),
            classRepository.getClassesByDateTime(startOfDay, endOfDay),
            examRepository.getExamsByDateTime(startOfDay, endOfDay)
        ) { planner, allSubjects, classes, exams ->

            checkNotNull(planner) { "Planner with id $plannerId not found." }

            val plannerSubjects = allSubjects.filter { it.plannerId == planner.first().id }

            val detailedSubjects = plannerSubjects.map { subject ->
                DetailedSubject(
                    subject = subject,
                    studentClasses = classes.filter { it.subjectId == subject.id },
                    exams = exams.filter { it.subjectId == subject.id }
                )
            }

            DetailedPlanner(
                planner = planner.first(),
                subjects = detailedSubjects
            )
        }
    }
}