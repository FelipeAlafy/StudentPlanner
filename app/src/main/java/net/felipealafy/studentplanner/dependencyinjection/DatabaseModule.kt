package net.felipealafy.studentplanner.dependencyinjection

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.felipealafy.studentplanner.database.StudentPlannerDatabase
import javax.inject.Singleton

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): StudentPlannerDatabase {
        return Room.databaseBuilder(
            context,
            StudentPlannerDatabase::class.java,
            "student_planner_db"
        )
        .setJournalMode(RoomDatabase.JournalMode.WRITE_AHEAD_LOGGING)
        .fallbackToDestructiveMigration()
        .build()
    }

    @Provides
    fun providePlannerDao(database: StudentPlannerDatabase) = database.PlannerDao()

    @Provides
    fun provideSubjectDao(database: StudentPlannerDatabase) = database.SubjectDao()

    @Provides
    fun provideClassDao(database: StudentPlannerDatabase) = database.ClassDao()

    @Provides
    fun provideExamDao(database: StudentPlannerDatabase) = database.ExamDao()
}
