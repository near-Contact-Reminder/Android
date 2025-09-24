package com.alarmy.near.presentation.feature.friendcontactcycle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alarmy.near.model.ReminderInterval
import com.alarmy.near.presentation.feature.friendcontactcycle.model.ContactCycleStep
import com.alarmy.near.presentation.feature.friendcontactcycle.model.FriendContactUIModel
import com.alarmy.near.presentation.feature.friendcontactcycle.state.FriendContactUIEvent
import com.alarmy.near.presentation.feature.friendcontactcycle.state.FriendContactUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendContactViewModel
    @Inject
    constructor() : ViewModel() {
        // 임시 데이터
        private val dummyContacts =
            listOf(
                FriendContactUIModel(
                    id = 1,
                    name = "신짱구",
                    photoUri = null,
                ),
                FriendContactUIModel(
                    id = 2,
                    name = "철수",
                    photoUri = null,
                ),
                FriendContactUIModel(
                    id = 3,
                    name = "유리",
                    photoUri = null,
                ),
                FriendContactUIModel(
                    id = 4,
                    name = "맹구",
                    photoUri = null,
                ),
                FriendContactUIModel(
                    id = 5,
                    name = "액션가면",
                    photoUri = null,
                ),
                FriendContactUIModel(
                    id = 6,
                    name = "흰둥이",
                    photoUri = null,
                ),
                FriendContactUIModel(
                    id = 7,
                    name = "수지",
                    photoUri = null,
                ),
            )

        private val _uiState = MutableStateFlow(FriendContactUIState())
        val uiState: StateFlow<FriendContactUIState> = _uiState.asStateFlow()

        private val _uiEvent = Channel<FriendContactUIEvent>()
        val uiEvent = _uiEvent.receiveAsFlow()

        init {
            // 초기 연락처 로드
            fetchContacts()
        }

        // 이베트 처리 함수: 이번트가 많아 파라미터로 이벤트를 받아 send
        fun onEvent(event: FriendContactUIEvent) {
            viewModelScope.launch {
                _uiEvent.send(event)
            }
        }

        // 화면 분기 관련 함수
        fun moveToNextStep() {
            viewModelScope.launch {
                _uiState.value =
                    _uiState.value.copy(
                        currentStep = ContactCycleStep.SET_CYCLE,
                    )
            }
        }

        fun moveToPreviousStep() {
            viewModelScope.launch {
                _uiState.value =
                    _uiState.value.copy(
                        currentStep = ContactCycleStep.LOAD_CONTACTS,
                    )
            }
        }

        // 연락처 관련 함수
        fun fetchContacts() {
            viewModelScope.launch {
                _uiState.value =
                    _uiState.value.copy(
                        isLoading = true,
                        error = null,
                    )

                try {
                    // 실제로는 Repository에서
                    _uiState.value =
                        _uiState.value.copy(
                            contacts = dummyContacts,
                            isLoading = false,
                        )
                } catch (e: Exception) {
                    _uiState.value =
                        _uiState.value.copy(
                            isLoading = false,
                            error = e.message ?: "연락처를 불러오는데 실패했습니다.",
                        )
                }
            }
        }

        fun deselectContact(contactId: String) {
            val currentState = _uiState.value
            val updatedContacts = currentState.contacts.filter { contact ->
                contact.id.toString() != contactId
            }
            
            _uiState.value = currentState.copy(contacts = updatedContacts)
        }

        // 한번에 설정 관련 함수
        fun toggleBulkSetting() {
            val currentState = _uiState.value

            if (currentState.isBulkSettingEnabled) {
                // 체크 해제 시: 미선택으로 돌아가고 설정한 값 리셋
                val resetContacts =
                    currentState.contacts.map { contact ->
                        contact.copy(reminderInterval = null)
                    }

                _uiState.value =
                    currentState.copy(
                        isBulkSettingEnabled = false,
                        selectedCycle = null,
                        contacts = resetContacts,
                    )
            } else {
                // 체크 시: 바텀시트 표시
                _uiState.value =
                    currentState.copy(
                        isBulkSettingEnabled = true,
                        isBottomSheetVisible = true,
                    )
            }
        }

        fun openBottomSheet() {
            _uiState.value =
                _uiState.value.copy(
                    isBottomSheetVisible = true,
                )
        }

        fun closeBottomSheet() {
            val currentState = _uiState.value
            _uiState.value =
                currentState.copy(
                    isBottomSheetVisible = false,
                )

            // 바텀시트를 취소로 닫으면 체크박스도 해제
            if (currentState.selectedCycle == null) {
                _uiState.value =
                    _uiState.value.copy(
                        isBulkSettingEnabled = false,
                    )
            }
        }

        fun completeCycleSetting(reminderInterval: ReminderInterval) {
            val currentState = _uiState.value

            // 한번에 설정으로 선택된 주기를 모든 연락처에 적용
            val updatedContacts =
                currentState.contacts.map { contact ->
                    contact.copy(reminderInterval = reminderInterval)
                }

            _uiState.value =
                currentState.copy(
                    selectedCycle = reminderInterval,
                    isBottomSheetVisible = false,
                    contacts = updatedContacts,
                )
        }

        fun setContactCycle(
            contactId: String,
            reminderInterval: ReminderInterval,
        ) {
            val currentState = _uiState.value
            val updatedContacts =
                currentState.contacts.map { contact ->
                    if (contact.id.toString() == contactId) {
                        contact.copy(reminderInterval = reminderInterval)
                    } else {
                        contact
                    }
                }

            _uiState.value = currentState.copy(contacts = updatedContacts)
        }
    }
