package com.alarmy.near.presentation.feature.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alarmy.near.data.local.datastore.OnboardingPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 온보딩 화면의 상태와 로직을 관리하는 ViewModel
 * 온보딩 완료 상태를 DataStore에 저장
 */
@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val onboardingPreferences: OnboardingPreferences,
) : ViewModel() {

    fun completeOnboarding() {
        viewModelScope.launch {
            onboardingPreferences.markOnboardingAsCompleted()
        }
    }
}
