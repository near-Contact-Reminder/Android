package com.alarmy.near.presentation.feature.main

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.alarmy.near.presentation.feature.friendprofile.navigation.friendProfileNavGraph
import com.alarmy.near.presentation.feature.friendprofile.navigation.navigateToFriendProfile
import com.alarmy.near.presentation.feature.friendprofileedittor.navigation.FRIEND_PROFILE_EDIT_COMPLETE_KEY
import com.alarmy.near.presentation.feature.friendprofileedittor.navigation.friendProfileEditorNavGraph
import com.alarmy.near.presentation.feature.friendprofileedittor.navigation.navigateToFriendProfileEditor
import com.alarmy.near.presentation.feature.home.navigation.RouteHome
import com.alarmy.near.presentation.feature.home.navigation.homeNavGraph

@Composable
internal fun NearNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onShowSnackbar: (Throwable?) -> Unit = { _ -> },
) {
    val context = LocalContext.current
    /*
     * 화면 이동 및 구성을 위한 컴포저블 함수입니다.
     * */
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = RouteHome,
    ) {
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
            navController.navigateToFriendProfileEditor(friend = it)
        })
        friendProfileEditorNavGraph(onShowErrorSnackBar = onShowSnackbar, onClickBackButton = {
        }, onSuccessEdit = {
            navController.previousBackStackEntry?.savedStateHandle?.set(
                FRIEND_PROFILE_EDIT_COMPLETE_KEY,
                it,
            )
            navController.popBackStack()
        })
        homeNavGraph(
            onShowErrorSnackBar = onShowSnackbar,
            onContactClick = { contactId ->
                navController.navigateToFriendProfile(friendId = contactId)
            },
            onMyPageClick = {},
            onAlarmClick = {},
            onAddContactClick = {},
        )
    }
}
