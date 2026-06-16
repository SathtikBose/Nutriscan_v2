package com.buildstack.nutriscan.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buildstack.nutriscan.domain.model.UserProfile
import com.buildstack.nutriscan.domain.repository.AuthRepository
import com.buildstack.nutriscan.domain.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.buildstack.nutriscan.data.local.prefs.TokenManager

data class ProfileState(
    val isLoading: Boolean = false,
    val profile: UserProfile? = null,
    val error: String? = null,
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val authRepository: AuthRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    val themeMode: StateFlow<String> = tokenManager.theme.stateIn(
        scope = viewModelScope,
        started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
        initialValue = "SYSTEM"
    )

    init {
        loadProfile()
    }

    fun loadProfile() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            profileRepository.getProfile().fold(
                onSuccess = { profile ->
                    _state.update { it.copy(isLoading = false, profile = profile) }
                },
                onFailure = { e ->
                    _state.update { it.copy(isLoading = false, error = e.message ?: "Failed to load profile") }
                }
            )
        }
    }

    fun updateProfile(
        name: String?,
        age: Int?,
        weight: Float?,
        height: Float?,
        allergies: List<String>,
        dietaryPreferences: List<String>,
        imageBytes: ByteArray? = null,
        mimeType: String? = null
    ) {
        viewModelScope.launch {
            _state.update { it.copy(isSaving = true, error = null, saveSuccess = false) }
            profileRepository.updateProfile(name, age, weight, height, allergies, dietaryPreferences, imageBytes, mimeType).fold(
                onSuccess = { profile ->
                    _state.update { it.copy(isSaving = false, profile = profile, saveSuccess = true) }
                },
                onFailure = { e ->
                    _state.update { it.copy(isSaving = false, error = e.message ?: "Failed to save profile") }
                }
            )
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }

    fun setThemeMode(mode: String) {
        viewModelScope.launch {
            tokenManager.saveTheme(mode)
        }
    }
}
