package com.alarmy.near.network.model

/**
 * 인증 관련 API 엔드포인트
 * 
 * 인증이 필요없는 경로들을 관리하여 TokenInterceptor에서 
 * Authorization 헤더 추가를 제외할 수 있습니다.
 */
enum class AuthEndpoint(val path: String) {
    SOCIAL_LOGIN("/auth/social"),
    TOKEN_RENEW("/auth/renew"),
    ;

    companion object {
        /**
         * 인증이 필요없는 모든 경로 목록
         * TokenInterceptor에서 이 경로들은 Authorization 헤더를 추가하지 않습니다
         */
        val excludedPaths: List<String> = entries.map { it.path }
    }
}
