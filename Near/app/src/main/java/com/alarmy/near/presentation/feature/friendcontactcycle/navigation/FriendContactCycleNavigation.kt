package com.alarmy.near.presentation.feature.friendcontactcycle.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.alarmy.near.presentation.feature.friendcontactcycle.FriendContactCycleRoute
import kotlinx.serialization.Serializable

@Serializable
object RouteFriendContactCycle

/**
 * 친구 연락처 주기 설정 화면으로 이동하는 확장 함수
 */
fun NavController.navigateToFriendContactCycle(navOptions: NavOptions? = null) {
    navigate(RouteFriendContactCycle, navOptions)
}

/**
 * 친구 연락처 주기 설정 화면 NavGraph 정의
 */
fun NavGraphBuilder.friendContactCycleNavGraph(
    onNavigateToHome: () -> Unit,
    onNavigateToContact: () -> Unit = {},
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit = { _ -> },
) {
    composable<RouteFriendContactCycle> { backStackEntry ->
        FriendContactCycleRoute(
            navBackStackEntry = backStackEntry,
            onNavigateToHome = onNavigateToHome,
            onNavigateToContact = onNavigateToContact,
        )
    }
}
