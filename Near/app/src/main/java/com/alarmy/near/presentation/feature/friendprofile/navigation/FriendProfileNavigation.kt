package com.alarmy.near.presentation.feature.friendprofile.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.alarmy.near.model.Friend
import com.alarmy.near.presentation.feature.friendprofile.FriendProfileRoute
import kotlinx.serialization.Serializable

@Serializable
data class RouteFriendProfile(
    val friendId: String,
)

fun NavController.navigateToFriendProfile(
    friendId: String,
    navOptions: NavOptions? = null,
) {
    navigate(RouteFriendProfile(friendId), navOptions)
}

fun NavGraphBuilder.friendProfileNavGraph(
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
    onClickBackButton: () -> Unit,
    onEditFriendInfo: (Friend) -> Unit = {},
    onClickCallButton: (phoneNumber: String) -> Unit = {},
    onClickMessageButton: (phoneNumber: String) -> Unit = {},
) {
    composable<RouteFriendProfile> { backStackEntry ->
        FriendProfileRoute(
            onShowErrorSnackBar = onShowErrorSnackBar,
            onClickBackButton = onClickBackButton,
            onEditFriendInfo = onEditFriendInfo,
            onClickCallButton = onClickCallButton,
            onClickMessageButton = onClickMessageButton,
        )
    }
}
