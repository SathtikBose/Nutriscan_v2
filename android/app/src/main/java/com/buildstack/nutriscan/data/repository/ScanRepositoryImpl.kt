package com.buildstack.nutriscan.data.repository

import com.buildstack.nutriscan.data.remote.ScanApiService
import com.buildstack.nutriscan.data.remote.dto.ScanResultDto
import com.buildstack.nutriscan.domain.model.NutritionSummary
import com.buildstack.nutriscan.domain.model.ScanResult
import com.buildstack.nutriscan.domain.repository.ScanRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ScanRepositoryImpl @Inject constructor(
    private val api: ScanApiService,
    private val dao: com.buildstack.nutriscan.data.local.ScanDao,
    private val gson: com.google.gson.Gson
) : ScanRepository {

    override suspend fun analyzeFood(imageFile: File): Result<ScanResult> {
        return try {
            val requestFile = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("image", imageFile.name, requestFile)

            val response = api.analyzeFood(body)
            if (response.isSuccessful && response.body()?.success == true) {
                val dto = response.body()!!.data!!
                val domainModel = dto.toDomainModel()
                // Cache locally
                dao.insertScan(com.buildstack.nutriscan.data.local.entity.ScanEntity.fromDomainModel(domainModel, gson))
                Result.success(domainModel)
            } else {
                Result.failure(Exception("Failed to analyze image"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getScanById(id: String): Result<ScanResult> {
        return try {
            // First check local DB
            val localScan = dao.getScanById(id)
            if (localScan != null) {
                return Result.success(localScan.toDomainModel(gson))
            }

            // Fallback to API if not in DB
            val response = api.getScanById(id)
            if (response.isSuccessful && response.body()?.success == true) {
                val dto = response.body()!!.data!!
                val domainModel = dto.toDomainModel()
                // Cache it
                dao.insertScan(com.buildstack.nutriscan.data.local.entity.ScanEntity.fromDomainModel(domainModel, gson))
                Result.success(domainModel)
            } else {
                Result.failure(Exception("Failed to fetch scan details"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getScanHistory(): Flow<List<ScanResult>> {
        return dao.getAllScans().map { entities ->
            entities.map { it.toDomainModel(gson) }
        }
    }

    override suspend fun syncHistory(): Result<Unit> {
        return try {
            val response = api.getAllScans()
            if (response.isSuccessful && response.body()?.success == true) {
                val remoteScans = response.body()!!.data.map { it.toDomainModel() }
                val entities = remoteScans.map { com.buildstack.nutriscan.data.local.entity.ScanEntity.fromDomainModel(it, gson) }
                dao.insertScans(entities)
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to sync history"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun ScanResultDto.toDomainModel(): ScanResult {
        return ScanResult(
            id = _id,
            productImage = productImage,
            productName = productName,
            productDescription = productDescription,
            productScore = productScore,
            productScoreStatus = productScoreStatus,
            productScoreColour = productScoreColour,
            goodIngredients = goodIngredients,
            badIngredients = badIngredients,
            allIngredients = allIngredients,
            allergenWarnings = allergenWarnings,
            severity = severity,
            betterAlternatives = betterAlternatives,
            nutritionSummary = NutritionSummary(
                calories = nutritionSummary.calories,
                protein = nutritionSummary.protein,
                fat = nutritionSummary.fat,
                carbs = nutritionSummary.carbs,
                sugar = nutritionSummary.sugar,
                fiber = nutritionSummary.fiber,
                sodium = nutritionSummary.sodium,
                servingSize = nutritionSummary.servingSize
            ),
            recommendation = recommendation,
            explanation = explanation,
            date = createdAt
        )
    }
}
