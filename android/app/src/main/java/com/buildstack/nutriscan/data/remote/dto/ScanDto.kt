package com.buildstack.nutriscan.data.remote.dto

data class NutritionSummaryDto(
    val calories: String,
    val protein: String,
    val fat: String,
    val carbs: String,
    val sugar: String,
    val fiber: String,
    val sodium: String,
    val servingSize: String
)

data class ScanResultDto(
    val _id: String,
    val userId: String,
    val productImage: String,
    val productName: String,
    val productDescription: String,
    val productScore: Int,
    val productScoreStatus: String,
    val productScoreColour: String?,
    val goodIngredients: List<String>,
    val badIngredients: List<String>,
    val allIngredients: List<String>,
    val allergenWarnings: List<String>,
    val severity: String,
    val betterAlternatives: List<String>,
    val nutritionSummary: NutritionSummaryDto,
    val recommendation: String,
    val explanation: String,
    val createdAt: String,
    val updatedAt: String
)

data class ScanResponse(
    val success: Boolean,
    val data: ScanResultDto?
)
