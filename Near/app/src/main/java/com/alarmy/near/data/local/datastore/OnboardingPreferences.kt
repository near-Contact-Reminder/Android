package com.alarmy.near.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.alarmy.near.data.di.OnboardingDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 온보딩 완료 상태를 관리하는 DataStore
 * 최초 접속 여부를 확인하여 온보딩 화면 표시 여부를 결정
 */
@Singleton
class OnboardingPreferences @Inject constructor(
    @OnboardingDataStore private val dataStore: DataStore<Preferences>,
) {
    companion object {
        private val ONBOARDING_COMPLETED_KEY = booleanPreferencesKey("onboarding_completed")
    }

    /**
     * 온보딩 완료 여부를 확인하는 Flow
     * true: 온보딩 완료됨, false: 온보딩 미완료
     */
    val isOnboardingCompleted: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[ONBOARDING_COMPLETED_KEY] ?: false
    }

    /**
     * 온보딩 완료 상태를 저장
     */
    suspend fun setOnboardingCompleted(completed: Boolean) {
        dataStore.edit { preferences ->
            preferences[ONBOARDING_COMPLETED_KEY] = completed
        }
    }

    /**
     * 온보딩 완료 상태를 true로 설정
     */
    suspend fun markOnboardingAsCompleted() {
        setOnboardingCompleted(true)
    }
}
