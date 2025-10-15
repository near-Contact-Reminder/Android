package com.alarmy.near.core.exception

import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * 다양한 예외를 커스텀 예외로 변환하는 유틸리티
 */
object ExceptionMapper {
    /**
     * Throwable을 NearException으로 변환
     */
    fun mapToAppException(throwable: Throwable): NearException =
        when (throwable) {
            // 이미 NearException인 경우 그대로 반환
            is NearException -> throwable

            // 네트워크 연결 문제
            is UnknownHostException, is IOException -> {
                NearException.NetworkException
            }

            // 타임아웃 문제
            is SocketTimeoutException -> {
                NearException.TimeoutException
            }

            // HTTP 에러
            is HttpException -> {
                mapHttpExceptionToAppException(throwable)
            }

            // 기타 예상치 못한 예외
            else -> {
                NearException.UnknownException(throwable)
            }
        }

    /**
     * HTTP 상태 코드별 NearException 매핑
     */
    private fun mapHttpExceptionToAppException(httpException: HttpException): NearException =
        when (httpException.code()) {
            // 401: 인증 실패
            401 -> NearException.AuthException

            // 403: 권한 없음
            403 -> NearException.AuthException

            // 400, 404 등 클라이언트 에러
            in 400..499 -> {
                // 서버에서 에러 메시지를 제공하는지 확인
                val errorMessage =
                    try {
                        httpException.response()?.errorBody()?.string()
                    } catch (e: Exception) {
                        null
                    }

                NearException.ClientException(
                    errorCode = httpException.code().toString(),
                    errorMessage = errorMessage,
                )
            }

            // 500번대: 서버 에러
            in 500..599 -> {
                val errorMessage =
                    try {
                        httpException.response()?.errorBody()?.string()
                    } catch (e: Exception) {
                        null
                    }

                NearException.ServerException(
                    errorCode = httpException.code().toString(),
                    errorMessage = errorMessage,
                )
            }

            // 기타 HTTP 에러
            else -> {
                NearException.UnknownException(httpException)
            }
        }
}
