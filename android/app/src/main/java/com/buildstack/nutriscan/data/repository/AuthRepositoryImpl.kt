package com.buildstack.nutriscan.data.repository

import com.buildstack.nutriscan.data.remote.AuthApi
import com.buildstack.nutriscan.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi
) : AuthRepository {

    private var token: String? = null // Temporary in-memory token

    override suspend fun login(email: String, password: String): Result<Boolean> {
        return try {
            val response = api.login(mapOf("email" to email, "password" to password))
            if (response.isSuccessful && response.body()?.success == true) {
                token = response.body()?.token
                Result.success(true)
            } else {
                Result.failure(Exception(response.body()?.message ?: "Login failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signup(name: String, email: String, password: String): Result<Boolean> {
        return try {
            val response = api.signup(mapOf("name" to name, "email" to email, "password" to password))
            if (response.isSuccessful && response.body()?.success == true) {
                token = response.body()?.token
                Result.success(true)
            } else {
                Result.failure(Exception(response.body()?.message ?: "Signup failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun forgotPassword(email: String): Result<Boolean> {
        return try {
            val response = api.forgotPassword(mapOf("email" to email))
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(true)
            } else {
                Result.failure(Exception(response.body()?.message ?: "Forgot password failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun verifyOtp(email: String, otp: String): Result<Boolean> {
        return try {
            val response = api.verifyOtp(mapOf("email" to email, "otp" to otp))
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(true)
            } else {
                Result.failure(Exception(response.body()?.message ?: "OTP verification failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun resetPassword(email: String, otp: String, password: String): Result<Boolean> {
        return try {
            val response = api.resetPassword(mapOf("email" to email, "otp" to otp, "password" to password))
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(true)
            } else {
                Result.failure(Exception(response.body()?.message ?: "Reset password failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun isLoggedIn(): Boolean {
        return token != null
    }

    override suspend fun logout() {
        token = null
    }
}
