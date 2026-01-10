package net.felipealafy.studentplanner.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import net.felipealafy.studentplanner.tablemodels.ClassTable
import net.felipealafy.studentplanner.tablemodels.ExamTable
import net.felipealafy.studentplanner.tablemodels.PlannerTable
import net.felipealafy.studentplanner.tablemodels.SubjectTable

@Database(
    version = 1,
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