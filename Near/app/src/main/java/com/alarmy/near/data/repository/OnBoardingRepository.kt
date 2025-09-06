package com.alarmy.near.data.repository

import kotlinx.coroutines.flow.Flow

/**
 * 온보딩 관련 데이터를 관리하는 Repository 인터페이스
 * 온보딩 완료 상태의 저장, 조회, 확인 기능을 제공
 */
interface OnBoardingRepository {

    /**
     * 온보딩 완료 여부를 확인하는 Flow
     * true: 온보딩 완료됨, false: 온보딩 미완료
     */
    fun observeOnboardingStatus(): Flow<Boolean>

    /**
     * 온보딩 완료 상태를 저장
     * completed: 온보딩 완료 여부
     */
    suspend fun setOnboardingCompleted(completed: Boolean)

    /**
     * 온보딩을 완료로 표시
     */
    suspend fun markOnboardingAsCompleted()

    /**
     * 온보딩 완료 상태를 확인
     */
    suspend fun isOnboardingCompleted(): Boolean

    /**
     * 온보딩 상태를 초기화 (테스트용 또는 재온보딩용)
     */
    suspend fun resetOnboardingStatus()
}
