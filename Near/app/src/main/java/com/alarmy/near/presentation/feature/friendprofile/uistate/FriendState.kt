package com.alarmy.near.presentation.feature.friendprofile.uistate

import com.alarmy.near.model.Friend

sealed interface FriendState {
    object Loading : FriendState

    data class Success(
        val friend: Friend,
    ) : FriendState

    data class Error(
        val errorMessage: String,
    ) : FriendState
}
