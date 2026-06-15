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
    private val getMotivationUseCase: GetMotivationUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState(isLoading = true))
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadHomeData()
    }

    private fun loadHomeData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            // Mock recent scans for now
            val mockScans = listOf(
                RecentScan("1", "Organic Almond Milk", null, 85),
                RecentScan("2", "Protein Bar", null, 65),
                RecentScan("3", "Sugary Cereal", null, 20)
            )

            // Fetch motivation
            val motivationResult = getMotivationUseCase()
            val motivationMessage = motivationResult.getOrDefault("Eat an apple a day to keep the doctor away!")

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                userName = "Alex", // Mock user name
                motivationMessage = motivationMessage,
                recentScans = mockScans,
                overallHealthScore = 56 // Mock overall score
            )
        }
    }
}
