package com.alarmy.near.presentation.feature.login.auth

import android.content.Context
import com.alarmy.near.R
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ApiError
import com.kakao.sdk.common.model.AuthError
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/**
 * 카카오 로그인을 담당하는 매니저 클래스
 */
class KakaoLoginManager(
    private val context: Context,
) : SocialLoginProvider {
    /**
     * 카카오 로그인 수행
     * KakaoTalk이 설치되어 있으면 KakaoTalk으로, 아니면 웹 계정으로 로그인합니다.
     */
    override suspend fun performLogin(
        onSuccess: (String) -> Unit,
        onFailure: (Throwable) -> Unit,
    ) {
        unlinkKakao()
        try {
            val accessToken = fetchAccessToken()
            onSuccess(accessToken)
        } catch (error: ClientError) {
            if (error.reason == ClientErrorCause.Cancelled) {
                onFailure(Exception(context.getString(R.string.login_user_cancelled)))
            } else {
                onFailure(Exception(context.getString(R.string.login_client_error)))
            }
        } catch (error: AuthError) {
            // OAuth 인증 과정 에러
            onFailure(Exception(context.getString(R.string.login_auth_error)))
        } catch (error: ApiError) {
            // API 호출 에러
            onFailure(Exception(context.getString(R.string.login_api_error)))
        } catch (error: KakaoSdkError) {
            // 카카오 SDK 에러
            onFailure(Exception(context.getString(R.string.login_sdk_error)))
        } catch (exception: Exception) {
            onFailure(exception)
        }
    }

    // 카카오 로그아웃 (언링크)
    private suspend fun unlinkKakao(): Unit =
        suspendCancellableCoroutine { continuation ->
            UserApiClient.instance.unlink { error ->
                continuation.resume(Unit)
            }
        }

    // 카카오톡을 통한 로그인
    private suspend fun loginWithKakaoTalk(): String =
        suspendCancellableCoroutine { continuation ->
            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                handleLoginResult(
                    token = token,
                    error = error,
                    continuation = continuation,
                )
            }
        }

    // 카카오 계정을 통한 로그인
    private suspend fun loginWithKakaoAccount(): String =
        suspendCancellableCoroutine { continuation ->
            UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
                handleLoginResult(
                    token = token,
                    error = error,
                    continuation = continuation,
                )
            }
        }

    private suspend fun fetchAccessToken(): String {
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context).not()) {
            return loginWithKakaoAccount()
        }
        return runCatching { loginWithKakaoTalk() }
            .getOrElse {
                // 카카오톡 로그인 단계에서 실패하면 즉시 웹 계정 로그인으로 폴백
                loginWithKakaoAccount()
            }
    }

    // 카카오 로그인 결과 처리
    private fun handleLoginResult(
        token: OAuthToken?,
        error: Throwable?,
        continuation: CancellableContinuation<String>,
    ) {
        when {
            token != null -> continuation.resume(token.accessToken)
            error != null ->
                continuation.resumeWith(
                    Result.failure(error),
                )
            else ->
                continuation.resumeWith(
                    Result.failure(
                        Exception(context.getString(R.string.login_user_cancelled)),
                    ),
                )
        }
    }
}
