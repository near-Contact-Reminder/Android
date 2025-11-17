package com.alarmy.near.presentation.feature.friendprofile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.alarmy.near.data.repository.FriendRepository
import com.alarmy.near.model.Friend
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
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class FriendProfileViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        private val friendRepository: FriendRepository,
    ) : ViewModel() {
        private val friendId: String =
            savedStateHandle.toRoute<RouteFriendProfile>().friendId
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
                            isEmpty = records.isEmpty(),
                            isLoading = false,
                        )
                    }
                }.catch { error ->
                    _friendShipRecordStateFlow.update {
                        it.copy(
                            isEmpty = true,
                            isLoading = false,
                        )
                    }
                    _uiEvent.send(FriendProfileUIEvent.NetworkError) // UI에서 단발성 이벤트로도 쓸 수 있음
                }.launchIn(viewModelScope)
        }

        fun onDeleteFriend(friendId: String) {
            friendRepository
                .deleteFriend(friendId)
                .onEach {
                    _uiEvent.send(FriendProfileUIEvent.DeleteFriendSuccess(friendId))
                    // event
                }.catch { error ->
                    _uiEvent.send(FriendProfileUIEvent.NetworkError) // UI에서 단발성 이벤트로도 쓸 수 있음
                }.launchIn(viewModelScope)
        }

        fun onRecordFriendShip(friendId: String) {
            friendRepository
                .recordContact(friendId) // 내 현재 시간 가져와서
                .onEach { _ ->
                    _uiEvent.send(FriendProfileUIEvent.RecordFriendShipSuccess)
                    _friendShipRecordStateFlow.update { recordState ->
                        recordState.copy(
                            isEmpty = false,
                            records =
                                listOf(
                                    FriendRecord(
                                        isChecked = true,
                                        createdAt = getTodayShortFormat(),
                                    ),
                                ) + (recordState.records),
                        )
                    }
                    if (friendFlow.value is FriendState.Success) {
                        _friendFlow.update {
                            (it as FriendState.Success).copy(
                                friend =
                                    it.friend.copy(
                                        lastContactAt = getTodayDashFormat(),
                                        lastContactFormat = it.friend.lastContactAt?.contactFormat(),
                                        isContactToday =
                                            it.friend.lastContactAt?.isToday()
                                                ?: false,
                                    ),
                            )
                        }
                    }

                    // event
                }.catch { error ->
                    _uiEvent.send(FriendProfileUIEvent.NetworkError) // UI에서 단발성 이벤트로도 쓸 수 있음
                }.launchIn(viewModelScope)
        }

        private fun getTodayShortFormat(): String {
            val today = LocalDate.now()
            val formatter = DateTimeFormatter.ofPattern("yy.MM.dd", Locale.KOREA)
            return today.format(formatter)
        }

        private fun getTodayDashFormat(): String {
            val today = LocalDate.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.KOREA)
            return today.format(formatter)
        }

        fun updateFriend(friend: Friend) {
            if (_friendFlow.value is FriendState.Success) {
                _friendFlow.update { (it as FriendState.Success).copy(friend = friend) }
            }
        }

        fun String.contactFormat(): String {
            val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val outputFormatter = DateTimeFormatter.ofPattern("M월 d일")

            val date = LocalDate.parse(this, inputFormatter)
            return date.format(outputFormatter)
        }

        private fun String.isToday(): Boolean {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.KOREA)
            val targetDate = LocalDate.parse(this, formatter)
            val today = LocalDate.now()
            return targetDate == today
    }
}
