package com.alarmy.near.presentation.feature.home.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.alarmy.near.model.Friend
import com.alarmy.near.presentation.feature.friendprofile.FriendProfileViewModel
import com.alarmy.near.presentation.feature.friendprofileedittor.navigation.FRIEND_PROFILE_EDIT_COMPLETE_KEY
import com.alarmy.near.presentation.feature.home.HomeRoute
import com.alarmy.near.presentation.feature.home.HomeViewModel
import kotlinx.serialization.Serializable

const val HOME_FRIEND_DELETE_COMPLETE_KEY = "HOME_FRIEND_DELETE_COMPLETE_KEY"

@Serializable
object RouteHome

/*
* 추후 홈으로 화면 이동이 필요할 때 이 함수를 사용합니다.
* */
fun NavController.navigateToHome(navOptions: NavOptions) {
    navigate(RouteHome, navOptions)
}

fun NavGraphBuilder.homeNavGraph(
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
    onContactClick: (String) -> Unit = {},
    onAlarmClick: () -> Unit = {},
    onMyPageClick: () -> Unit = {},
    onAddContactClick: () -> Unit = {},
) {
    composable<RouteHome> { backStackEntry ->
        val viewModel: HomeViewModel = hiltViewModel()
        val friendId: String? = backStackEntry.savedStateHandle.get<String>(HOME_FRIEND_DELETE_COMPLETE_KEY)
        friendId?.let {
            viewModel.deleteFriend(it)
        }
        HomeRoute(
            onShowErrorSnackBar = onShowErrorSnackBar,
            onContactClick = onContactClick,
            onAlarmClick = onAlarmClick,
            onMyPageClick = onMyPageClick,
            onAddContactClick = onAddContactClick,
        )
    }
}
