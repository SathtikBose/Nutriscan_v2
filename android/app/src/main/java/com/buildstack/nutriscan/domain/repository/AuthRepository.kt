package com.buildstack.nutriscan.domain.repository

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<Boolean>
    suspend fun signup(name: String, email: String, password: String): Result<Boolean>
    suspend fun forgotPassword(email: String): Result<Boolean>
    suspend fun verifyOtp(email: String, otp: String): Result<Boolean>
    suspend fun resetPassword(email: String, otp: String, password: String): Result<Boolean>
    suspend fun changePassword(currentPassword: String, newPassword: String): Result<Boolean>
    fun isLoggedIn(): Boolean
    suspend fun logout()
}
