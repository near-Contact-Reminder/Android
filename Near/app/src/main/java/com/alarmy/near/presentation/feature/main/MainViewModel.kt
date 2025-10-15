package com.alarmy.near.presentation.feature.main

import androidx.lifecycle.viewModelScope
import com.alarmy.near.core.viewmodel.BaseViewModel
import com.alarmy.near.data.repository.AuthRepository
import com.alarmy.near.data.repository.OnBoardingRepository
import com.alarmy.near.presentation.feature.home.navigation.RouteHome
import com.alarmy.near.presentation.feature.login.navigation.RouteLogin
import com.alarmy.near.presentation.feature.onboarding.navigation.RouteOnboarding
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel
    @Inject
    constructor(
        private val authRepository: AuthRepository,
        private val onBoardingRepository: OnBoardingRepository,
    ) : BaseViewModel() {
        // UI 상태 관리
        private val _uiState = MutableStateFlow(MainUiState())
        val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

        init {
            checkAppStatus()
        }

        /**
         * 앱 상태를 확인하고 스플래시 스크린을 제어합니다.
         * 온보딩 완료 여부와 로그인 상태를 확인하여 적절한 화면으로 이동합니다.
         */
        private fun checkAppStatus() {
            viewModelScope.launch {
                runCatching {
                    // 온보딩 완료 여부 확인
                    val isOnboardingCompleted = onBoardingRepository.isOnboardingCompleted()
                    // 로그인 상태 검증
                    val isLoggedIn = authRepository.isLoggedIn()

                    // 시작 화면 결정
                    val startDestination =
                        when {
                            !isOnboardingCompleted -> RouteOnboarding
                            isLoggedIn -> RouteHome
                            else -> RouteLogin
                        }

                    // UI 상태 업데이트
                    _uiState.value =
                        _uiState.value.copy(
                            isLoading = false,
                            isOnboardingCompleted = isOnboardingCompleted,
                            isLoggedIn = isLoggedIn,
                            startDestination = startDestination,
                        )
                }.onFailure {
                    // 에러 발생 시 기본값으로 설정
                    _uiState.value =
                        _uiState.value.copy(
                            isLoading = false,
                            isOnboardingCompleted = false,
                            isLoggedIn = false,
                            startDestination = RouteOnboarding,
                        )
                }
            }
        }
    }

/**
 * MainActivity의 UI 상태를 관리하는 데이터 클래스
 */
data class MainUiState(
    val isLoading: Boolean = true,
    val isOnboardingCompleted: Boolean = false,
    val isLoggedIn: Boolean = false,
    val startDestination: Any = RouteOnboarding,
)
