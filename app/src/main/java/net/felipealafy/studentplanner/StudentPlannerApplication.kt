package net.felipealafy.studentplanner

import android.app.Application
import net.felipealafy.studentplanner.database.StudentPlannerDatabase
import net.felipealafy.studentplanner.repositories.ClassRepository
import net.felipealafy.studentplanner.repositories.ExamRepository
import net.felipealafy.studentplanner.repositories.PlannerRepository
import net.felipealafy.studentplanner.repositories.SubjectRepository

class StudentPlannerApplication : Application() {
    val database by lazy {
        StudentPlannerDatabase.getDatabase(this)
    }

    val plannerRepository by lazy {
        PlannerRepository(dao = database.PlannerDao())
    }

    val subjectRepository by lazy {
        SubjectRepository(dao = database.SubjectDao())
    }

    val classRepository by lazy {
        ClassRepository(dao = database.ClassDao())
    }

    val examRepository by lazy {
        ExamRepository(dao = database.ExamDao())
    }
}