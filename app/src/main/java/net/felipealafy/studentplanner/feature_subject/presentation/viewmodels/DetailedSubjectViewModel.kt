package net.felipealafy.studentplanner.feature_subject.presentation.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import net.felipealafy.studentplanner.feature_subject.domain.model.DetailedSubject
import net.felipealafy.studentplanner.feature_subject.domain.use_case.GetDetailedSubjectUseCase
import javax.inject.Inject

sealed interface SubjectDetailsUiState {
    data object Loading: SubjectDetailsUiState
    data class Success(val data: DetailedSubject) : SubjectDetailsUiState
    data class Error(val exception: String) : SubjectDetailsUiState
}

@HiltViewModel
class DetailedSubjectViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getDetailedSubjectUseCase: GetDetailedSubjectUseCase,
): ViewModel() {
    private val subjectId: String = checkNotNull(savedStateHandle["subjectId"])


    val uiState : StateFlow<SubjectDetailsUiState> = getDetailedSubjectUseCase(subjectId = subjectId)
        .map { detailedSubject ->
            SubjectDetailsUiState.Success(data = detailedSubject) as SubjectDetailsUiState
        }
        .catch { error ->
            emit(SubjectDetailsUiState.Error(exception = error.message ?: "Unknown error"))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SubjectDetailsUiState.Loading
        )
}