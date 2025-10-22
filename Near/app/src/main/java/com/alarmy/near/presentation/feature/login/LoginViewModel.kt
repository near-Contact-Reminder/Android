package com.alarmy.near.presentation.feature.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alarmy.near.data.repository.AuthRepository
import com.alarmy.near.model.ProviderType
import com.alarmy.near.presentation.feature.login.model.TermType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
    @Inject
    constructor(
        private val authRepository: AuthRepository,
    ) : ViewModel() {
        // UI 상태 관리
        private val _uiState = MutableStateFlow(LoginUiState())
        val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

        // 이벤트 관리
        private val _event = Channel<LoginEvent>()
        val event = _event.receiveAsFlow()

        // 로그인 화면 상태 관리
        private val _loginState = MutableStateFlow(LoginState())
        val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

        // 개별 상태들을 편의를 위해 노출
        val termsAgreementState: StateFlow<TermsAgreementState> =
            _loginState
                .map { it.termsAgreementState }
                .stateIn(
                    viewModelScope,
                    SharingStarted.WhileSubscribed(),
                    TermsAgreementState(),
                )
        val showPrivacyBottomSheet: StateFlow<Boolean> =
            _loginState
                .map { it.showPrivacyBottomSheet }
                .stateIn(
                    viewModelScope,
                    SharingStarted.WhileSubscribed(),
                    false,
                )

        /**
         * 소셜 로그인 수행
         */
        fun onSocialLoginSuccess(
            accessToken: String,
            providerType: ProviderType,
        ) {
            viewModelScope.launch {
                authRepository
                    .performSocialLogin(accessToken, providerType)
                    .onSuccess {
                        updateLoadingState(isLoading = false)
                        _loginState.value = _loginState.value.copy(showPrivacyBottomSheet = true)
                    }.onFailure { exception ->
                        updateLoadingState(isLoading = false)
                        _event.send(LoginEvent.ShowError(exception))
                    }
            }
        }

        /**
         * 소셜 로그인 실패 시 에러 처리
         */
        fun onSocialLoginFailure(exception: Throwable) {
            viewModelScope.launch {
                updateLoadingState(isLoading = false)
                _event.send(LoginEvent.ShowError(exception))
            }
        }

        /**
         * 개인정보 동의 완료 처리
         */
        fun onPrivacyConsentComplete() {
            viewModelScope.launch {
                _loginState.value = _loginState.value.copy(showPrivacyBottomSheet = false)
                _event.send(LoginEvent.NavigateToHome)
            }
        }

        /**
         * 프라이버시 바텀시트 닫기
         */
        fun dismissPrivacyBottomSheet() {
            _loginState.value = _loginState.value.copy(showPrivacyBottomSheet = false)
        }

        /**
         * 로딩 상태 업데이트
         */
        private fun updateLoadingState(isLoading: Boolean) {
            _uiState.value = _uiState.value.copy(isLoading = isLoading)
        }

        /**
         * 약관 전체 동의 토글
         */
        fun toggleAllTermsAgreement() {
            val currentTermsState = _loginState.value.termsAgreementState
            val newAgreedState = !currentTermsState.isAllAgreed

            val updatedTermsState =
                currentTermsState.copy(
                    isAllAgreed = newAgreedState,
                    isServiceTermsAgreed = newAgreedState,
                    isPrivacyCollectionAgreed = newAgreedState,
                    isPrivacyPolicyAgreed = newAgreedState,
                )

            _loginState.value = _loginState.value.copy(termsAgreementState = updatedTermsState)
        }

        /**
         * 개별 약관 동의 토글
         */
        fun toggleIndividualTermsAgreement(termType: TermType) {
            val currentTermsState = _loginState.value.termsAgreementState
            val newTermsState =
                when (termType) {
                    TermType.SERVICE_TERMS -> currentTermsState.copy(isServiceTermsAgreed = !currentTermsState.isServiceTermsAgreed)
                    TermType.PRIVACY_COLLECTION ->
                        currentTermsState.copy(
                            isPrivacyCollectionAgreed = !currentTermsState.isPrivacyCollectionAgreed,
                        )

                    TermType.PRIVACY_POLICY -> currentTermsState.copy(isPrivacyPolicyAgreed = !currentTermsState.isPrivacyPolicyAgreed)
                }

            // 모든 개별 약관이 동의되었는지 확인하여 전체 동의 상태 업데이트
            val isAllIndividualAgreed =
                newTermsState.isServiceTermsAgreed &&
                    newTermsState.isPrivacyCollectionAgreed &&
                    newTermsState.isPrivacyPolicyAgreed

            val updatedTermsState = newTermsState.copy(isAllAgreed = isAllIndividualAgreed)
            _loginState.value = _loginState.value.copy(termsAgreementState = updatedTermsState)
        }

        /**
         * 약관 상세 보기 시 웹뷰로 이동했음을 표시
         */
        fun markNavigatedToWebView() {
            _loginState.value = _loginState.value.copy(hasNavigatedToWebView = true)
        }

        /**
         * 웹뷰에서 돌아온 후 바텀시트 복원
         */
        fun restoreBottomSheetIfNeeded() {
            val currentState = _loginState.value
            if (currentState.hasNavigatedToWebView && !currentState.showPrivacyBottomSheet) {
                _loginState.value =
                    currentState.copy(
                        showPrivacyBottomSheet = true,
                        hasNavigatedToWebView = false,
                    )
            }
        }
    }

/**
 * 로그인 화면 통합 상태
 */
data class LoginState(
    val termsAgreementState: TermsAgreementState = TermsAgreementState(),
    val hasNavigatedToWebView: Boolean = false,
    val showPrivacyBottomSheet: Boolean = false,
)

/**
 * 로그인 화면 UI 상태
 */
data class LoginUiState(
    val isLoading: Boolean = false,
    val hasError: Boolean = false,
)

/**
 * 약관 동의 상태
 */
data class TermsAgreementState(
    val isAllAgreed: Boolean = false,
    val isServiceTermsAgreed: Boolean = false,
    val isPrivacyCollectionAgreed: Boolean = false,
    val isPrivacyPolicyAgreed: Boolean = false,
) {
    /**
     * 모든 필수 약관에 동의했는지 확인
     */
    val isAllRequiredTermsAgreed: Boolean
        get() = isServiceTermsAgreed && isPrivacyCollectionAgreed && isPrivacyPolicyAgreed
}

/*
* 로그인 화면 이벤트 관리
*
* */
sealed class LoginEvent {
    object NavigateToHome : LoginEvent()

    data class ShowTermsDetail(
        val termType: TermType,
    ) : LoginEvent()

    data class ShowError(
        val throwable: Throwable?,
    ) : LoginEvent()
}
