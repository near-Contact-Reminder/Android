package com.alarmy.near.network.auth

import com.alarmy.near.data.local.datastore.TokenPreferences
import com.alarmy.near.network.request.TokenRefreshRequest
import com.alarmy.near.network.service.AuthService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

/**
 * 토큰 관리자
 * 토큰 저장, 조회, 갱신을 담당하는 중앙 관리 클래스
 * Provider 패턴으로 순환 참조 해결
 */
@Singleton
class TokenManager
@Inject
constructor(
    private val tokenPreferences: TokenPreferences,
    private val authServiceProvider: Provider<AuthService>, // Provider로 지연 주입
) {
    
    private val refreshMutex = Mutex()
    
    /**
     * 현재 액세스 토큰 가져오기
     */
    suspend fun getAccessToken(): String? {
        return tokenPreferences.getAccessToken()
    }
    
    /**
     * 토큰 갱신 시도
     * @return 갱신 성공 여부
     */
    suspend fun refreshToken(): Boolean {
        return refreshMutex.withLock {
            try {
                val refreshToken = tokenPreferences.getRefreshToken() ?: return false
                
                // Provider를 통해 AuthService 가져오기 (지연 주입)
                val authService = authServiceProvider.get()
                
                // 토큰 갱신 API 호출
                val request = TokenRefreshRequest(refreshToken = refreshToken)
                val response = authService.renewToken(request)
                
                // 새로운 토큰 저장
                val expiresIn = calculateExpiresIn(response.refreshTokenInfo?.expiresAt)
                
                tokenPreferences.saveTokens(
                    accessToken = response.accessToken,
                    refreshToken = response.refreshTokenInfo?.token,
                    expiresIn = expiresIn,
                )
                
                true
            } catch (e: Exception) {
                // 갱신 실패 시 토큰 삭제
                tokenPreferences.clearAllTokens()
                false
            }
        }
    }
    
    /**
     * 토큰이 유효한지 확인
     */
    suspend fun hasValidToken(): Boolean {
        return tokenPreferences.hasValidTokens()
    }
    
    /**
     * 모든 토큰 삭제
     */
    suspend fun clearAllTokens() {
        tokenPreferences.clearAllTokens()
    }
    
    /**
     * 로그인 상태 관찰
     */
    fun observeLoginStatus(): Flow<Boolean> {
        return tokenPreferences.observeLoginStatus()
    }
    
    /**
     * 토큰 저장
     */
    suspend fun saveTokens(
        accessToken: String,
        refreshToken: String?,
        expiresIn: Long?,
    ) {
        tokenPreferences.saveTokens(
            accessToken = accessToken,
            refreshToken = refreshToken,
            expiresIn = expiresIn,
        )
    }
    
    /**
     * 만료 시간 계산 (초 단위)
     */
    fun calculateExpiresIn(expiresAtString: String?): Long? {
        return expiresAtString?.let { expiresAt ->
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val expiresAtTime = dateFormat.parse(expiresAt)
            val currentTime = System.currentTimeMillis()
            ((expiresAtTime?.time ?: currentTime) - currentTime) / 1000 // 초 단위로 변환
        }
    }
}
