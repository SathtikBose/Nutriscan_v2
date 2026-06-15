package com.buildstack.nutriscan.domain.model

data class NutritionSummary(
    val calories: String,
    val protein: String,
    val fat: String,
    val carbs: String,
    val sugar: String,
    val fiber: String,
    val sodium: String,
    val servingSize: String
)

data class ScanResult(
    val id: String,
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
    val nutritionSummary: NutritionSummary,
    val recommendation: String,
    val explanation: String,
    val date: String
)
