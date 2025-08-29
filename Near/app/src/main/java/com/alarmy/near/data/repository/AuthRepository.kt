package com.alarmy.near.data.repository

import com.alarmy.near.model.LoginResult
import com.alarmy.near.model.ProviderType
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    // 소셜 로그인 수행
    suspend fun performSocialLogin(providerType: ProviderType): LoginResult

    /**
     * 소셜 로그인 수행 (토큰 직접 전달)
     */
    suspend fun socialLogin(
        accessToken: String,
        providerType: ProviderType,
    ): LoginResult

    // 로그아웃 수행
    suspend fun logout()

    // 로그인 상태 확인
    suspend fun isLoggedIn(): Boolean

    // 현재 사용자 토큰 가져오기
    suspend fun getCurrentUserToken(): String?

    // 로그인 상태 확인
    fun observeLoginStatus(): Flow<Boolean>

    // 토큰 갱신
    suspend fun refreshToken(): Boolean
}
