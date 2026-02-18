package net.felipealafy.studentplanner.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import net.felipealafy.studentplanner.StudentPlannerApplication
import net.felipealafy.studentplanner.models.DetailedExamViewModel
import net.felipealafy.studentplanner.models.DetailedPlannerViewModel
import net.felipealafy.studentplanner.models.DetailedStudentClassViewModel
import net.felipealafy.studentplanner.models.EditExamViewModel
import net.felipealafy.studentplanner.models.EditStudentClassViewModel
import net.felipealafy.studentplanner.models.ExamCreationViewModel
import net.felipealafy.studentplanner.models.MainViewModel
import net.felipealafy.studentplanner.models.PlannerModel
import net.felipealafy.studentplanner.models.StudentClassCreationViewModel
import net.felipealafy.studentplanner.models.SubjectCreationViewModel
import net.felipealafy.studentplanner.models.TodayViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            val app = (this[APPLICATION_KEY] as StudentPlannerApplication)
            PlannerModel(app.plannerRepository)
        }

        initializer {
            val app = (this[APPLICATION_KEY] as StudentPlannerApplication)
            TodayViewModel(
                plannerRepository = app.plannerRepository,
                classRepository = app.classRepository,
                subjectRepository = app.subjectRepository,
                examRepository = app.examRepository
            )
        }

        initializer {
            val app = (this[APPLICATION_KEY] as StudentPlannerApplication)
            DetailedPlannerViewModel(
                savedStateHandle = this.createSavedStateHandle(),
                plannerRepository = app.plannerRepository,
                subjectRepository = app.subjectRepository,
                classRepository = app.classRepository,
                examRepository = app.examRepository
            )
        }

        initializer {
            val app = (this[APPLICATION_KEY] as StudentPlannerApplication)
            MainViewModel(
                repository = app.plannerRepository
            )
        }

        initializer {
            val app = (this[APPLICATION_KEY] as StudentPlannerApplication)
            StudentClassCreationViewModel(
                savedStateHandle = this.createSavedStateHandle(),
                classRepository = app.classRepository,
                plannerRepository = app.plannerRepository,
                subjectsRepository = app.subjectRepository
            )
        }

        initializer {
            val app = (this[APPLICATION_KEY] as StudentPlannerApplication)
            SubjectCreationViewModel(
                savedStateHandle = this.createSavedStateHandle(),
                subjectRepository = app.subjectRepository,
                plannerRepository = app.plannerRepository
            )
        }
        
        initializer { 
            val app = (this[APPLICATION_KEY] as StudentPlannerApplication)
            DetailedStudentClassViewModel(
                savedStateHandle = this.createSavedStateHandle(),
                classRepository = app.classRepository,
                subjectRepository = app.subjectRepository
            )
        }

        initializer {
            val app = (this[APPLICATION_KEY] as StudentPlannerApplication)
            ExamCreationViewModel(
                savedStateHandle = this.createSavedStateHandle(),
                plannerRepository = app.plannerRepository,
                subjectRepository = app.subjectRepository,
                examRepository = app.examRepository,
            )
        }

        initializer {
            val app = (this[APPLICATION_KEY] as StudentPlannerApplication)
            DetailedExamViewModel(
                savedStateHandler = this.createSavedStateHandle(),
                plannerRepository = app.plannerRepository,
                subjectRepository = app.subjectRepository,
                examRepository = app.examRepository
            )
        }

        initializer {
            val app = (this[APPLICATION_KEY] as StudentPlannerApplication)
            EditStudentClassViewModel(
                savedStateHandle = this.createSavedStateHandle(),
                classRepository = app.classRepository,
                plannerRepository = app.plannerRepository,
                subjectsRepository = app.subjectRepository
            )
        }

        initializer {
            val app = (this[APPLICATION_KEY] as StudentPlannerApplication)
            EditExamViewModel(
                savedStateHandle = this.createSavedStateHandle(),
                plannerRepository = app.plannerRepository,
                subjectRepository = app.subjectRepository,
                examRepository = app.examRepository
            )
        }
    }
}