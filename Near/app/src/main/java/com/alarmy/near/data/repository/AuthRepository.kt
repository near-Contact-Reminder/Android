package com.alarmy.near.data.repository

import com.alarmy.near.model.LoginResult
import com.alarmy.near.model.ProviderType
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    /**
     * 소셜 로그인 수행
     */
    suspend fun socialLogin(
        accessToken: String,
        providerType: ProviderType,
    ): LoginResult

    /**
     * 로그아웃 수행
     * 로컬에 저장된 토큰을 삭제
     */
    suspend fun logout()

    /**
     * 로그인 상태 확인
     * 저장된 토큰이 있는지 확인
     */
    suspend fun isLoggedIn(): Boolean

    /**
     * 현재 사용자 토큰 가져오기
     * 저장된 Access Token 반환
     */
    suspend fun getCurrentUserToken(): String?

    /**
     * 로그인 상태 관찰
     * 토큰 변화를 실시간으로 관찰
     */
    fun observeLoginStatus(): Flow<Boolean>
}
