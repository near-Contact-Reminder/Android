package com.alarmy.near.presentation.feature.mothlyreminderall

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alarmy.near.data.repository.FriendRepository
import com.alarmy.near.model.monthly.MonthlyFriend
import com.alarmy.near.presentation.feature.mothlyreminderall.model.MonthlyReminderTypeInfo
import com.alarmy.near.presentation.feature.mothlyreminderall.model.MonthlyReminderUIModel
import com.alarmy.near.presentation.feature.mothlyreminderall.uistate.MonthlyReminderAllUIEvent
import com.alarmy.near.presentation.feature.mothlyreminderall.uistate.MonthlyReminderAllUIState
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
import kotlinx.coroutines.launch
import java.time.LocalDate
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

        init {
            fetchMonthlyFriends()
        }

        private fun fetchMonthlyFriends() {
            friendRepository
                .fetchMonthlyFriends()
                .onEach { monthlyFriends ->
                    val uiModels = convertToUIModels(monthlyFriends)
                    _uiState.update {
                        if (uiModels.isEmpty()) {
                            MonthlyReminderAllUIState.Empty
                        } else {
                            MonthlyReminderAllUIState.Success(uiModels)
                        }
                    }
                }.catch { exception ->
                    viewModelScope.launch {
                        _uiEvent.send(MonthlyReminderAllUIEvent.NetworkError)
                    }
                }.launchIn(viewModelScope)
        }

        private fun convertToUIModels(monthlyFriends: List<MonthlyFriend>): List<MonthlyReminderUIModel> {
            val today = LocalDate.now()
            return monthlyFriends.map { friend ->
                val typeInfo = MonthlyReminderTypeInfo.from(friend.type)
                MonthlyReminderUIModel(
                    friendId = friend.friendId,
                    name = friend.name,
                    imageRes = typeInfo.imageRes,
                    description = typeInfo.description,
                    nextContactAt = friend.nextContactAt,
                    daysUntilNextContact = friend.daysUntilNextContact(today),
                )
            }
        }
    }
