package com.alarmy.near.data.datasource

import android.content.Context
import com.alarmy.near.model.ProviderType
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

/**
 * 카카오 로그인 데이터 소스
 * 단일 책임: 카카오 SDK만 처리
 */
@Singleton
class KakaoDataSource
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
    ) : SocialLoginDataSource {
        override val supportedType: ProviderType = ProviderType.KAKAO

        override suspend fun login(): Result<String> =
            try {
                val token =
                    if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
                        loginWithKakaoTalk(context)
                    } else {
                        loginWithKakaoAccount(context)
                    }

                if (token.isNotEmpty()) {
                    Result.success(token)
                } else {
                    Result.failure(Exception("사용자가 로그인을 취소했습니다"))
                }
            } catch (exception: Exception) {
                Result.failure(exception)
            }

        private suspend fun loginWithKakaoTalk(context: Context): String =
            suspendCancellableCoroutine { continuation ->
                UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                    when {
                        error != null -> {
                            if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                                continuation.resume("")
                            } else {
                                UserApiClient.instance.loginWithKakaoAccount(context) { retryToken, retryError ->
                                    handleLoginResult(retryToken, retryError, continuation)
                                }
                            }
                        }
                        token != null -> continuation.resume(token.accessToken)
                        else -> continuation.resume("")
                    }
                }
            }

        private suspend fun loginWithKakaoAccount(context: Context): String =
            suspendCancellableCoroutine { continuation ->
                UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
                    handleLoginResult(token, error, continuation)
                }
            }

        private fun handleLoginResult(
            token: OAuthToken?,
            error: Throwable?,
            continuation: CancellableContinuation<String>,
        ) {
            when {
                error != null -> {
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        continuation.resume("")
                    } else {
                        continuation.resumeWith(Result.failure(error))
                    }
                }
                token != null -> continuation.resume(token.accessToken)
                else -> continuation.resume("")
            }
        }
    }
