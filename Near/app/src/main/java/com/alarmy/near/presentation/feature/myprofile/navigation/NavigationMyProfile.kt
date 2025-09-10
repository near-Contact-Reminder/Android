package com.alarmy.near.presentation.feature.myprofile.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.alarmy.near.presentation.feature.myprofile.MyProfileRoute
import kotlinx.serialization.Serializable

@Serializable
object RouteMyProfile

fun NavController.navigateToMyProfile() {
    navigate(RouteMyProfile)
}

fun NavGraphBuilder.myProfileNavGraph(onNavigateBack: () -> Unit) {
    composable<RouteMyProfile> {
        MyProfileRoute(
            onNavigateBack = onNavigateBack,
        )
    }
}
