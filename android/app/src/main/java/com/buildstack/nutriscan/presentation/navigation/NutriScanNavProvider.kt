package com.buildstack.nutriscan.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay

import com.buildstack.nutriscan.presentation.auth.SplashScreen
import com.buildstack.nutriscan.presentation.auth.LoginScreen
import com.buildstack.nutriscan.presentation.auth.SignupScreen
import com.buildstack.nutriscan.presentation.auth.ForgotPasswordScreen
import com.buildstack.nutriscan.presentation.auth.OtpVerificationScreen
import com.buildstack.nutriscan.presentation.auth.ResetPasswordScreen
import com.buildstack.nutriscan.presentation.profile.ChangePasswordScreen

import com.buildstack.nutriscan.presentation.home.HomeScreen
import com.buildstack.nutriscan.presentation.scan.ScanScreen
import com.buildstack.nutriscan.presentation.history.HistoryScreen
import com.buildstack.nutriscan.presentation.profile.ProfileScreen
import com.buildstack.nutriscan.presentation.result.ResultScreen

@Composable
fun NutriScanNavProvider() {
    val backStack = rememberNavBackStack(Splash)

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<Splash> {
                SplashScreen(
                    onNavigateToLogin = {
                        backStack.removeLastOrNull()
                        backStack.add(Login)
                    },
                    onNavigateToHome = {
                        backStack.removeLastOrNull()
                        backStack.add(Home)
                    }
                )
            }
            entry<Login> {
                LoginScreen(
                    onNavigateToSignup = { backStack.add(Signup) },
                    onNavigateToForgotPassword = { backStack.add(ForgotPassword) },
                    onLoginSuccess = {
                        backStack.clear()
                        backStack.add(Home)
                    }
                )
            }
            entry<Signup> {
                SignupScreen(
                    onNavigateToLogin = { backStack.removeLastOrNull() },
                    onSignupSuccess = {
                        backStack.clear()
                        backStack.add(Home)
                    }
                )
            }
            entry<ForgotPassword> {
                ForgotPasswordScreen(
                    onNavigateBack = { backStack.removeLastOrNull() },
                    onNavigateToOtp = { email ->
                        backStack.add(OtpVerification(email = email))
                    }
                )
            }
            entry<OtpVerification> {
                val email = it.email
                OtpVerificationScreen(
                    email = email,
                    onNavigateBack = { backStack.removeLastOrNull() },
                    onNavigateToResetPassword = { otp ->
                        backStack.add(ResetPassword(email = email, otp = otp))
                    }
                )
            }
            entry<ResetPassword> {
                val email = it.email
                val otp = it.otp
                ResetPasswordScreen(
                    email = email,
                    otp = otp,
                    onNavigateBack = { backStack.removeLastOrNull() },
                    onResetSuccess = {
                        backStack.clear()
                        backStack.add(Login)
                    }
                )
            }
            entry<Home> {
                HomeScreen(
                    onNavigateToScan = { backStack.add(Scan) },
                    onNavigateToHistory = { backStack.add(History) },
                    onNavigateToProfile = { backStack.add(Profile) }
                )
            }
            entry<Scan> {
                ScanScreen(
                    onNavigateBack = { backStack.removeLastOrNull() },
                    onAnalysisComplete = { resultId ->
                        backStack.removeLastOrNull()
                        backStack.add(ResultRoute(resultId))
                    }
                )
            }
            entry<ResultRoute> { args ->
                ResultScreen(
                    resultId = args.id,
                    onNavigateBack = { backStack.removeLastOrNull() }
                )
            }
            entry<History> {
                HistoryScreen(
                    onNavigateToHome = {
                        backStack.clear()
                        backStack.add(Home)
                    },
                    onNavigateToScan = { backStack.add(Scan) },
                    onNavigateToProfile = { backStack.add(Profile) },
                    onNavigateToResult = { resultId ->
                        backStack.add(ResultRoute(resultId))
                    }
                )
            }
            entry<Profile> {
                ProfileScreen(
                    onNavigateToLogin = {
                        backStack.clear()
                        backStack.add(Login)
                    },
                    onNavigateToHome = {
                        backStack.clear()
                        backStack.add(Home)
                    },
                    onNavigateToScan = { backStack.add(Scan) },
                    onNavigateToHistory = { backStack.add(History) },
                    onNavigateToChangePassword = { backStack.add(ChangePassword) }
                )
            }
            entry<ChangePassword> {
                ChangePasswordScreen(
                    onNavigateBack = { backStack.removeLastOrNull() }
                )
            }
        }
    )
}
