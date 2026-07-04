package net.felipealafy.studentplanner.feature_class.presentation.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import net.felipealafy.studentplanner.feature_class.domain.exception.InvalidClassExceptions
import net.felipealafy.studentplanner.feature_class.domain.model.EnrichedDetailedClass
import net.felipealafy.studentplanner.feature_class.domain.use_case.GetClassUseCase
import javax.inject.Inject

sealed interface DetailedStudentClassUiState {
    data object Loading: DetailedStudentClassUiState
    data class Error(val exception: InvalidClassExceptions): DetailedStudentClassUiState
    data class Success(val enrichedPlanner: EnrichedDetailedClass): DetailedStudentClassUiState
}

@HiltViewModel
class DetailedStudentClassViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getClassUseCase: GetClassUseCase

): ViewModel() {
    val studentClassId: String = checkNotNull(savedStateHandle["studentClassId"])

    val uiState: StateFlow<DetailedStudentClassUiState> = getClassUseCase(studentClassId).map {
        DetailedStudentClassUiState.Success(enrichedPlanner = it) as DetailedStudentClassUiState
    }.catch { error ->
            emit(DetailedStudentClassUiState.Error(exception = error as InvalidClassExceptions))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = DetailedStudentClassUiState.Loading
    )
}

