package com.alarmy.near.presentation.feature.friendprofileedittor.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.alarmy.near.presentation.feature.friendprofile.FriendProfileRoute
import com.alarmy.near.presentation.feature.friendprofileedittor.FriendProfileEditorRoute
import kotlinx.serialization.Serializable

@Serializable
object RouteFriendProfileEditor

fun NavController.navigateToFriendProfileEditor(navOptions: NavOptions) {
    navigate(RouteFriendProfileEditor, navOptions)
}

fun NavGraphBuilder.friendProfileEditorNavGraph(onShowErrorSnackBar: (throwable: Throwable?) -> Unit) {
    composable<RouteFriendProfileEditor> { backStackEntry ->
        FriendProfileEditorRoute(
            onShowErrorSnackBar = onShowErrorSnackBar,
        )
    }
}
