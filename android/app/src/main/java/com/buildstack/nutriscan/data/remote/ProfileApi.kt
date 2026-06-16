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

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.Part
import retrofit2.http.PartMap

interface ProfileApi {
    @GET("api/user/profile")
    suspend fun getProfile(): Response<ProfileResponse>

    @Multipart
    @PUT("api/user/profile")
    suspend fun updateProfile(
        @Part profilePic: MultipartBody.Part?,
        @PartMap fields: Map<String, @JvmSuppressWildcards RequestBody>
    ): Response<ProfileResponse>
}
