package com.alarmy.near.presentation.feature.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alarmy.near.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository,
): ViewModel() {

    // 로그인 상태 확인 및 화면 전환
    fun checkLoginStatusAndNavigate(
        onNavigateToLogin: () -> Unit,
        onNavigateToHome: () -> Unit,
    ) {
        viewModelScope.launch {
            runCatching {
                delay(SPLASH_DELAY_MILLIS)
                authRepository.isLoggedIn()
            }.onSuccess { isLoggedIn ->
                when(isLoggedIn) {
                    true -> { onNavigateToHome() }
                    false -> { onNavigateToLogin() }
                }
            }.onFailure { exception ->
                onNavigateToLogin()
            }
        }
    }

    companion object {
        const val SPLASH_DELAY_MILLIS = 1000L
    }
}
