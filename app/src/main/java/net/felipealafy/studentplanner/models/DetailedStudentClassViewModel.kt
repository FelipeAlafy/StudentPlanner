package net.felipealafy.studentplanner.models

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import net.felipealafy.studentplanner.datamodels.StudentClass
import net.felipealafy.studentplanner.datamodels.Subject
import net.felipealafy.studentplanner.repositories.ClassRepository
import net.felipealafy.studentplanner.repositories.SubjectRepository
import java.time.LocalDateTime
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow

data class DetailedStudentClassUiState(
    val classEntry: StudentClass? = null,
    val subject: Subject? = null,
    val isLoading: Boolean = true
)

class DetailedStudentClassViewModel (
    savedStateHandle: SavedStateHandle,
    classRepository: ClassRepository,
    subjectRepository: SubjectRepository

): ViewModel() {
    val subjectId: String = checkNotNull(savedStateHandle["subjectId"])
    val studentClassId: String = checkNotNull(savedStateHandle["studentClassId"])

    private var _studentClassEntry: Flow<StudentClass> = classRepository.getClassById(studentClassId)

    val subjectFlow: Flow<Subject> = subjectRepository.getSubjectById(subjectId)

    val uiState: StateFlow<DetailedStudentClassUiState> = combine(
        _studentClassEntry,
        subjectFlow
    ) { currentEntry, subject ->

        DetailedStudentClassUiState(
            classEntry = currentEntry,
            subject = subject,
            isLoading = false
        )

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = DetailedStudentClassUiState()
    )


    fun getEmptyStudentClass(): StudentClass {
        return StudentClass(
            id = "",
            subjectId = "",
            title = "THERE ARE FAILURES WHILE LOADING DATA, PLEASE RESTART.",
            start = LocalDateTime.now(),
            end = LocalDateTime.now(),
            noteTakingLink = "",
            observation = ""
        )
    }
}

