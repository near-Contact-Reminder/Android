package com.alarmy.near.presentation.feature.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alarmy.near.data.local.datastore.OnboardingPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 온보딩 화면의 상태와 로직을 관리하는 ViewModel
 *  * 온보딩 완료 상태를 DataStore에 저장
 */
@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val onboardingPreferences: OnboardingPreferences,
) : ViewModel() {
    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<OnboardingEffect>()
    val effect = _effect.asSharedFlow()

    /**
     * 온보딩 완료 처리
     */
    fun completeOnboarding() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            runCatching {
                onboardingPreferences.markOnboardingAsCompleted()
            }.onSuccess {
                _effect.emit(OnboardingEffect.NavigateToLogin)
            }.onFailure { exception ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = exception.message
                )
            }
        }
    }

}

/**
 * 온보딩 UI 상태
 */
data class OnboardingUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
)

/**
 * 온보딩 사이드 이펙트
 */
sealed class OnboardingEffect {
    object NavigateToLogin : OnboardingEffect()
}
