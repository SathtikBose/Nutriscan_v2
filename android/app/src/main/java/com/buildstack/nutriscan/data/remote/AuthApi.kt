package com.buildstack.nutriscan.data.remote

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Response

data class AuthResponse(
    val success: Boolean,
    val token: String?,
    val refreshToken: String?,
    val message: String?,
    val user: UserDto?
)

data class UserDto(
    val id: String,
    val name: String,
    val email: String
)

interface AuthApi {
    @POST("api/auth/login")
    suspend fun login(@Body request: Map<String, String>): Response<AuthResponse>

    @POST("api/auth/signup")
    suspend fun signup(@Body request: Map<String, String>): Response<AuthResponse>

    @POST("api/auth/forgot-password")
    suspend fun forgotPassword(@Body request: Map<String, String>): Response<AuthResponse>

    @POST("api/auth/verify-otp")
    suspend fun verifyOtp(@Body request: Map<String, String>): Response<AuthResponse>

    @POST("api/auth/reset-password")
    suspend fun resetPassword(@Body request: Map<String, String>): Response<AuthResponse>
}
