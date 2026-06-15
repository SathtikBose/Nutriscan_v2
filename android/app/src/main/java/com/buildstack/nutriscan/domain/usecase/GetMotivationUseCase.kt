package com.buildstack.nutriscan.domain.usecase

import com.buildstack.nutriscan.data.remote.MotivationApiService
import javax.inject.Inject

class GetMotivationUseCase @Inject constructor(
    private val motivationApiService: MotivationApiService
) {
    suspend operator fun invoke(): Result<String> {
        return try {
            val response = motivationApiService.getDailyMotivation()
            if (response.isSuccessful) {
                Result.success(response.body()?.data?.message ?: "Healthy eating starts today!")
            } else {
                Result.failure(Exception("Failed to fetch motivation"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
