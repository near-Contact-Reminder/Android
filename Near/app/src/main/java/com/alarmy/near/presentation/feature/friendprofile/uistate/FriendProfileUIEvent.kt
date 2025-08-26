package com.alarmy.near.presentation.feature.friendprofile.uistate

sealed interface FriendProfileUIEvent {
    data object NetworkError : FriendProfileUIEvent

    data object DeleteFriendSuccess : FriendProfileUIEvent

    data object RecordFriendShipSuccess : FriendProfileUIEvent
}
