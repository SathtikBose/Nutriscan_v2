package com.buildstack.nutriscan.domain.repository

import com.buildstack.nutriscan.domain.model.UserProfile

interface ProfileRepository {
    suspend fun getProfile(): Result<UserProfile>
    suspend fun updateProfile(
        name: String?,
        age: Int?,
        weight: Float?,
        height: Float?,
        allergies: List<String>,
        dietaryPreferences: List<String>,
        imageBytes: ByteArray? = null,
        mimeType: String? = null
    ): Result<UserProfile>
}
