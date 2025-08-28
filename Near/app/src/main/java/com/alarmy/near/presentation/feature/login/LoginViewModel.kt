package com.alarmy.near.presentation.feature.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

        // 에러 이벤트 관리
        private val _errorEvent = Channel<Throwable?>()
        val errorEvent = _errorEvent.receiveAsFlow()

        // 로그인 성공 이벤트 관리
        private val _loginSuccessEvent = Channel<Unit>()
        val loginSuccessEvent = _loginSuccessEvent.receiveAsFlow()

        /**
         * 소셜 로그인 수행
         *
         * @param accessToken 소셜 플랫폼에서 받은 Access Token
         * @param providerType 소셜 로그인 제공자 타입
         */
        fun performSocialLogin(
            accessToken: String,
            providerType: com.alarmy.near.model.ProviderType,
        ) {
            viewModelScope.launch {
                try {
                    updateLoadingState(isLoading = true)
                    val loginResult = authRepository.socialLogin(accessToken, providerType)

                    updateLoadingState(isLoading = false)

                    if (loginResult.isSuccess) {
                        _loginSuccessEvent.send(Unit)
                    } else {
                        val errorMsg = loginResult.errorMessage ?: "로그인에 실패했습니다"
                        _errorEvent.send(Exception(errorMsg))
                    }
                } catch (exception: Exception) {
                    updateLoadingState(isLoading = false)
                    _errorEvent.send(exception)
                }
            }
        }

        /**
         * 카카오 토큰으로 로그인 수행
         * UI에서 카카오 로그인을 완료한 후 토큰을 받아서 처리
         */
        fun performKakaoLogin(kakaoAccessToken: String) {
            performSocialLogin(kakaoAccessToken, ProviderType.KAKAO)
        }

        /**
         * 로그인 상태 확인
         */
        fun checkLoginStatus() {
            viewModelScope.launch {
                try {
                    val isLoggedIn = authRepository.isLoggedIn()
                    if (isLoggedIn) {
                        _loginSuccessEvent.send(Unit)
                    }
                } catch (exception: Exception) {
                    _errorEvent.send(exception)
                }
            }
        }

        /**
         * 로딩 상태 업데이트
         */
        private fun updateLoadingState(isLoading: Boolean) {
            _uiState.value = _uiState.value.copy(isLoading = isLoading)
        }

        /**
         * 에러 상태 초기화
         */
        fun clearError() {
            _uiState.value = _uiState.value.copy(hasError = false)
        }
    }

/**
 * 로그인 화면 UI 상태
 */
data class LoginUiState(
    val isLoading: Boolean = false,
    val hasError: Boolean = false,
)
