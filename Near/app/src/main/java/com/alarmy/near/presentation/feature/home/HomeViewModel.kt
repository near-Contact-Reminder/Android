package com.alarmy.near.presentation.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alarmy.near.data.repository.FriendRepository
import com.alarmy.near.model.Friend
import com.alarmy.near.model.MonthlyFriend
import com.alarmy.near.presentation.feature.home.model.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        friendRepository: FriendRepository,
    ) : ViewModel() {
        // Example: 여러번 초기화되는 StateFlow
        private val _uiStateFlow = MutableStateFlow(HomeUiState.Loading)
        val uiStateFlow = _uiStateFlow.asStateFlow()

        val friendsFlow: StateFlow<List<Friend>> =
            friendRepository
                .fetchFriends()
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = emptyList(),
                )

        val monthlyFriendFlow:
            StateFlow<List<MonthlyFriend>> =
            friendRepository
                .fetchMonthlyFriends()
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = emptyList(),
            )

        fun removeContact(id: Long) {
            // contactRepository.removeContact(id)
        }
    }
