package com.alarmy.near.presentation.feature.friendprofile.uistate

sealed interface FriendProfileUIEvent {
    data object NetworkError : FriendProfileUIEvent

    data class DeleteFriendSuccess(
        val friendId: String,
    ) : FriendProfileUIEvent

    data object RecordFriendShipSuccess : FriendProfileUIEvent
}
