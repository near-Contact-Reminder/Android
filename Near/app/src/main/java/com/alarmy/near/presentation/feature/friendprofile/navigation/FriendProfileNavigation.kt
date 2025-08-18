package com.alarmy.near.presentation.feature.friendprofile.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.alarmy.near.presentation.feature.friendprofile.FriendProfileRoute
import kotlinx.serialization.Serializable

@Serializable
object RouteFriendProfile

fun NavController.navigateToFriendProfile(navOptions: NavOptions) {
    navigate(RouteFriendProfile, navOptions)
}

fun NavGraphBuilder.friendProfileNavGraph(
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
    onClickBackButton: () -> Unit,
) {
    composable<RouteFriendProfile> { backStackEntry ->
        FriendProfileRoute(
            onShowErrorSnackBar = onShowErrorSnackBar,
            onClickBackButton = onClickBackButton,
        )
    }
}
