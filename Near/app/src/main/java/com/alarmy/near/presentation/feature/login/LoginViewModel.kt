package com.alarmy.near.presentation.feature.login

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alarmy.near.BuildConfig
import com.alarmy.near.R
import com.alarmy.near.data.repository.AuthRepository
import com.alarmy.near.model.ProviderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
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

        // 약관 동의 상태 관리
        private val _termsAgreementState = MutableStateFlow(TermsAgreementState())
        val termsAgreementState: StateFlow<TermsAgreementState> = _termsAgreementState.asStateFlow()

        /**
         * 소셜 로그인 수행
         */
        fun performLogin(providerType: ProviderType) {
            viewModelScope.launch {
                updateLoadingState(isLoading = true)
                authRepository
                    .performSocialLogin(providerType)
                    .onSuccess {
                        updateLoadingState(isLoading = false)
                        _event.send(LoginEvent.ShowPrivacyBottomSheet)
                    }.onFailure { exception ->
                        updateLoadingState(isLoading = false)
                        _event.send(LoginEvent.ShowError(exception))
                    }
            }
        }

        /**
         * 개인정보 동의 완료 처리
         */
        fun onPrivacyConsentComplete() {
            viewModelScope.launch {
                _event.send(LoginEvent.NavigateToHome)
            }
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
            val currentState = _termsAgreementState.value
            val newAgreedState = !currentState.isAllAgreed

            _termsAgreementState.value =
                currentState.copy(
                    isAllAgreed = newAgreedState,
                    isServiceTermsAgreed = newAgreedState,
                    isPrivacyCollectionAgreed = newAgreedState,
                    isPrivacyPolicyAgreed = newAgreedState,
                )
        }

        /**
         * 개별 약관 동의 토글
         */
        fun toggleIndividualTermsAgreement(termType: TermType) {
            val currentState = _termsAgreementState.value
            val newState =
                when (termType) {
                    TermType.SERVICE_TERMS -> currentState.copy(isServiceTermsAgreed = !currentState.isServiceTermsAgreed)
                    TermType.PRIVACY_COLLECTION -> currentState.copy(isPrivacyCollectionAgreed = !currentState.isPrivacyCollectionAgreed)
                    TermType.PRIVACY_POLICY -> currentState.copy(isPrivacyPolicyAgreed = !currentState.isPrivacyPolicyAgreed)
                }

            // 모든 개별 약관이 동의되었는지 확인하여 전체 동의 상태 업데이트
            val isAllIndividualAgreed =
                newState.isServiceTermsAgreed &&
                    newState.isPrivacyCollectionAgreed &&
                    newState.isPrivacyPolicyAgreed

            _termsAgreementState.value = newState.copy(isAllAgreed = isAllIndividualAgreed)
        }

        /**
         * 약관 상세 보기 (웹뷰로 이동)
         */
        fun showTermsDetail(termType: TermType) {
            viewModelScope.launch {
                _event.send(LoginEvent.ShowTermsDetail(termType))
            }
        }
    }

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

/**
 * 약관 및 정책 타입
 */
enum class TermType(
    @StringRes val titleRes: Int,
    val url: String,
) {
    SERVICE_TERMS(
        titleRes = R.string.terms_service_agreed,
        url = BuildConfig.SERVICE_AGREED_TERMS_URL,
    ),
    PRIVACY_COLLECTION(
        titleRes = R.string.terms_personal_info,
        url = BuildConfig.PERSONAL_INFO_TERMS_URL,
    ),
    PRIVACY_POLICY(
        titleRes = R.string.terms_privacy_policy,
        url = BuildConfig.PRIVACY_POLICY_TERMS_URL,
    ),
}

/*
* 로그인 화면 이벤트 관리
*
* */
sealed class LoginEvent {
    object ShowPrivacyBottomSheet : LoginEvent()

    object NavigateToHome : LoginEvent()

    data class ShowTermsDetail(
        val termType: TermType,
    ) : LoginEvent()

    data class ShowError(
        val throwable: Throwable?,
    ) : LoginEvent()
}
