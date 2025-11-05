package com.alarmy.near.presentation.feature.home.model

import com.alarmy.near.model.friendsummary.FriendSummary
import com.alarmy.near.model.member.MemberInfo
import com.alarmy.near.model.monthly.MonthlyFriend

data class HomeUiState(
    val memberInfo: MemberInfo? = null,
    val monthlyFriendUIState: MonthlyFriendUIState = MonthlyFriendUIState.Loading,
    val myFriendUIState: MyFriendUIState = MyFriendUIState.Loading,
)

sealed interface MonthlyFriendUIState {
    data class Success(
        val monthlyFriends: List<MonthlyFriend>,
    ) : MonthlyFriendUIState

    data object Loading : MonthlyFriendUIState
}

sealed interface MyFriendUIState {
    data class Success(
        val myFriends: List<FriendSummary>,
    ) : MyFriendUIState

    data object Loading : MyFriendUIState
}
