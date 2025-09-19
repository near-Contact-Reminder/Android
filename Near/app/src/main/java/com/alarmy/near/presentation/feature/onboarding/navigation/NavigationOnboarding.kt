package com.alarmy.near.presentation.feature.onboarding.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.alarmy.near.presentation.feature.onboarding.OnboardingScreen
import kotlinx.serialization.Serializable

@Serializable
object RouteOnboarding

// 온보딩 화면으로 네비게이션
fun NavController.navigateToOnboarding() {
    navigate(RouteOnboarding) {
        popUpTo(0) { inclusive = true }
    }
}

// 온보딩 네비게이션 그래프
fun NavGraphBuilder.onboardingNavGraph(onNavigateToLogin: () -> Unit) {
    composable<RouteOnboarding> {
        OnboardingScreen(
            onNavigateToLogin = onNavigateToLogin,
        )
    }
}
