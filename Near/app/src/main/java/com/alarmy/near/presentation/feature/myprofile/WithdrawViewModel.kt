package com.alarmy.near.presentation.feature.myprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alarmy.near.data.repository.AuthRepository
import com.alarmy.near.data.repository.MemberRepository
import com.alarmy.near.model.member.WithdrawRequest
import com.alarmy.near.presentation.feature.myprofile.model.WithdrawReason
import com.alarmy.near.utils.logger.NearLog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WithdrawViewModel @Inject constructor(
    private val memberRepository: MemberRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    // UI 상태 관리
    private val _uiState = MutableStateFlow(WithdrawUiState())
    val uiState: StateFlow<WithdrawUiState> = _uiState.asStateFlow()

    // UI 이벤트 관리
    private val _uiEvent = Channel<WithdrawUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    /**
     * 탈퇴 사유를 선택하는 함수
     */
    fun selectReason(reason: WithdrawReason) {
        _uiState.value = _uiState.value.copy(
            selectedReason = reason,
            errorMessage = null // 에러 메시지 초기화
        )
    }

    /**
     * 기타 사유 텍스트를 업데이트하는 함수
     */
    fun updateOtherReasonText(text: String) {
        _uiState.value = _uiState.value.copy(
            otherReasonText = text,
            errorMessage = null,
        )
    }

    /**
     * 탈퇴 요청을 처리하는 함수
     */
    fun submitWithdrawRequest() {
        val currentState = _uiState.value
        val reason = currentState.selectedReason

        if (reason == null) {
            _uiState.value = currentState.copy(
                errorMessage = "탈퇴 사유를 선택해주세요."
            )
            return
        }

        // 로딩 상태 시작
        _uiState.value = currentState.copy(
            isLoading = true,
            errorMessage = null
        )

        viewModelScope.launch {
            val request = WithdrawRequest(
                reasonType = reason.name,
                customReason = if (reason == WithdrawReason.REASON_OTHER) {
                    currentState.otherReasonText.takeIf { it.isNotEmpty() }
                } else null
            )

            memberRepository.withdraw(request)
                .onSuccess {
                    // 성공 시 로그아웃 로직 실행
                    onWithdrawSuccess()
                }
                .onFailure { exception ->
                    // 실패 시 에러 처리
                    NearLog.d(exception.message.toString())
                    onWithdrawFailure(exception.message ?: "탈퇴 처리 중 오류가 발생했습니다.")
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
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = exception.message ?: "로그아웃 처리 중 오류가 발생했습니다."
                )
            }
        }
    }

    /**
     * 탈퇴 실패 시 호출되는 함수
     */
    private fun onWithdrawFailure(errorMessage: String) {
        _uiState.value = _uiState.value.copy(
            isLoading = false,
            errorMessage = errorMessage
        )
    }
}

/**
 * 탈퇴 화면의 UI 상태
 */
data class WithdrawUiState(
    val selectedReason: WithdrawReason? = null,
    val otherReasonText: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

/**
 * 탈퇴 화면의 UI 이벤트
 */
sealed class WithdrawUiEvent {
    object NavigateToLogin : WithdrawUiEvent()
}
