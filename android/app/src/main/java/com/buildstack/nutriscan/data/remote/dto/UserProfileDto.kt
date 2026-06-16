package com.buildstack.nutriscan.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UserProfileDto(
    @SerializedName("_id") val id: String,
    val name: String,
    val email: String,
    val age: Int?,
    val weight: Float?,
    val height: Float?,
    val profilePic: String?,
    val allergies: List<String>?,
    val dietaryPreferences: List<String>?
) {
    fun toDomainModel() = com.buildstack.nutriscan.domain.model.UserProfile(
        id = id,
        name = name,
        email = email,
        age = age,
        weight = weight,
        height = height,
        profilePic = profilePic,
        allergies = allergies ?: emptyList(),
        dietaryPreferences = dietaryPreferences ?: emptyList()
    )
}

data class ProfileResponse(
    val success: Boolean,
    val data: UserProfileDto
)
