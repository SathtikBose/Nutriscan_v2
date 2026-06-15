package com.buildstack.nutriscan.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buildstack.nutriscan.domain.model.ScanResult
import com.buildstack.nutriscan.domain.usecase.history.GetScanHistoryUseCase
import com.buildstack.nutriscan.domain.usecase.history.SyncHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HistoryUiState(
    val scans: List<ScanResult> = emptyList(),
    val isSyncing: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getScanHistoryUseCase: GetScanHistoryUseCase,
    private val syncHistoryUseCase: SyncHistoryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    init {
        loadLocalHistory()
        syncHistory()
    }

    private fun loadLocalHistory() {
        viewModelScope.launch {
            getScanHistoryUseCase()
                .catch { e ->
                    _uiState.value = _uiState.value.copy(errorMessage = e.message)
                }
                .collect { scans ->
                    _uiState.value = _uiState.value.copy(scans = scans)
                }
        }
    }

    fun syncHistory() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSyncing = true, errorMessage = null)
            val result = syncHistoryUseCase()
            if (result.isFailure) {
                _uiState.value = _uiState.value.copy(
                    isSyncing = false,
                    errorMessage = result.exceptionOrNull()?.message
                )
            } else {
                _uiState.value = _uiState.value.copy(isSyncing = false)
            }
        }
    }
}
