package com.buildstack.nutriscan.presentation.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object Splash : NavKey

@Serializable
data object Login : NavKey

@Serializable
data object Signup : NavKey

@Serializable
data object ForgotPassword : NavKey

@Serializable
data object OtpVerification : NavKey

@Serializable
data object ResetPassword : NavKey

@Serializable
data object Home : NavKey

@Serializable
data object Scan : NavKey

@Serializable
object History : NavKey

@Serializable
object Profile : NavKey

@Serializable
data class Result(val id: String) : NavKey
