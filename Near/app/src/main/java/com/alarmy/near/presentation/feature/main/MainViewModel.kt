package com.alarmy.near.presentation.feature.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alarmy.near.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    // UI 상태 관리
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        checkLoginStatus()
    }

    /**
     * 로그인 상태를 확인하고 스플래시 스크린을 제어합니다.
     * API 스플래시가 표시되는 동안 백그라운드에서 검증을 수행합니다.
     */
    private fun checkLoginStatus() {
        viewModelScope.launch {

            // 로그인 상태 검증
            val isLoggedIn = runCatching {
                authRepository.isLoggedIn()
            }.getOrElse { false }

            // UI 상태 업데이트
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                isLoggedIn = isLoggedIn
            )
        }
    }
}

/**
 * MainActivity의 UI 상태를 관리하는 데이터 클래스
 */
data class MainUiState(
    val isLoading: Boolean = true,
    val isLoggedIn: Boolean = false,
)
