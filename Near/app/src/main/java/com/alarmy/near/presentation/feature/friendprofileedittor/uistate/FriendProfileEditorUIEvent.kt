package com.alarmy.near.presentation.feature.friendprofileedittor.uistate

import com.alarmy.near.model.Friend

sealed interface FriendProfileEditorUIEvent {
    data class FriendProfileEditSuccess(
        val friend: Friend,
    ) : FriendProfileEditorUIEvent

    data class FriendProfileEditFailure(
        val throwable: Throwable,
    ) : FriendProfileEditorUIEvent

    data object FriendProfileEditNetworkError : FriendProfileEditorUIEvent

    data object WarningExit : FriendProfileEditorUIEvent
}
