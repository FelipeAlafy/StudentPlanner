package net.felipealafy.studentplanner.core.dependencyinjection

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import net.felipealafy.studentplanner.feature_class.data.repository.ClassRepositoryImpl
import net.felipealafy.studentplanner.feature_class.domain.repository.ClassRepository
import net.felipealafy.studentplanner.feature_exams.data.repository.ExamRepositoryImpl
import net.felipealafy.studentplanner.feature_exams.domain.repository.ExamRepository
import net.felipealafy.studentplanner.feature_planner.data.repository.PlannerRepositoryImpl
import net.felipealafy.studentplanner.feature_planner.domain.repository.PlannerRepository
import net.felipealafy.studentplanner.feature_subject.data.repository.SubjectRepositoryImpl
import net.felipealafy.studentplanner.feature_subject.domain.repository.SubjectRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindPlannerRepository(
        plannerRepositoryImpl: PlannerRepositoryImpl
    ): PlannerRepository

    @Binds
    @Singleton
    abstract fun bindSubjectRepository(
        subjectRepositoryImpl: SubjectRepositoryImpl
    ): SubjectRepository

    @Binds
    @Singleton
    abstract fun bindClassRepository(
        classRepositoryImpl: ClassRepositoryImpl
    ): ClassRepository

    @Binds
    @Singleton
    abstract fun bindExamRepository(
        examRepositoryImpl: ExamRepositoryImpl
    ): ExamRepository
}