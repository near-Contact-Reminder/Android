package com.alarmy.near.presentation.feature.friendcontactcycle.state

/**
 * FriendContactCycle 화면의 UI 이벤트
 */
sealed class FriendContactUIEvent {
    // 홈 화면으로 이동
    object NavigateToHome : FriendContactUIEvent()

    // 에러 표시
    data class ShowError(
        val throwable: Throwable?,
    ) : FriendContactUIEvent()
}
