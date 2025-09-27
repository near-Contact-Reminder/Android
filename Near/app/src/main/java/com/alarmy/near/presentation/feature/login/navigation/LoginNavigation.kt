package com.alarmy.near.presentation.feature.login.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.alarmy.near.presentation.feature.login.LoginRoute
import kotlinx.serialization.Serializable

@Serializable
object RouteLogin

// 로그인 화면으로 이동하는 확장 함수
fun NavController.navigateToLogin(navOptions: NavOptions? = null) {
    navigate(RouteLogin, navOptions)
}

// 로그인 화면 NavGraph 정의
fun NavGraphBuilder.loginNavGraph(
    onNavigateToHome: () -> Unit,
    onNavigateToTerms: (title: String, url: String) -> Unit,
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
) {
    composable<RouteLogin> {
        LoginRoute(
            onNavigateToHome = onNavigateToHome,
            onNavigateToWebView = onNavigateToTerms,
            onShowErrorSnackBar = onShowErrorSnackBar,
        )
    }
}
