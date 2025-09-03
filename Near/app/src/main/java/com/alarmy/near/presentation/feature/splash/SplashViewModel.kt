package com.alarmy.near.presentation.feature.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alarmy.near.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val SPLASH_DELAY_MILLIS = 1000L

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _effect = MutableSharedFlow<SplashEffect>()
    val effect: SharedFlow<SplashEffect> = _effect.asSharedFlow()

    init {
        checkLoginStatus()
    }

    // 로그인 상태 확인
    private fun checkLoginStatus() {
        viewModelScope.launch {
            runCatching {
                delay(SPLASH_DELAY_MILLIS)
                authRepository.isLoggedIn()
            }.onSuccess { isLoggedIn ->
                when (isLoggedIn) {
                    true -> _effect.emit(SplashEffect.NavigateToHome)
                    false -> _effect.emit(SplashEffect.NavigateToLogin)
                }
            }.onFailure { exception ->
                _effect.emit(SplashEffect.NavigateToLogin)
            }
        }
    }
}

sealed class SplashEffect {
    object NavigateToHome : SplashEffect()
    object NavigateToLogin : SplashEffect()
}
