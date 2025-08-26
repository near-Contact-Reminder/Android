package com.alarmy.near.presentation.feature.friendprofile

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.alarmy.near.data.repository.FriendRepository
import com.alarmy.near.model.FriendRecord
import com.alarmy.near.presentation.feature.friendprofile.navigation.RouteFriendProfile
import com.alarmy.near.presentation.feature.friendprofile.uistate.FriendProfileUIEvent
import com.alarmy.near.presentation.feature.friendprofile.uistate.FriendShipRecordState
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
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class FriendProfileViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        private val friendRepository: FriendRepository,
    ) : ViewModel() {
        private val friendId: String = savedStateHandle.toRoute<RouteFriendProfile>().friendId
        private val _uiEvent = Channel<FriendProfileUIEvent>()
        val uiEvent = _uiEvent.receiveAsFlow()
        private val _friendFlow: MutableStateFlow<FriendState> = MutableStateFlow(FriendState.Loading)
        val friendFlow: StateFlow<FriendState> = _friendFlow.asStateFlow()

        private val _friendShipRecordStateFlow: MutableStateFlow<FriendShipRecordState> =
            MutableStateFlow(FriendShipRecordState(isLoading = true))
        val friendShipRecordStateFlow: StateFlow<FriendShipRecordState> =
            _friendShipRecordStateFlow.asStateFlow()

        init {
            fetchFriend()
            fetchFriendShipRecord()
        }

        fun fetchFriend() {
            friendRepository
                .fetchFriendById(friendId)
                .onEach { friend ->
                    _friendFlow.value = FriendState.Success(friend)
                }.catch { error ->
                    _friendFlow.value = FriendState.Error("데이터를 가져오는데 실패했습니다.")
                    _uiEvent.send(FriendProfileUIEvent.NetworkError) // UI에서 단발성 이벤트로도 쓸 수 있음
                }.launchIn(viewModelScope)
        }

        fun fetchFriendShipRecord() {
            friendRepository
                .fetchFriendRecord(friendId)
                .onEach { records ->
                    _friendShipRecordStateFlow.update {
                        it.copy(
                            records = records.filter { record -> record.isChecked },
                            isLoading = false,
                        )
                    }
                }.catch { error ->
                    _friendShipRecordStateFlow.update {
                        it.copy(
                            isLoading = false,
                        )
                    }
                    _uiEvent.send(FriendProfileUIEvent.NetworkError) // UI에서 단발성 이벤트로도 쓸 수 있음
                }.launchIn(viewModelScope)
        }

        fun deleteFriend() {
            friendRepository
                .deleteFriend(friendId)
                .onEach {
                    _uiEvent.send(FriendProfileUIEvent.DeleteFriendSuccess)
                    // event
                }.catch { error ->
                    _uiEvent.send(FriendProfileUIEvent.NetworkError) // UI에서 단발성 이벤트로도 쓸 수 있음
                }.launchIn(viewModelScope)
        }

        fun recordFriendShip() {
            friendRepository
                .recordContact(friendId)
                .onEach { result ->
                    _uiEvent.send(FriendProfileUIEvent.RecordFriendShipSuccess)
                    _friendShipRecordStateFlow.update { recordState ->
                        recordState.copy(
                            records =
                                listOf(
                                    FriendRecord(isChecked = true, createdAt = result),
                                ) + (recordState.records),
                        )
                    }
                    // event
                }.catch { error ->
                    _uiEvent.send(FriendProfileUIEvent.NetworkError) // UI에서 단발성 이벤트로도 쓸 수 있음
                }.launchIn(viewModelScope)
        }
    }
