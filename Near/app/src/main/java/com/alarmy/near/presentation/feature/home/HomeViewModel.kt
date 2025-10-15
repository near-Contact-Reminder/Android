package com.alarmy.near.presentation.feature.home

import androidx.lifecycle.viewModelScope
import com.alarmy.near.core.viewmodel.BaseViewModel
import com.alarmy.near.data.repository.FriendRepository
import com.alarmy.near.data.repository.MemberRepository
import com.alarmy.near.model.friendsummary.FriendSummary
import com.alarmy.near.model.member.MemberInfo
import com.alarmy.near.model.monthly.MonthlyFriend
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        friendRepository: FriendRepository,
        memberRepository: MemberRepository,
    ) : BaseViewModel() {
        private val deletedFriendIdsFlow = MutableStateFlow<Set<String>>(setOf())
        val memberInfoFlow: StateFlow<MemberInfo?> =
            memberRepository
                .getMyInfo()
                .handleError()
                .stateIn(
                    viewModelScope,
                    SharingStarted.WhileSubscribed(5_000),
                    null,
                )

        val friendsFlow: StateFlow<List<FriendSummary>> =
            combine(
                friendRepository
                    .fetchFriends(),
                deletedFriendIdsFlow,
            ) { friends, deletedIds ->
                friends.filter { it.id !in deletedIds }
            }.handleError()
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = emptyList(),
                )

        val monthlyFriendFlow: StateFlow<List<MonthlyFriend>> =
            friendRepository
                .fetchMonthlyFriends()
                .handleError()
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = emptyList(),
                )

        fun deleteFriend(friendId: String) {
            viewModelScope.launch {
                deletedFriendIdsFlow.emit(deletedFriendIdsFlow.value + friendId)
            }
        }
    }
