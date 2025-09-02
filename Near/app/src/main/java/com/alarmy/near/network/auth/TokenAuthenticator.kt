package com.alarmy.near.network.auth

import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 토큰 인증자
 * 401 에러 발생 시 토큰 갱신을 시도하고 원래 요청을 재시도
 */
@Singleton
class TokenAuthenticator
@Inject
constructor(
    private val tokenManager: TokenManager,
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        // 401 에러가 아니면 null 반환 (인증 시도하지 않음)
        if (response.code != 401) {
            return null
        }

        // runBlocking을 사용하여 suspend 함수 호출
        return runBlocking {
            try {
                // 토큰 갱신 시도
                val refreshSuccess = tokenManager.refreshToken()

                // 토큰 갱신 실패 시 null 반환 (재로그인 필요)
                if (!refreshSuccess) {
                    return@runBlocking null
                }

                // 새로운 토큰으로 원래 요청 재시도
                val newToken = tokenManager.getAccessToken()

                if (newToken != null) {
                    response.request
                        .newBuilder()
                        .header("Authorization", "Bearer $newToken")
                        .build()
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }
        }
    }
}
