package com.alarmy.near.core.exception

import com.alarmy.near.R

/**
 * Near 애플리케이션에서 발생하는 커스텀 예외들
 *
 * data 계층에서 서버/네트워크 에러를 이 예외들로 래핑하여
 * presentation 계층에서 Retrofit 등 구체적인 구현에 의존하지 않도록 함
 *
 * messageResId를 통해 다국어 지원 및 일관된 메시지 관리
 */
sealed class NearException(
    val messageResId: Int,
) : Exception() {
    /**
     * 네트워크 연결 문제
     */
    object NetworkException : NearException(R.string.error_network)

    /**
     * 인증/권한 관련 에러 (401, 403)
     */
    object AuthException : NearException(R.string.error_auth)

    /**
     * 서버 에러 (500번대)
     */
    data class ServerException(
        val errorCode: String? = null,
        val errorMessage: String? = null,
    ) : NearException(R.string.error_server)

    /**
     * 클라이언트 에러 (400번대)
     */
    data class ClientException(
        val errorCode: String? = null,
        val errorMessage: String? = null,
    ) : NearException(R.string.error_client)

    /**
     * 타임아웃 에러
     */
    object TimeoutException : NearException(R.string.error_timeout)

    /**
     * 예상치 못한 에러
     */
    data class UnknownException(
        val originalException: Throwable,
    ) : NearException(R.string.error_unknown)

    /**
     * 기본 에러 (ViewModel에서 customMessage와 함께 사용)
     */
    data class DefaultException(
        val customMessage: String,
        val originalException: Throwable? = null,
    ) : NearException(R.string.error_default)
}
