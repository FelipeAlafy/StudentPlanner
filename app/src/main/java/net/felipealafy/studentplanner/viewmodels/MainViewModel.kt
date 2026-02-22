package net.felipealafy.studentplanner.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import net.felipealafy.studentplanner.repositories.PlannerRepository
import net.felipealafy.studentplanner.ui.views.StudentPlannerViews
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: PlannerRepository) : ViewModel() {
    private val _startDestination = MutableStateFlow<String?>(null)
    val startDestination = _startDestination.asStateFlow()

    init {
        checkIfExistPlannersOnDB()
    }

    private fun checkIfExistPlannersOnDB() {
        viewModelScope.launch {
            val planners = repository.getAllPlanners().first()
            if (planners.isNotEmpty()) {
                _startDestination.value = StudentPlannerViews.TodayView.name
            } else {
                _startDestination.value = StudentPlannerViews.WelcomeView.name
            }
        }
    }
}