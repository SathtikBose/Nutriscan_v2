package com.buildstack.nutriscan.data.repository

import com.buildstack.nutriscan.data.remote.ProfileApi
import com.buildstack.nutriscan.data.remote.UpdateProfileRequest
import com.buildstack.nutriscan.domain.model.UserProfile
import com.buildstack.nutriscan.domain.repository.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val api: ProfileApi
) : ProfileRepository {

    override suspend fun getProfile(): Result<UserProfile> = withContext(Dispatchers.IO) {
        try {
            val response = api.getProfile()
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()!!.data.toDomainModel())
            } else {
                Result.failure(Exception(response.message() ?: "Failed to fetch profile"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateProfile(
        age: Int?,
        weight: Float?,
        height: Float?,
        allergies: List<String>,
        dietaryPreferences: List<String>
    ): Result<UserProfile> = withContext(Dispatchers.IO) {
        try {
            val request = UpdateProfileRequest(
                age = age,
                weight = weight,
                height = height,
                allergies = allergies,
                dietaryPreferences = dietaryPreferences
            )
            val response = api.updateProfile(request)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()!!.data.toDomainModel())
            } else {
                Result.failure(Exception(response.message() ?: "Failed to update profile"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
