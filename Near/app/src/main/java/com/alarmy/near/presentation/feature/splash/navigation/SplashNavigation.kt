package com.alarmy.near.presentation.feature.splash.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.alarmy.near.presentation.feature.splash.SplashScreen
import kotlinx.serialization.Serializable

@Serializable
object RouteSplash

fun NavGraphBuilder.splashNavGraph(
    onNavigateToLogin: () -> Unit,
    onNavigateToHome: () -> Unit,
) {
    composable<RouteSplash> {
        SplashScreen(
            onNavigateToLogin = onNavigateToLogin,
            onNavigateToHome = onNavigateToHome,
        )
    }
}
