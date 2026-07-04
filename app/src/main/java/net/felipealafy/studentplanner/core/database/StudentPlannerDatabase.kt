package net.felipealafy.studentplanner.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import net.felipealafy.studentplanner.feature_class.data.local.ClassDao
import net.felipealafy.studentplanner.feature_planner.data.local.PlannerDao
import net.felipealafy.studentplanner.feature_subject.data.local.SubjectDao
import net.felipealafy.studentplanner.feature_class.data.local.ClassTable
import net.felipealafy.studentplanner.feature_exams.data.local.ExamDao
import net.felipealafy.studentplanner.feature_exams.data.local.ExamTable
import net.felipealafy.studentplanner.feature_planner.data.local.PlannerTable
import net.felipealafy.studentplanner.feature_subject.data.local.SubjectTable

@Database(
    version = 2,
    exportSchema = true,
    entities =
        [
            PlannerTable::class,
            SubjectTable::class,
            ClassTable::class,
            ExamTable::class
        ]
)
@TypeConverters(Converters::class)
abstract class StudentPlannerDatabase : RoomDatabase() {
    abstract fun PlannerDao() : PlannerDao
    abstract fun SubjectDao() : SubjectDao
    abstract fun ClassDao(): ClassDao
    abstract fun ExamDao(): ExamDao

    companion object {
        @Volatile
        private var INSTANCE: StudentPlannerDatabase? = null
        fun getDatabase(applicationContext: Context): StudentPlannerDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context = applicationContext,
                    klass = StudentPlannerDatabase::class.java,
                    name = "student_planner_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}