package net.felipealafy.studentplanner.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import net.felipealafy.studentplanner.R
import net.felipealafy.studentplanner.datamodels.Exam
import net.felipealafy.studentplanner.datamodels.GradeAToF
import net.felipealafy.studentplanner.datamodels.GradeAToFWithE
import net.felipealafy.studentplanner.datamodels.GradeStyle
import net.felipealafy.studentplanner.datamodels.Planner
import net.felipealafy.studentplanner.repositories.ExamRepository
import net.felipealafy.studentplanner.repositories.PlannerRepository
import net.felipealafy.studentplanner.repositories.SubjectRepository
import net.felipealafy.studentplanner.ui.forms.ExamForm
import net.felipealafy.studentplanner.ui.views.parseToDateTime
import javax.inject.Inject

data class ExamCreationUiState(
    val planner: Planner? = null,
    val entryExam: ExamForm = ExamForm(),
    val isValid: Boolean = false,
    val isLoading: Boolean = true
)

@HiltViewModel
class ExamCreationViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val plannerRepository: PlannerRepository,
    subjectRepository: SubjectRepository,
    private val examRepository: ExamRepository
) : ViewModel() {
    private val plannerId: String = checkNotNull(savedStateHandle["plannerId"])

    private val plannerFlow: Flow<Planner?> = flow {
        emit(plannerRepository.getPlannerById(plannerId))
    }.flowOn(Dispatchers.IO)

    private val _currentForm = MutableStateFlow(ExamForm())

    private val _toastEvent = MutableSharedFlow<Int>()

    private var _isInvalidInputErrorForGrade = MutableStateFlow(false)

    private var _isInvalidInputErrorForGradeWeight = MutableStateFlow(false)


    val toastEvent: SharedFlow<Int> = _toastEvent.asSharedFlow()
    val isInvalidInputErrorForGrade: StateFlow<Boolean> = _isInvalidInputErrorForGrade

    val isInvalidInputErrorForGradeWeight: StateFlow<Boolean> = _isInvalidInputErrorForGradeWeight

    val uiState: StateFlow<ExamCreationUiState> = combine(
        plannerFlow,
        subjectRepository.getAllSubjectsOfAPlanner(plannerId),
        _currentForm
    ) { planner, subjects, currentForm ->

        val isValid = currentForm.name.isNotEmpty() &&
                currentForm.subjectId.isNotEmpty()

        val plannerWithSubjects = planner?.copy(subjects = subjects.toTypedArray())

        ExamCreationUiState(
            planner = plannerWithSubjects,
            entryExam = currentForm,
            isValid = isValid,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ExamCreationUiState(
            entryExam = ExamForm(),
            isLoading = true
        )
    )

    fun updateSubject(subjectId: String) {
        _currentForm.update {
            it.copy(subjectId = subjectId)
        }
    }

    fun updateName(newName: String) {
        _currentForm.update {
            it.copy(name = newName)
        }
    }

    fun updateGradeFrom0to100(newGrade: String) {
        validateGrade(false)
        _currentForm.update {
            it.copy(grade = newGrade)
        }
    }

    fun updateGradeFrom0to10(newGrade: String) {
        validateGrade(false)
        _currentForm.update {
            it.copy(grade = newGrade)
        }
    }

    fun validateGrade(shouldDisplayToast: Boolean = false) {
        var pattern: Regex
        var errorStringId: Int
        if (uiState.value.planner!!.gradeDisplayStyle == GradeStyle.FROM_ZERO_TO_ONE_HUNDRED) {
            pattern = Regex("^(100|[1-9]?[0-9])$")
            errorStringId = R.string.invalid_grade_input_0_to_100
        } else {
            pattern = Regex("^(10([.,]0*)?|[0-9]([.,][0-9]+)?)$")
            errorStringId = R.string.invalid_grade_input_0_to_10
        }
        if (!pattern.matches(_currentForm.value.grade)) {
            _isInvalidInputErrorForGrade.update {
                true
            }
            if (!shouldDisplayToast) return
            viewModelScope.launch {
                _toastEvent.emit(errorStringId)
            }
            return
        }
        _isInvalidInputErrorForGrade.update {
            false
        }
    }

    fun validateGradeWeight(shouldDisplayToast: Boolean = false) {
        val pattern = Regex("^(100|[1-9]?[0-9])$")
        val errorStringId = R.string.invalid_grade_input_0_to_100

        if (!pattern.matches(_currentForm.value.gradeWeight)) {
            _isInvalidInputErrorForGradeWeight.update {
                true
            }
            if (!shouldDisplayToast) return
            viewModelScope.launch {
                _toastEvent.emit(errorStringId)
            }
            return
        }

        _isInvalidInputErrorForGradeWeight.update {
            false
        }
    }

    fun updateGradeFromAToF(newGrade: GradeAToF) {
        val grade = when (newGrade) {
            GradeAToF.A -> 100F
            GradeAToF.B -> 80F
            GradeAToF.C -> 70F
            GradeAToF.D -> 60F
            GradeAToF.F -> 0F
        }

        _currentForm.update {
            it.copy(grade = grade.toString())
        }
    }

    fun updateGradeFromAToFWithE(newGrade: GradeAToFWithE) {
        val grade = when (newGrade) {
            GradeAToFWithE.A -> 100F
            GradeAToFWithE.B -> 80F
            GradeAToFWithE.C -> 70F
            GradeAToFWithE.D -> 60F
            GradeAToFWithE.E -> 50F
            GradeAToFWithE.F -> 0F
        }

        _currentForm.update {
            it.copy(grade = grade.toString())
        }
    }

    fun updateGradeWeight(newGradeWeight: String) {
        _currentForm.update {
            it.copy(gradeWeight = newGradeWeight)
        }
    }

    private fun validator(entryExam: ExamForm): Boolean {
        if (entryExam.name.isEmpty()) return false
        if (entryExam.subjectId.isEmpty()) return false
        if (isInvalidInputErrorForGrade.value) return false
        if (isInvalidInputErrorForGradeWeight.value) return false

        return true
    }

    fun saveExam() {
        if (!uiState.value.isValid) return
        if (!validator(uiState.value.entryExam)) return
        viewModelScope.launch {
            examRepository.insert(examFormToExam(uiState.value.entryExam))
        }
    }

    private fun examFormToExam(examForm: ExamForm): Exam {
        return Exam(
            id = examForm.id,
            subjectId = examForm.subjectId,
            name = examForm.name,
            grade = (examForm.grade.replace(',', '.').toFloat()),
            gradeWeight = (examForm.gradeWeight.replace(',', '.').toFloat() / 100F),
            start = examForm.start,
            end = examForm.end
        )
    }

    fun updateStartDate(dateMillis: Long?, hour: Int, minute: Int) {
        if (dateMillis == null) return
        val newDateTime = parseToDateTime(dateMillis, hour, minute)
        _currentForm.update {
            it.copy(start = newDateTime)
        }
    }

    fun updateEndDate(dateMillis: Long?, hour: Int, minute: Int) {
        if (dateMillis == null) return
        val newDateTime = parseToDateTime(dateMillis, hour, minute)
        _currentForm.update {
            it.copy(end = newDateTime)
        }
    }
}