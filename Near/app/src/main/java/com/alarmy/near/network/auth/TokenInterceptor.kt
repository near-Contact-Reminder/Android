package com.alarmy.near.network.auth

import com.alarmy.near.data.local.datastore.TokenPreferences
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 토큰 인터셉터
 * Authorization 헤더를 자동으로 추가하고 인증이 필요없는 요청은 제외
 */
@Singleton
class TokenInterceptor
    @Inject
    constructor(
        private val tokenPreferences: TokenPreferences,
    ) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalRequest = chain.request()

            if (isAuthExcludedRequest(originalRequest)) {
                return chain.proceed(originalRequest)
            }

            val requestWithAuth =
                runBlocking {
                    addAuthHeader(originalRequest)
                }

            return chain.proceed(requestWithAuth)
        }

        /**
         * 인증이 필요없는 요청인지 확인
         */
        private fun isAuthExcludedRequest(request: Request): Boolean {
            val url = request.url.toString()
            return url.contains("/auth/social") ||
                url.contains("/auth/renew")
        }

        /**
         * Authorization 헤더 추가
         */
        private suspend fun addAuthHeader(originalRequest: Request): Request {
            val accessToken = tokenPreferences.getAccessToken()

            return if (accessToken != null) {
                originalRequest
                    .newBuilder()
                    .header("Authorization", "Bearer $accessToken")
                    .build()
            } else {
                originalRequest
            }
        }
    }
