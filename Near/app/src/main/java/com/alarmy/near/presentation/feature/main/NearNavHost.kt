package com.alarmy.near.presentation.feature.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import com.alarmy.near.presentation.feature.friendprofile.navigation.friendProfileNavGraph
import com.alarmy.near.presentation.feature.friendprofile.navigation.navigateToFriendProfile
import com.alarmy.near.presentation.feature.friendprofileedittor.navigation.friendProfileEditorNavGraph
import com.alarmy.near.presentation.feature.home.navigation.homeNavGraph
import com.alarmy.near.presentation.feature.home.navigation.navigateToHome
import com.alarmy.near.presentation.feature.login.navigation.RouteLogin
import com.alarmy.near.presentation.feature.login.navigation.loginNavGraph
import com.alarmy.near.presentation.feature.login.navigation.navigateToLogin
import com.alarmy.near.presentation.feature.splash.navigation.RouteSplash
import com.alarmy.near.presentation.feature.splash.navigation.splashNavGraph

@Composable
internal fun NearNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onShowSnackbar: (Throwable?) -> Unit = { _ -> },
) {
    /*
     * 화면 이동 및 구성을 위한 컴포저블 함수입니다.
     * */
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = RouteSplash,
    ) {

        // 스플래쉬 화면 NavGraph
        splashNavGraph(
            onNavigateToLogin = {
                navController.navigateToLogin(
                    navOptions = navOptions {
                        popUpTo(RouteSplash) { inclusive = true }
                    }
                )
            },
            onNavigateToHome = {
                navController.navigateToHome(
                    navOptions = navOptions {
                        popUpTo(RouteSplash) { inclusive = true }
                    }
                )
            }
        )

        // 로그인 화면 NavGraph
        loginNavGraph(
            onShowErrorSnackBar = onShowSnackbar,
            onNavigateToHome = {
                navController.navigateToHome(
                    navOptions = navOptions {
                        popUpTo(RouteLogin) { inclusive = true }
                    }
                )
            }
        )

        // 홈 화면 NavGraph
        homeNavGraph(
            onShowErrorSnackBar = onShowSnackbar,
            onContactClick = { contactId ->
                navController.navigateToFriendProfile(friendId = contactId)
            },
            onMyPageClick = {},
            onAlarmClick = {},
            onAddContactClick = {},
        )

        // 친구 프로필 화면 NavGraph
        friendProfileNavGraph(
            onShowErrorSnackBar = onShowSnackbar,
            onClickBackButton = {
                navController.popBackStack()
            }
        )

        // 친구 프로필 편집 화면 NavGraph
        friendProfileEditorNavGraph(
            onShowErrorSnackBar = onShowSnackbar,
            onClickBackButton = {
                navController.popBackStack()
            }
        )
    }
}
