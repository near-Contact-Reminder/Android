package com.alarmy.near.presentation.feature.mothlyreminderall

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alarmy.near.data.repository.FriendRepository
import com.alarmy.near.model.monthly.MonthlyFriend
import com.alarmy.near.presentation.feature.mothlyreminderall.model.MonthlyReminderCombinedData
import com.alarmy.near.presentation.feature.mothlyreminderall.model.MonthlyReminderTypeInfo
import com.alarmy.near.presentation.feature.mothlyreminderall.model.MonthlyReminderUIModel
import com.alarmy.near.presentation.feature.mothlyreminderall.uistate.MonthlyReminderAllUIEvent
import com.alarmy.near.presentation.feature.mothlyreminderall.uistate.MonthlyReminderAllUIState
import com.alarmy.near.utils.extensions.handleError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class MonthlyReminderAllViewModel
    @Inject
    constructor(
        private val friendRepository: FriendRepository,
    ) : ViewModel() {
        private val _uiEvent = Channel<MonthlyReminderAllUIEvent>()
        val uiEvent = _uiEvent.receiveAsFlow()

        private val _uiState: MutableStateFlow<MonthlyReminderAllUIState> =
            MutableStateFlow(MonthlyReminderAllUIState.Loading)
        val uiState: StateFlow<MonthlyReminderAllUIState> = _uiState.asStateFlow()

        private val monthlyReminders = MutableStateFlow<List<MonthlyReminderUIModel>>(emptyList())
        private val completedReminders = MutableStateFlow<List<MonthlyReminderUIModel>>(emptyList())

        init {
            fetchMonthlyReminders()
        }

        private fun fetchMonthlyReminders() {
            combine(
                friendRepository.fetchMonthlyFriends(),
                friendRepository.fetchMonthlyCompleteFriends(),
            ) { monthlyFriends, completeFriends ->
                MonthlyReminderCombinedData(
                    monthlyFriends = monthlyFriends,
                    completeFriends = completeFriends,
                )
            }.onEach { data ->
                val monthlyUIModels =
                    convertToUIModels(data.monthlyFriends)
                        .distinctBy { it.friendId }
                        .sortedBy { it.nextContactAt }
                monthlyReminders.value = monthlyUIModels
                val completedUIModels =
                    convertToUIModels(data.completeFriends)
                        .distinctBy { it.friendId }
                completedReminders.value = completedUIModels
                // 두 데이터가 모두 준비된 후 UI 상태 업데이트
                combineRemindersToUIState()
            }.handleError(viewModelScope, _uiEvent) { exception ->
                MonthlyReminderAllUIEvent.ShowError(exception)
            }.launchIn(viewModelScope)
        }

        private fun combineRemindersToUIState() {
            val monthlyList = monthlyReminders.value
            val completedList = completedReminders.value
            val completedFriendIds = completedList.map { it.friendId }.toSet()
            val filteredMonthlyList =
                monthlyList
                    .filter { it.friendId !in completedFriendIds }
                    .distinctBy { it.friendId }

            _uiState.update {
                if (filteredMonthlyList.isEmpty() && completedList.isEmpty()) {
                    MonthlyReminderAllUIState.Empty
                } else {
                    MonthlyReminderAllUIState.Success(
                        monthlyReminders = filteredMonthlyList,
                        completedReminders = completedList.distinctBy { it.friendId },
                        hasCompletedReminders = completedList.isNotEmpty(),
                    )
                }
            }
        }

        // 챙김시에 기념일이 나와야 함
        fun onRecordFriendShip(friendId: String) {
            friendRepository
                .recordContact(friendId)
                .onEach { _ ->
                    viewModelScope.launch {
                        _uiEvent.send(MonthlyReminderAllUIEvent.RecordFriendShipSuccess)
                    }
                    val recordedFriend = monthlyReminders.value.find { it.friendId == friendId }
                    if (recordedFriend != null) {
                        monthlyReminders.value =
                            monthlyReminders.value.filter { it.friendId != friendId }
                        completedReminders.value = listOf(
                            recordedFriend.copy(
                                nextContactAt =
                                    LocalDate
                                        .now()
                                        .format(DateTimeFormatter.ofPattern("yy.MM.dd")),
                            ),
                        ) + completedReminders.value
                        combineRemindersToUIState()
                    }
                }.handleError(viewModelScope, _uiEvent) { exception ->
                    MonthlyReminderAllUIEvent.ShowError(exception)
                }.launchIn(viewModelScope)
        }

        private fun convertToUIModels(monthlyFriends: List<MonthlyFriend>): List<MonthlyReminderUIModel> {
            val today = LocalDate.now()
            return monthlyFriends.mapNotNull { friend ->
                try {
                    val typeInfo = MonthlyReminderTypeInfo.from(friend.type)
                    MonthlyReminderUIModel(
                        friendId = friend.friendId,
                        name = friend.name,
                        imageRes = typeInfo.imageRes,
                        descriptionRes = typeInfo.descriptionRes,
                        nextContactAt = friend.nextContactAt,
                        daysUntilNextContact = friend.daysUntilNextContact(today),
                        isToday = friend.isNextContactDay(today),
                    )
                } catch (e: Exception) {
                    null
                }
            }
        }
    }
