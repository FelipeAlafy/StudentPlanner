package net.felipealafy.studentplanner.dependencyinjection

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
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

    //Database migration history
    //Version 1 to Version 2 - entries added to the database
    val MIGRATION_V1_TO_V2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("DROP INDEX IF EXISTS `index_subject_plannerId`")
            db.execSQL("DROP INDEX IF EXISTS `index_class_subjectId`")
            db.execSQL("DROP INDEX IF EXISTS `index_class_start_end`")
            db.execSQL("DROP INDEX IF EXISTS `index_exam_subjectId`")
            db.execSQL("DROP INDEX IF EXISTS `index_exam_start_end`")

            db.execSQL("CREATE INDEX `index_subject_plannerId` ON `subject` (`plannerId`)")
            db.execSQL("CREATE INDEX `index_class_subjectId` ON `class` (`subjectId`)")
            db.execSQL("CREATE INDEX `index_class_start_end` ON `class` (`start`, `end`)")
            db.execSQL("CREATE INDEX `index_exam_subjectId` ON `exam` (`subjectId`)")
            db.execSQL("CREATE INDEX `index_exam_start_end` ON `exam` (`start`, `end`)")
        }
    }

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
        .addMigrations(MIGRATION_V1_TO_V2)
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
