package com.alarmy.near.presentation.feature.friendprofile

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.alarmy.near.data.repository.FriendRepository
import com.alarmy.near.presentation.feature.friendprofile.navigation.RouteFriendProfile
import com.alarmy.near.presentation.feature.friendprofile.uistate.FriendState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class FriendProfileViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        private val friendRepository: FriendRepository,
    ) : ViewModel() {
        private val friendId: String = savedStateHandle.toRoute<RouteFriendProfile>().friendId
        private val _errorEvent = Channel<Throwable>()
        val errorEvent = _errorEvent.receiveAsFlow()
        private val _friendFlow: MutableStateFlow<FriendState> = MutableStateFlow(FriendState.Loading)
        val friendFlow: StateFlow<FriendState> = _friendFlow.asStateFlow()

        init {
            fetchFriend()
        }

        fun fetchFriend() {
            friendRepository
                .fetchFriendById(friendId)
                .onEach { friend ->
                    _friendFlow.value = FriendState.Success(friend)
                }.catch { error ->
                    Log.d("test1", error.message.toString())
                    _friendFlow.value = FriendState.Error("데이터를 가져오는데 실패했습니다.")
                    _errorEvent.send(error) // UI에서 단발성 이벤트로도 쓸 수 있음
                }.launchIn(viewModelScope)
        }

        fun deleteFriend() {
        }
    }
