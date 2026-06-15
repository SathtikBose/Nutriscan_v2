package com.buildstack.nutriscan.domain.repository

import com.buildstack.nutriscan.domain.model.ScanResult
import java.io.File

interface ScanRepository {
    suspend fun analyzeFood(imageFile: File): Result<ScanResult>
    suspend fun getScanById(id: String): Result<ScanResult>
    fun getScanHistory(): kotlinx.coroutines.flow.Flow<List<ScanResult>>
    suspend fun syncHistory(): Result<Unit>
}
