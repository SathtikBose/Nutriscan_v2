package com.buildstack.nutriscan.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.buildstack.nutriscan.domain.model.NutritionSummary
import com.buildstack.nutriscan.domain.model.ScanResult
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = "scans")
data class ScanEntity(
    @PrimaryKey
    val id: String,
    val productImage: String,
    val productName: String,
    val productDescription: String,
    val productScore: Int,
    val productScoreStatus: String,
    val productScoreColour: String?,
    val goodIngredientsJson: String,
    val badIngredientsJson: String,
    val allIngredientsJson: String,
    val allergenWarningsJson: String,
    val severity: String,
    val betterAlternativesJson: String,
    val nutritionSummaryJson: String,
    val recommendation: String,
    val explanation: String,
    val date: String
) {
    fun toDomainModel(gson: Gson): ScanResult {
        val listType = object : TypeToken<List<String>>() {}.type
        
        return ScanResult(
            id = id,
            productImage = productImage,
            productName = productName,
            productDescription = productDescription,
            productScore = productScore,
            productScoreStatus = productScoreStatus,
            productScoreColour = productScoreColour,
            goodIngredients = gson.fromJson(goodIngredientsJson, listType),
            badIngredients = gson.fromJson(badIngredientsJson, listType),
            allIngredients = gson.fromJson(allIngredientsJson, listType),
            allergenWarnings = gson.fromJson(allergenWarningsJson, listType),
            severity = severity,
            betterAlternatives = gson.fromJson(betterAlternativesJson, listType),
            nutritionSummary = gson.fromJson(nutritionSummaryJson, NutritionSummary::class.java),
            recommendation = recommendation,
            explanation = explanation,
            date = date
        )
    }

    companion object {
        fun fromDomainModel(scan: ScanResult, gson: Gson): ScanEntity {
            return ScanEntity(
                id = scan.id,
                productImage = scan.productImage,
                productName = scan.productName,
                productDescription = scan.productDescription,
                productScore = scan.productScore,
                productScoreStatus = scan.productScoreStatus,
                productScoreColour = scan.productScoreColour,
                goodIngredientsJson = gson.toJson(scan.goodIngredients),
                badIngredientsJson = gson.toJson(scan.badIngredients),
                allIngredientsJson = gson.toJson(scan.allIngredients),
                allergenWarningsJson = gson.toJson(scan.allergenWarnings),
                severity = scan.severity,
                betterAlternativesJson = gson.toJson(scan.betterAlternatives),
                nutritionSummaryJson = gson.toJson(scan.nutritionSummary),
                recommendation = scan.recommendation,
                explanation = scan.explanation,
                date = scan.date
            )
        }
    }
}
