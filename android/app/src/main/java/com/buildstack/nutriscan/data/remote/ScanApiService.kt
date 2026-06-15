package com.buildstack.nutriscan.data.remote

import com.buildstack.nutriscan.data.remote.dto.ScanResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ScanApiService {
    @Multipart
    @POST("api/scan/analyze")
    suspend fun analyzeFood(
        @Part image: MultipartBody.Part
    ): Response<ScanResponse>

    @retrofit2.http.GET("api/scan/{id}")
    suspend fun getScanById(
        @retrofit2.http.Path("id") id: String
    ): Response<ScanResponse>

    @retrofit2.http.GET("api/scan/history")
    suspend fun getAllScans(): Response<com.buildstack.nutriscan.data.remote.dto.HistoryResponse>
}
