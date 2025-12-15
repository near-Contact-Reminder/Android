package com.alarmy.near.presentation.feature.home.navigation

import android.os.Parcelable
import android.util.Log
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.alarmy.near.presentation.feature.home.HomeRoute
import com.alarmy.near.presentation.feature.home.HomeViewModel
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

const val HOME_FRIEND_DELETE_COMPLETE_KEY = "HOME_FRIEND_DELETE_COMPLETE_KEY"
const val HOME_RESULT_EVENT = "HOME_RESULT_EVENT"

@Serializable
object RouteHome

sealed interface HomeNavigationEvent : Parcelable {
    @Parcelize
    @Serializable
    data class FriendDeleted(
        val friendId: String,
    ) : HomeNavigationEvent

    @Parcelize
    @Serializable
    data class FriendReminderUpdated(
        val friendId: String,
        val friendReminderUpdatedAt: String?,
    ) : HomeNavigationEvent
}

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
    onMonthlyReminderAllClick: () -> Unit = {},
) {
    composable<RouteHome> { backStackEntry ->
        val viewModel: HomeViewModel = hiltViewModel()
        val homeEvent = backStackEntry.savedStateHandle.get<HomeNavigationEvent>(HOME_RESULT_EVENT)
        LaunchedEffect(homeEvent) {
            when (homeEvent) {
                is HomeNavigationEvent.FriendDeleted -> {
                    viewModel.deleteFriend(homeEvent.friendId)
                }

                is HomeNavigationEvent.FriendReminderUpdated -> {
                    viewModel.updateFriendReminder(homeEvent.friendId, homeEvent.friendReminderUpdatedAt)
                }
                null -> Unit
            }
        }

        HomeRoute(
            onShowErrorSnackBar = onShowErrorSnackBar,
            onContactClick = onContactClick,
            onAlarmClick = onAlarmClick,
            onMyPageClick = onMyPageClick,
            onAddContactClick = onAddContactClick,
            onMonthlyReminderAllClick = onMonthlyReminderAllClick,
        )
    }
}
