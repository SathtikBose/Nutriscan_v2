package com.buildstack.nutriscan.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.buildstack.nutriscan.presentation.auth.*

import com.buildstack.nutriscan.presentation.home.HomeScreen

import com.buildstack.nutriscan.presentation.scan.ScanScreen

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
                    onNavigateToOtp = { backStack.add(OtpVerification) }
                )
            }
            entry<OtpVerification> {
                OtpVerificationScreen(
                    onNavigateBack = { backStack.removeLastOrNull() },
                    onNavigateToResetPassword = {
                        backStack.removeLastOrNull()
                        backStack.add(ResetPassword)
                    }
                )
            }
            entry<ResetPassword> {
                ResetPasswordScreen(
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
                        backStack.add(Result(resultId))
                    }
                )
            }
            entry<Result> { args ->
                com.buildstack.nutriscan.presentation.result.ResultScreen(
                    resultId = args.id,
                    onNavigateBack = { backStack.removeLastOrNull() }
                )
            }
            entry<History> {
                com.buildstack.nutriscan.presentation.history.HistoryScreen(
                    onNavigateToHome = {
                        backStack.clear()
                        backStack.add(Home)
                    },
                    onNavigateToScan = { backStack.add(Scan) },
                    onNavigateToProfile = { backStack.add(Profile) },
                    onNavigateToResult = { resultId ->
                        backStack.add(Result(resultId))
                    }
                )
            }
            entry<Profile> {
                com.buildstack.nutriscan.presentation.profile.ProfileScreen(
                    onNavigateToLogin = {
                        backStack.clear()
                        backStack.add(Login)
                    },
                    onNavigateToHome = {
                        backStack.clear()
                        backStack.add(Home)
                    },
                    onNavigateToScan = { backStack.add(Scan) },
                    onNavigateToHistory = { backStack.add(History) }
                )
            }
        }
    )
}
