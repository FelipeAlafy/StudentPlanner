package net.felipealafy.studentplanner.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import net.felipealafy.studentplanner.StudentPlannerApplication
import net.felipealafy.studentplanner.models.DetailedPlannerViewModel
import net.felipealafy.studentplanner.models.MainViewModel
import net.felipealafy.studentplanner.models.PlannerModel
import net.felipealafy.studentplanner.models.StudentClassViewModel
import net.felipealafy.studentplanner.models.SubjectCreationViewModel
import net.felipealafy.studentplanner.models.TodayModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            val app = (this[APPLICATION_KEY] as StudentPlannerApplication)
            PlannerModel(app.plannerRepository)
        }

        initializer {
            val app = (this[APPLICATION_KEY] as StudentPlannerApplication)
            TodayModel(
                plannerRepository = app.plannerRepository,
                classRepository = app.classRepository
                )
        }

        initializer {
            val app = (this[APPLICATION_KEY] as StudentPlannerApplication)
            DetailedPlannerViewModel(
                savedStateHandle = this.createSavedStateHandle(),
                plannerRepository = app.plannerRepository
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
            StudentClassViewModel(
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
    }
}