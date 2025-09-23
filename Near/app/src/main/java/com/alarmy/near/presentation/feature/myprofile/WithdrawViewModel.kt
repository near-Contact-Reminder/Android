package com.alarmy.near.presentation.feature.myprofile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.alarmy.near.data.repository.AuthRepository
import com.alarmy.near.data.repository.MemberRepository
import com.alarmy.near.presentation.feature.myprofile.model.WithdrawReason
import com.alarmy.near.presentation.feature.myprofile.navigation.RouteWithdraw
import com.alarmy.near.utils.logger.NearLog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WithdrawViewModel
    @Inject
    constructor(
        private val memberRepository: MemberRepository,
        private val authRepository: AuthRepository,
        savedStateHandle: SavedStateHandle,
    ) : ViewModel() {
        private val nickname: String = savedStateHandle.toRoute<RouteWithdraw>().nickname

        // UI 상태 관리
        private val _uiState = MutableStateFlow(WithdrawUiState(nickname = nickname))
        val uiState: StateFlow<WithdrawUiState> = _uiState.asStateFlow()

        // UI 이벤트 관리
        private val _uiEvent = Channel<WithdrawUiEvent>()
        val uiEvent = _uiEvent.receiveAsFlow()

        /**
         * 탈퇴 사유를 선택하는 함수
         */
        fun selectReason(reason: WithdrawReason) {
            val currentState = _uiState.value
            _uiState.value =
                currentState.copy(
                    selectedReason = reason,
                )
        }

        /**
         * 기타 사유 텍스트를 업데이트하는 함수
         */
        fun updateOtherReasonText(text: String) {
            val currentState = _uiState.value
            _uiState.value =
                currentState.copy(
                    otherReasonText = text,
                )
        }

        /**
         * 탈퇴 요청을 처리하는 함수
         */
        fun submitWithdrawRequest() {
            val currentState = _uiState.value
            val reason = currentState.selectedReason

            if (reason == null) {
                _uiEvent.trySend(WithdrawUiEvent.ShowError(Exception("탈퇴 사유를 선택해주세요.")))
                return
            }

            // 로딩 상태 시작
            _uiState.value =
                currentState.copy(
                    isLoading = true,
                )

            viewModelScope.launch {
                val customReason = if (currentState.isOtherReasonSelected) {
                    currentState.otherReasonText.takeIf { it.isNotEmpty() }
                } else {
                    null
                }

                memberRepository
                    .withdraw(reason, customReason)
                    .catch { error ->
                        // 실패 시 에러 처리
                        NearLog.d(error.message.toString())
                        onWithdrawFailure(error)
                    }.collect {
                        // 성공 시 로그아웃 로직 실행
                        onWithdrawSuccess()
                    }
            }
        }

        /**
         * 탈퇴 성공 시 호출되는 함수
         */
        private fun onWithdrawSuccess() {
            viewModelScope.launch {
                runCatching {
                    authRepository.logout()
                }.onSuccess {
                    _uiEvent.trySend(WithdrawUiEvent.NavigateToLogin)
                }.onFailure { exception ->
                    NearLog.d(exception.message.toString())
                    _uiState.value = _uiState.value.copy(isLoading = false)
                    _uiEvent.trySend(WithdrawUiEvent.ShowError(exception))
                }
            }
        }

        /**
         * 탈퇴 실패 시 호출되는 함수
         */
        private fun onWithdrawFailure(exception: Throwable) {
            _uiState.value = _uiState.value.copy(isLoading = false)
            _uiEvent.trySend(WithdrawUiEvent.ShowError(exception))
        }

        /**
         * 백 네비게이션 이벤트 발생
         */
        fun onNavigateBack() {
            _uiEvent.trySend(WithdrawUiEvent.NavigateBack)
        }
    }

/**
 * 탈퇴 화면의 UI 상태
 */
data class WithdrawUiState(
    val nickname: String = "",
    val selectedReason: WithdrawReason? = null,
    val otherReasonText: String = "",
    val isLoading: Boolean = false,
) {
    // 기타 사유가 선택되었는지 확인
    val isOtherReasonSelected: Boolean
        get() = selectedReason == WithdrawReason.REASON_OTHER

    // 기타 사유 텍스트 필드가 활성화되어야 하는지 확인
    val isOtherReasonTextFieldEnabled: Boolean
        get() = isOtherReasonSelected

    // 기타 사유 텍스트가 유효한지 확인
    val isOtherReasonTextValid: Boolean
        get() = !isOtherReasonSelected || otherReasonText.isNotEmpty()

    // 탈퇴하기 버튼이 활성화되어야 하는지 확인
    val isWithdrawButtonEnabled: Boolean
        get() = selectedReason != null && isOtherReasonTextValid && !isLoading
}

/**
 * 탈퇴 화면의 UI 이벤트
 */
sealed class WithdrawUiEvent {
    object NavigateBack : WithdrawUiEvent()

    object NavigateToLogin : WithdrawUiEvent()

    data class ShowError(
        val throwable: Throwable?,
    ) : WithdrawUiEvent()
}
