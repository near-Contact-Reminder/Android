package com.alarmy.near.presentation.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alarmy.near.data.repository.FriendRepository
import com.alarmy.near.model.FriendSummary
import com.alarmy.near.model.monthly.MonthlyFriend
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        friendRepository: FriendRepository,
    ) : ViewModel() {
        private val _errorEvent = Channel<Throwable?>()
        val errorEvent = _errorEvent.receiveAsFlow()

        val friendsFlow: StateFlow<List<FriendSummary>> =
            friendRepository
                .fetchFriends()
                .catch {
                    _errorEvent.send(it)
                }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = emptyList(),
                )

        val monthlyFriendFlow:
            StateFlow<List<MonthlyFriend>> =
            friendRepository
                .fetchMonthlyFriends()
                .catch {
                    _errorEvent.send(it)
                }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = emptyList(),
                )
    }
