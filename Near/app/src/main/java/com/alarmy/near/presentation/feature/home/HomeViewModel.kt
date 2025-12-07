package com.alarmy.near.presentation.feature.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alarmy.near.data.repository.FriendRepository
import com.alarmy.near.data.repository.MemberRepository
import com.alarmy.near.presentation.feature.home.model.HomeUiState
import com.alarmy.near.presentation.feature.home.model.MonthlyFriendUIState
import com.alarmy.near.presentation.feature.home.model.MyFriendUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        friendRepository: FriendRepository,
        memberRepository: MemberRepository,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(HomeUiState())
        val uiState = _uiState.asStateFlow()

        private val _errorEvent = Channel<Throwable?>()

        private val deletedFriendIdsFlow = MutableStateFlow<Set<String>>(setOf())

        val errorEvent = _errorEvent.receiveAsFlow()

        init {
            viewModelScope.launch {
                launch {
                    memberRepository
                        .getMyInfo()
                        .catch {
                            _errorEvent.send(it)
                        }.collect {
                            _uiState.update { state ->
                                state.copy(
                                    memberInfo = it,
                                )
                            }
                        }
                }
                launch {
                    combine(
                        friendRepository
                            .fetchFriends(),
                        deletedFriendIdsFlow,
                    ) { friends, deletedIds ->
                        friends.filter { it.id !in deletedIds }
                    }.catch {
                        _errorEvent.send(it)
                    }.collect {
                        _uiState.update { state ->
                            state.copy(
                                myFriendUIState = MyFriendUIState.Success(it),
                            )
                        }
                    }
                }
                launch {
                    combine(
                        friendRepository
                            .fetchMonthlyFriends(),
                        deletedFriendIdsFlow,
                    ) { monthlyFriends, deletedIds ->
                        monthlyFriends.filter { it.friendId !in deletedIds }
                    }.catch {
                        _errorEvent.send(it)
                    }.collect {
                        _uiState.update { state ->
                            state.copy(
                                monthlyFriendUIState = MonthlyFriendUIState.Success(it),
                            )
                        }
                    }
                }
            }
        }

        fun deleteFriend(friendId: String) {
            viewModelScope.launch {
                deletedFriendIdsFlow.emit(deletedFriendIdsFlow.value + friendId)
            }
        }

        fun updateFriendReminder(
            friendId: String,
            friendReminderUpdatedAt: String?,
        ) {
            _uiState.update { state ->
                val current = state.myFriendUIState

                if (current is MyFriendUIState.Success) {
                    val updatedList =
                        current.myFriends.map { friend ->
                            if (friend.id == friendId) {
                                friend.copy(lastContactedAt = friendReminderUpdatedAt)
                            } else {
                                friend
                            }
                        }

                    return@update state.copy(
                        myFriendUIState = MyFriendUIState.Success(updatedList),
                    )
                }

                state
            }
        }
    }
