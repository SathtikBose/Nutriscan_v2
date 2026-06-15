package com.buildstack.nutriscan.data.remote

import com.buildstack.nutriscan.data.remote.dto.ProfileResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

data class UpdateProfileRequest(
    val age: Int?,
    val weight: Float?,
    val height: Float?,
    val allergies: List<String>,
    val dietaryPreferences: List<String>
)

interface ProfileApi {
    @GET("user/profile")
    suspend fun getProfile(): Response<ProfileResponse>

    @PUT("user/profile")
    suspend fun updateProfile(@Body request: UpdateProfileRequest): Response<ProfileResponse>
}
