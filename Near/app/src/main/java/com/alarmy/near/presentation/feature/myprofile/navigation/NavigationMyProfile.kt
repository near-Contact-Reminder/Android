package com.alarmy.near.presentation.feature.myprofile.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.alarmy.near.presentation.feature.myprofile.MyProfileRoute
import com.alarmy.near.presentation.feature.myprofile.WithdrawRoute
import com.alarmy.near.presentation.ui.component.WebViewFrame
import kotlinx.serialization.Serializable

@Serializable
object RouteMyProfile

@Serializable
data class RouteWithdraw(
    val nickname: String,
)

@Serializable
data class RouteWebView(
    val title: String,
    val url: String,
)

fun NavController.navigateToMyProfile() {
    navigate(RouteMyProfile)
}

fun NavController.navigateToWithdraw(nickname: String) {
    navigate(RouteWithdraw(nickname))
}

fun NavController.navigateToWebView(
    title: String,
    url: String,
) {
    navigate(RouteWebView(title, url))
}

fun NavGraphBuilder.myProfileNavGraph(
    onNavigateBack: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToWithdraw: (nickname: String) -> Unit,
    onNavigateToTerms: (title: String, url: String) -> Unit,
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
) {
    composable<RouteMyProfile> {
        MyProfileRoute(
            onNavigateBack = onNavigateBack,
            onNavigateToLogin = onNavigateToLogin,
            onNavigateToWithdraw = onNavigateToWithdraw,
            onNavigateToTerms = onNavigateToTerms,
            onShowErrorSnackBar = onShowErrorSnackBar,
        )
    }

    composable<RouteWithdraw> {
        WithdrawRoute(
            onNavigateBack = onNavigateBack,
            onNavigateToLogin = onNavigateToLogin,
        )
    }

    composable<RouteWebView> { backStackEntry ->
        val title = backStackEntry.arguments?.getString("title") ?: ""
        val url = backStackEntry.arguments?.getString("url") ?: ""

        WebViewFrame(
            onNavigateBack = onNavigateBack,
            title = title,
            url = url,
        )
    }
}
