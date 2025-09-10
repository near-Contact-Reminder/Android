package com.alarmy.near.presentation.feature.myprofile.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.alarmy.near.presentation.feature.myprofile.MyProfileRoute
import com.alarmy.near.presentation.feature.myprofile.WithdrawRoute
import kotlinx.serialization.Serializable

@Serializable
object RouteMyProfile

@Serializable
object RouteWithdraw

fun NavController.navigateToMyProfile() {
    navigate(RouteMyProfile)
}

fun NavController.navigateToWithdraw() {
    navigate(RouteWithdraw)
}

fun NavGraphBuilder.myProfileNavGraph(
    onNavigateBack: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToWithdraw: () -> Unit,
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
) {
    composable<RouteMyProfile> {
        MyProfileRoute(
            onNavigateBack = onNavigateBack,
            onNavigateToLogin = onNavigateToLogin,
            onNavigateToWithdraw = onNavigateToWithdraw,
            onShowErrorSnackBar = onShowErrorSnackBar,
        )
    }

    composable<RouteWithdraw> {
        WithdrawRoute(
            onNavigateToLogin = onNavigateToLogin,
        )
    }
}
