package com.alarmy.near.presentation.feature.main

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import com.alarmy.near.presentation.feature.contact.navigation.CONTACT_SELECTION_COMPLETE_KEY
import com.alarmy.near.presentation.feature.contact.navigation.RouteContact
import com.alarmy.near.presentation.feature.contact.navigation.contactNavGraph
import com.alarmy.near.presentation.feature.friendprofile.navigation.friendProfileNavGraph
import com.alarmy.near.presentation.feature.friendprofile.navigation.navigateToFriendProfile
import com.alarmy.near.presentation.feature.friendprofileedittor.navigation.FRIEND_PROFILE_EDIT_COMPLETE_KEY
import com.alarmy.near.presentation.feature.friendprofileedittor.navigation.friendProfileEditorNavGraph
import com.alarmy.near.presentation.feature.friendprofileedittor.navigation.navigateToFriendProfileEditor
import com.alarmy.near.presentation.feature.home.navigation.homeNavGraph
import com.alarmy.near.presentation.feature.home.navigation.navigateToHome
import com.alarmy.near.presentation.feature.login.navigation.RouteLogin
import com.alarmy.near.presentation.feature.login.navigation.loginNavGraph
import com.alarmy.near.presentation.feature.login.navigation.navigateToLogin
import com.alarmy.near.presentation.feature.myprofile.navigation.myProfileNavGraph
import com.alarmy.near.presentation.feature.myprofile.navigation.navigateToMyProfile
import com.alarmy.near.presentation.feature.myprofile.navigation.navigateToWebView
import com.alarmy.near.presentation.feature.myprofile.navigation.navigateToWithdraw
import com.alarmy.near.presentation.feature.onboarding.navigation.RouteOnboarding
import com.alarmy.near.presentation.feature.onboarding.navigation.onboardingNavGraph
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
internal fun NearNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: Any, // 나중에 모든 루트를 sealed로 구성하면 sealed 타입으로 변경
    onShowSnackbar: (Throwable?) -> Unit = { _ -> },
) {
    val context = LocalContext.current

    /*
     * 화면 이동 및 구성을 위한 컴포저블 함수입니다.
     * startDestination 파라미터로 받은 시작 화면으로 이동합니다.
     * */
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
    ) {
        // 온보딩 화면 NavGraph
        onboardingNavGraph(
            onNavigateToLogin = {
                navController.navigateToLogin(
                    navOptions =
                        navOptions {
                            popUpTo(RouteOnboarding) { inclusive = true }
                        },
                )
            },
        )

        // 로그인 화면 NavGraph
        loginNavGraph(
            onShowErrorSnackBar = onShowSnackbar,
            onNavigateToHome = {
                navController.navigateToHome(
                    navOptions =
                        navOptions {
                            popUpTo(RouteLogin) { inclusive = true }
                        },
                )
            },
        )

        // 홈 화면 NavGraph
        homeNavGraph(
            onShowErrorSnackBar = onShowSnackbar,
            onContactClick = { contactId ->
                navController.navigateToFriendProfile(friendId = contactId)
            },
            onMyPageClick = { navController.navigateToMyProfile() },
            onAlarmClick = {},
            onAddContactClick = {},
        )

        myProfileNavGraph(
            onNavigateBack = {
                navController.popBackStack()
            },
            onNavigateToLogin = {
                navController.navigateToLogin(
                    navOptions =
                        navOptions {
                            popUpTo(0) { inclusive = true }
                        },
                )
            },
            onNavigateToWithdraw = { nickname ->
                navController.navigateToWithdraw(nickname)
            },
            onNavigateToTerms = { title, url ->
                navController.navigateToWebView(title, url)
            },
            onShowErrorSnackBar = onShowSnackbar,
        )

        // 친구 프로필 화면 NavGraph
        friendProfileNavGraph(onShowErrorSnackBar = onShowSnackbar, onClickBackButton = {
            navController.popBackStack()
        }, onClickCallButton = { phoneNumber ->
            val intent =
                Intent(Intent.ACTION_DIAL).apply {
                    data = "tel:$phoneNumber".toUri()
                }
            context.startActivity(intent)
        }, onClickMessageButton = { phoneNumber ->
            val intent =
                Intent(Intent.ACTION_VIEW).apply {
                    data = "sms:$phoneNumber".toUri()
                }
            context.startActivity(intent)
        }, onEditFriendInfo = {
            navController.navigateToFriendProfileEditor(
                friend =
                    it.copy(
                        imageUrl =
                            it.imageUrl?.let { imageUrl ->
                                URLEncoder.encode(
                                    imageUrl,
                                    StandardCharsets.UTF_8.toString(),
                                )
                            },
                    ),
            )
        })

        // 친구 프로필 편집 화면 NavGraph
        friendProfileEditorNavGraph(onShowErrorSnackBar = onShowSnackbar, onClickBackButton = {
            navController.popBackStack()
        }, onSuccessEdit = {
            navController.previousBackStackEntry?.savedStateHandle?.set(
                FRIEND_PROFILE_EDIT_COMPLETE_KEY,
                it,
            )
            navController.popBackStack()
        })

        // 로그인 화면 NavGraph
        loginNavGraph(
            onShowErrorSnackBar = onShowSnackbar,
            onNavigateToHome = {
                navController.navigateToHome(
                    navOptions =
                        navOptions {
                            popUpTo(RouteLogin) { inclusive = true }
                        },
                )
            },
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

        contactNavGraph(
            onShowErrorSnackBar = onShowSnackbar,
            onBackClick = {
                navController.popBackStack()
            },
            onCompletedSelection = {
                navController.previousBackStackEntry?.savedStateHandle?.set(
                    CONTACT_SELECTION_COMPLETE_KEY,
                    it,
                )
                navController.popBackStack()
            },
        )
    }
}
