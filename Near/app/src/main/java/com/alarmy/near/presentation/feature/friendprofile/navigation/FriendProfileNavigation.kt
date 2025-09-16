package com.alarmy.near.presentation.feature.friendprofile.navigation

import android.os.Build
import android.os.Parcelable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import androidx.savedstate.SavedState
import com.alarmy.near.model.Friend
import com.alarmy.near.presentation.feature.friendprofile.FriendProfileRoute
import com.alarmy.near.presentation.feature.friendprofile.FriendProfileViewModel
import com.alarmy.near.presentation.feature.friendprofileedittor.navigation.FRIEND_PROFILE_EDIT_COMPLETE_KEY
import com.alarmy.near.presentation.feature.friendprofileedittor.navigation.FriendType
import com.alarmy.near.presentation.feature.friendprofileedittor.navigation.RouteFriendProfileEditor
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.reflect.KType
import kotlin.reflect.typeOf

@Parcelize
@Serializable
data class RouteFriendProfile(
    val friendId: String,
) : Parcelable

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
        val viewModel: FriendProfileViewModel = hiltViewModel()
        val friend = backStackEntry.savedStateHandle.get<Friend>(FRIEND_PROFILE_EDIT_COMPLETE_KEY)
        friend?.let {
            viewModel.updateFriend(it)
        }
        FriendProfileRoute(
            viewModel = viewModel,
            onShowErrorSnackBar = onShowErrorSnackBar,
            onClickBackButton = onClickBackButton,
            onEditFriendInfo = onEditFriendInfo,
            onClickCallButton = onClickCallButton,
            onClickMessageButton = onClickMessageButton,
        )
    }
}
