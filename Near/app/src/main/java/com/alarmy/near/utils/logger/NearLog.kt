package com.alarmy.near.utils.logger

import android.util.Log
import com.alarmy.near.BuildConfig

object NearLog {
    private const val TAG = "Near"
    private const val LOGGER_FILE_NAME = "NearLog.kt"

    private fun buildLogMessage(message: String): String =
        runCatching {
            val stackTrace = Thread.currentThread().stackTrace

            // 스택 트레이스를 순회하며 로거 파일이 아닌 첫 번째 호출자를 찾습니다
            // 인덱스 0: Thread.getStackTrace()
            // 인덱스 1: 현재 함수 (buildLogMessage)
            // 인덱스 2~: 로그 함수들 (d, e 등)
            // 그 이후: 실제 호출자
            val callerElement =
                stackTrace.drop(2).firstOrNull { element ->
                    element.fileName != LOGGER_FILE_NAME
                } ?: throw IllegalStateException("Caller not found")

            val fileName =
                callerElement.fileName
                    ?.substringBeforeLast('.')
                    ?: "Unknown"

            val methodName = callerElement.methodName ?: "unknownMethod"
            val lineNumber = callerElement.lineNumber
            val originalFileName = callerElement.fileName ?: "Unknown"

            "[$fileName::$methodName ($originalFileName:$lineNumber)] $message"
        }.getOrElse { exception ->
            // 스택 트레이스 분석 실패 시 간단한 포맷으로 대체하여 로깅 기능 유지
            "[LogError:${exception.javaClass.simpleName}] $message"
        }

    // 디버그 모드 확인
    private fun isLoggingEnabled(): Boolean = BuildConfig.DEBUG

    /**
     * 공통 로그 출력 함수
     * 모든 로그 레벨에서 공통으로 사용되는 로직을 통합합니다
     */
    private fun writeLog(
        level: Int,
        tag: String,
        message: String,
        throwable: Throwable? = null,
    ) {
        if (!isLoggingEnabled()) return

        val formattedMessage = buildLogMessage(message)

        when (level) {
            Log.VERBOSE -> Log.v(tag, formattedMessage)
            Log.DEBUG -> Log.d(tag, formattedMessage)
            Log.INFO -> Log.i(tag, formattedMessage)
            Log.WARN -> Log.w(tag, formattedMessage)
            Log.ERROR -> Log.e(tag, formattedMessage, throwable)
        }
    }

    // Verbose 로그
    fun v(
        message: String,
        tag: String = TAG,
    ) = writeLog(Log.VERBOSE, tag, message)

    // Debug 로그
    fun d(
        message: String,
        tag: String = TAG,
    ) = writeLog(Log.DEBUG, tag, message)

    // Info 로그
    fun i(
        message: String,
        tag: String = TAG,
    ) = writeLog(Log.INFO, tag, message)

    // Warning 로그
    fun w(
        message: String,
        tag: String = TAG,
    ) = writeLog(Log.WARN, tag, message)

    // Error 로그
    fun e(
        message: String,
        throwable: Throwable? = null,
        name: String? = null,
        tag: String = TAG,
    ) {
        val finalMessage = if (name != null) "$name: $message" else message
        writeLog(Log.ERROR, tag, finalMessage, throwable)
    }
}
