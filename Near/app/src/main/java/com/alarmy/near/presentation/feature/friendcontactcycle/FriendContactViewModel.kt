package com.alarmy.near.presentation.feature.friendcontactcycle

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alarmy.near.data.repository.FriendRepository
import com.alarmy.near.data.repository.MemberRepository
import com.alarmy.near.model.ProviderType
import com.alarmy.near.model.ReminderInterval
import com.alarmy.near.model.contact.Contact
import com.alarmy.near.presentation.feature.contact.navigation.CONTACT_SELECTION_COMPLETE_KEY
import com.alarmy.near.presentation.feature.friendcontactcycle.model.ContactCycleStep
import com.alarmy.near.presentation.feature.friendcontactcycle.model.FriendContactUIModel
import com.alarmy.near.presentation.feature.friendcontactcycle.model.toFriendContactUIModel
import com.alarmy.near.presentation.feature.friendcontactcycle.state.FriendContactUIEvent
import com.alarmy.near.presentation.feature.friendcontactcycle.state.FriendContactUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendContactViewModel
    @Inject
    constructor(
        private val savedStateHandle: SavedStateHandle,
        private val friendRepository: FriendRepository,
        private val memberRepository: MemberRepository,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(FriendContactUIState())
        val uiState: StateFlow<FriendContactUIState> = _uiState.asStateFlow()

        private val _uiEvent = Channel<FriendContactUIEvent>()
        val uiEvent = _uiEvent.receiveAsFlow()

        init {
            observeContactSelection()
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
        private fun observeContactSelection() {
            viewModelScope.launch {
                savedStateHandle
                    .getStateFlow<List<Contact>?>(
                        CONTACT_SELECTION_COMPLETE_KEY,
                        null,
                    ).collect { selectedContacts ->
                        selectedContacts?.let { contacts ->
                            addSelectedContacts(contacts)
                            savedStateHandle.remove<List<Contact>>(
                                CONTACT_SELECTION_COMPLETE_KEY,
                            )
                        }
                    }
            }
        }

        fun addSelectedContacts(contacts: List<Contact>) {
            val friendContacts = contacts.map { it.toFriendContactUIModel() }
            _uiState.value =
                _uiState.value.copy(
                    contacts = friendContacts,
                )
        }

        fun deselectContact(contactId: String) {
            val currentState = _uiState.value
            val updatedContacts =
                currentState.contacts.filter { contact ->
                    contact.id.toString() != contactId
                }

            _uiState.value = currentState.copy(contacts = updatedContacts)
        }

        // 한번에 설정 관련 함수
        fun toggleBulkSetting() {
            val currentState = _uiState.value

            if (currentState.isBulkSettingEnabled) {
                // 체크 해제 시: 한번에 설정 모드만 해제, 각 연락처의 주기는 유지
                _uiState.value =
                    currentState.copy(
                        isBulkSettingEnabled = false,
                        selectedCycle = null,
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

        // 한번에 설정 바텀시트 열기
        fun openBottomSheet() {
            _uiState.value =
                _uiState.value.copy(
                    isBottomSheetVisible = true,
                )
        }

        // 개별 설정 바텀시트 열기 (특정 연락처 선택)
        fun openIndividualBottomSheet(contactId: String) {
            _uiState.value =
                _uiState.value.copy(
                    isBottomSheetVisible = true,
                    selectedContactId = contactId,
                )
        }

        // 바텀시트 닫기 (취소 시 한번에 설정 모드도 해제)
        fun closeBottomSheet() {
            val currentState = _uiState.value
            _uiState.value =
                currentState.copy(
                    isBottomSheetVisible = false,
                    selectedContactId = null,
                )

            // 바텀시트를 취소로 닫으면 체크박스도 해제 (한번에 설정일 때만)
            if (currentState.selectedCycle == null && currentState.isBulkSettingEnabled) {
                _uiState.value =
                    _uiState.value.copy(
                        isBulkSettingEnabled = false,
                    )
            }
        }

        // 주기 설정 완료 (개별/한번에 설정 분기 처리)
        fun completeCycleSetting(reminderInterval: ReminderInterval) {
            _uiState.value.run {
                selectedContactId?.let {
                    applyIndividualCycleSetting(this, reminderInterval)
                } ?: applyBulkCycleSetting(this, reminderInterval)
            }
        }

        // 개별 연락처 주기 설정 적용 (한번에 설정 모드가 true라면 해제합니다)
        private fun applyIndividualCycleSetting(
            currentState: FriendContactUIState,
            reminderInterval: ReminderInterval,
        ) {
            val updatedContacts = updateContactCycle(currentState.selectedContactId!!, reminderInterval)
            _uiState.value =
                currentState.copy(
                    isBottomSheetVisible = false,
                    contacts = updatedContacts,
                    selectedContactId = null,
                    isBulkSettingEnabled = false,
                    selectedCycle = null,
                )
        }

        // 한번에 설정 주기 적용 (모든 연락처에 동일 주기 설정)
        private fun applyBulkCycleSetting(
            currentState: FriendContactUIState,
            reminderInterval: ReminderInterval,
        ) {
            val updatedContacts = updateAllContactsCycle(reminderInterval)
            _uiState.value =
                currentState.copy(
                    selectedCycle = reminderInterval,
                    isBottomSheetVisible = false,
                    contacts = updatedContacts,
                )
        }

        // 개별 연락처의 주기만 업데이트
        private fun updateContactCycle(
            contactId: String,
            reminderInterval: ReminderInterval,
        ): List<FriendContactUIModel> =
            _uiState.value.contacts.map { contact ->
                if (contact.id.toString() == contactId) {
                    contact.copy(reminderInterval = reminderInterval)
                } else {
                    contact
                }
            }

        // 모든 연락처의 주기를 동일하게 업데이트
        private fun updateAllContactsCycle(reminderInterval: ReminderInterval): List<FriendContactUIModel> =
            _uiState.value.contacts.map { contact ->
                contact.copy(reminderInterval = reminderInterval)
            }

        // 서버에 친구 목록 전송
        fun completeFriendInit() {
            viewModelScope.launch {
                try {
                    _uiState.value = _uiState.value.copy(isLoading = true)

                    friendRepository
                        .initFriends(
                            contacts = _uiState.value.contacts,
                            providerType = getCurrentUserProviderType(),
                        ).collect { friendInitResponse ->
                            _uiState.value = _uiState.value.copy(isLoading = false)
                            _uiEvent.send(FriendContactUIEvent.NavigateToHome)
                        }
                } catch (e: Exception) {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                    _uiEvent.send(FriendContactUIEvent.ShowError(e))
                }
            }
        }

        // 현재 사용자의 로그인 타입 가져오기
        private suspend fun getCurrentUserProviderType(): String =
            runCatching {
                memberRepository
                    .getMyInfo()
                    .first()
                    .providerType
            }.getOrElse { exception ->
                ProviderType.KAKAO.name
            }

        // 권한 관련 함수들
        fun setPermissionRequestFunction(requestPermission: () -> Unit) {
            _uiState.value = _uiState.value.copy(onRequestPermission = requestPermission)
        }

        fun updatePermissionDeniedDialog(show: Boolean) {
            _uiState.value = _uiState.value.copy(showPermissionDeniedDialog = show)
        }
    }
