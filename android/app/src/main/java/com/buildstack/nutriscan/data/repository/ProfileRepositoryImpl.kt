package com.buildstack.nutriscan.data.repository

import com.buildstack.nutriscan.data.remote.ProfileApi
import com.buildstack.nutriscan.data.remote.UpdateProfileRequest
import com.buildstack.nutriscan.domain.model.UserProfile
import com.buildstack.nutriscan.domain.repository.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import javax.inject.Inject

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

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
        dietaryPreferences: List<String>,
        imageBytes: ByteArray?,
        mimeType: String?
    ): Result<UserProfile> = withContext(Dispatchers.IO) {
        try {
            val partMap = mutableMapOf<String, okhttp3.RequestBody>()
            if (age != null) partMap["age"] = age.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            if (weight != null) partMap["weight"] = weight.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            if (height != null) partMap["height"] = height.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            
            if (allergies.isNotEmpty()) {
                partMap["allergies"] = allergies.joinToString(",").toRequestBody("text/plain".toMediaTypeOrNull())
            }
            if (dietaryPreferences.isNotEmpty()) {
                partMap["dietaryPreferences"] = dietaryPreferences.joinToString(",").toRequestBody("text/plain".toMediaTypeOrNull())
            }

            var profilePicPart: MultipartBody.Part? = null
            if (imageBytes != null && mimeType != null) {
                val reqBody = imageBytes.toRequestBody(mimeType.toMediaTypeOrNull())
                // The filename 'profile.jpg' is arbitrary, backend uses the field name 'profilePic'
                profilePicPart = MultipartBody.Part.createFormData("profilePic", "profile.jpg", reqBody)
            }

            val response = api.updateProfile(profilePicPart, partMap)
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
