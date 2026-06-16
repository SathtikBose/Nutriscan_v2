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
data class OtpVerification(val email: String) : NavKey

@Serializable
data class ResetPassword(val email: String, val otp: String) : NavKey

@Serializable
data object Home : NavKey

@Serializable
data object Scan : NavKey

@Serializable
object History : NavKey

@Serializable
object Profile : NavKey

@Serializable
object ChangePassword : NavKey

@Serializable
data class ResultRoute(val id: String) : NavKey
