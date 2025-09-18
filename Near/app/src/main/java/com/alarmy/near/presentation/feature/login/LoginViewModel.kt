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

        // 이벤트 관리
        private val _event = Channel<LoginEvent>()
        val event = _event.receiveAsFlow()

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
                        _event.send(LoginEvent.NavigateToHome)
                    }.onFailure { exception ->
                        updateLoadingState(isLoading = false)
                        _event.send(LoginEvent.ShowError(exception))
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

/*
* 로그인 화면 이벤트 관리
*
* */
sealed class LoginEvent {
    object ShowPrivacyBottomSheet : LoginEvent()

    object NavigateToHome : LoginEvent()

    data class ShowError(
        val throwable: Throwable?,
    ) : LoginEvent()
}
