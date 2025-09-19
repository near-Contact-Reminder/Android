package com.alarmy.near.data.repository

import com.alarmy.near.data.local.datastore.OnboardingPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

/**
 * OnBoardingRepository의 구현체
 * OnboardingPreferences를 사용하여 온보딩 상태를 관리
 */
@Singleton
class OnBoardingRepositoryImpl @Inject constructor(
    private val onboardingPreferences: OnboardingPreferences,
) : OnBoardingRepository {

    override fun observeOnboardingStatus(): Flow<Boolean> {
        return onboardingPreferences.isOnboardingCompleted
    }

    override suspend fun setOnboardingCompleted(completed: Boolean) {
        onboardingPreferences.setOnboardingCompleted(completed)
    }

    override suspend fun markOnboardingAsCompleted() {
        onboardingPreferences.markOnboardingAsCompleted()
    }

    override suspend fun isOnboardingCompleted(): Boolean {
        return onboardingPreferences.isOnboardingCompleted.first()
    }

    override suspend fun resetOnboardingStatus() {
        onboardingPreferences.setOnboardingCompleted(false)
    }
}
