package com.buildstack.nutriscan.domain.model

data class UserProfile(
    val id: String,
    val name: String,
    val email: String,
    val age: Int?,
    val weight: Float?,
    val height: Float?,
    val allergies: List<String>,
    val dietaryPreferences: List<String>
)
