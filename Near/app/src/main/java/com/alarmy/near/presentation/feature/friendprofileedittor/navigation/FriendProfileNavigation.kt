package com.alarmy.near.presentation.feature.friendprofileedittor.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.alarmy.near.model.Friend
import com.alarmy.near.presentation.feature.friendprofileedittor.FriendProfileEditorRoute
import kotlinx.serialization.Serializable

@Serializable
data class RouteFriendProfileEditor(
    val friend: Friend,
)

fun NavController.navigateToFriendProfileEditor(navOptions: NavOptions) {
    navigate(RouteFriendProfileEditor, navOptions)
}

fun NavGraphBuilder.friendProfileEditorNavGraph(
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
    onClickBackButton: () -> Unit = {},
) {
    composable<RouteFriendProfileEditor> { backStackEntry ->
        FriendProfileEditorRoute(
            onShowErrorSnackBar = onShowErrorSnackBar,
            onClickBackButton = onClickBackButton,
        )
    }
}
