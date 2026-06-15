package com.buildstack.nutriscan.presentation.scan

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ScanUiState(
    val imageUri: Uri? = null,
    val isAnalyzing: Boolean = false,
    val analysisSuccess: Boolean = false,
    val resultId: String? = null,
    val errorMessage: String? = null
)

@HiltViewModel
class ScanViewModel @Inject constructor(
    private val analyzeFoodUseCase: com.buildstack.nutriscan.domain.usecase.scan.AnalyzeFoodUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScanUiState())
    val uiState: StateFlow<ScanUiState> = _uiState.asStateFlow()

    fun onImageSelected(uri: Uri) {
        _uiState.value = _uiState.value.copy(
            imageUri = uri,
            errorMessage = null
        )
    }

    fun clearImage() {
        _uiState.value = _uiState.value.copy(
            imageUri = null,
            isAnalyzing = false,
            analysisSuccess = false,
            errorMessage = null
        )
    }

    fun analyzeImage(context: Context) {
        val uri = _uiState.value.imageUri ?: return
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isAnalyzing = true, errorMessage = null)
            
            val file = com.buildstack.nutriscan.util.ImageUtils.uriToFile(context, uri)
            if (file == null) {
                _uiState.value = _uiState.value.copy(
                    isAnalyzing = false,
                    errorMessage = "Failed to process image"
                )
                return@launch
            }

            val result = analyzeFoodUseCase(file)
            
            if (result.isSuccess) {
                _uiState.value = _uiState.value.copy(
                    isAnalyzing = false,
                    analysisSuccess = true,
                    resultId = result.getOrNull()?.id
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    isAnalyzing = false,
                    errorMessage = result.exceptionOrNull()?.message ?: "Unknown error"
                )
            }
        }
    }
}
