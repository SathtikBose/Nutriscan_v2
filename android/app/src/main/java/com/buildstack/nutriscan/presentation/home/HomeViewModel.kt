package com.buildstack.nutriscan.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buildstack.nutriscan.domain.usecase.GetMotivationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RecentScan(
    val id: String,
    val name: String,
    val imageUrl: String?,
    val healthScore: Int
)

data class HomeUiState(
    val isLoading: Boolean = false,
    val userName: String = "User",
    val motivationMessage: String = "Loading...",
    val recentScans: List<RecentScan> = emptyList(),
    val overallHealthScore: Int = 0
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getMotivationUseCase: GetMotivationUseCase,
    private val profileRepository: com.buildstack.nutriscan.domain.repository.ProfileRepository,
    private val scanRepository: com.buildstack.nutriscan.domain.repository.ScanRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState(isLoading = true))
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadHomeData()
    }

    private fun loadHomeData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            // Sync history first
            scanRepository.syncHistory()

            var name = "User"
            val profileResult = profileRepository.getProfile()
            if (profileResult.isSuccess) {
                name = profileResult.getOrNull()?.name ?: "User"
            }

            var recentScans: List<RecentScan> = emptyList()
            var avgScore = 0
            
            // Collect scan history directly from local DB
            scanRepository.getScanHistory().collect { scans ->
                val sorted = scans.sortedByDescending { it.date }
                recentScans = sorted.take(5).map { 
                    RecentScan(it.id, it.productName, it.productImage, it.productScore)
                }
                
                if (scans.isNotEmpty()) {
                    avgScore = scans.map { it.productScore }.average().toInt()
                }

                // Fetch motivation
                val motivationResult = getMotivationUseCase()
                val motivationMessage = motivationResult.getOrDefault("Eat an apple a day to keep the doctor away!")

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    userName = name,
                    motivationMessage = motivationMessage,
                    recentScans = recentScans,
                    overallHealthScore = avgScore
                )
            }
        }
    }
}
