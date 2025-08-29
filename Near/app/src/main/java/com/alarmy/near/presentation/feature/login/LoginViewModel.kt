package com.alarmy.near.presentation.feature.login

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
         */
        fun performLogin(providerType: ProviderType) {
            viewModelScope.launch {
                try {
                    updateLoadingState(isLoading = true)

                    val loginResult = authRepository.performSocialLogin(providerType)

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
         * 로딩 상태 업데이트
         */
        private fun updateLoadingState(isLoading: Boolean) {
            _uiState.value = _uiState.value.copy(isLoading = isLoading)
        }
    }

/**
 * 로그인 화면 UI 상태
 */
data class LoginUiState(
    val isLoading: Boolean = false,
    val hasError: Boolean = false,
)
