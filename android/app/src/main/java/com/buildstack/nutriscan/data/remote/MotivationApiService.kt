package com.buildstack.nutriscan.data.remote

import retrofit2.Response
import retrofit2.http.GET

data class MotivationResponse(
    val success: Boolean,
    val data: MotivationData?
)

data class MotivationData(
    val message: String
)

interface MotivationApiService {
    @GET("api/motivation/daily")
    suspend fun getDailyMotivation(): Response<MotivationResponse>
}
