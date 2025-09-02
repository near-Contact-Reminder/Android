package com.alarmy.near.utils.logger

import android.util.Log
import com.alarmy.near.BuildConfig

private const val TAG = "Near"
private const val LOGGER_FILE_NAME = "NearLog.kt"

private fun buildLogMessage(message: String): String {
    return try {
        val stackTrace = Thread.currentThread().stackTrace

        // 스택 트레이스를 순회하며 로거 파일이 아닌 첫 번째 호출자를 찾습니다
        // 인덱스 0: Thread.getStackTrace()
        // 인덱스 1: 현재 함수 (buildLogMessage)
        // 인덱스 2~: 로그 함수들 (logd, loge 등)
        // 그 이후: 실제 호출자
        val callerElement =
            stackTrace.drop(2).firstOrNull { element ->
                element.fileName != LOGGER_FILE_NAME
            }

        if (callerElement == null) {
            return "[CallerNotFound] $message"
        }

        val fileName =
            callerElement.fileName
                ?.substringBeforeLast('.')
                ?: "Unknown"

        val methodName = callerElement.methodName ?: "unknownMethod"
        val lineNumber = callerElement.lineNumber
        val originalFileName = callerElement.fileName ?: "Unknown"

        "[$fileName::$methodName ($originalFileName:$lineNumber)] $message"
    } catch (exception: Exception) {
        "[LogError:${exception.javaClass.simpleName}] $message"
    }
}

/**
 * 디버그 모드인지 확인합니다
 */
private fun isLoggingEnabled(): Boolean = BuildConfig.DEBUG

// Verbose 로그
fun logv(
    message: String,
    tag: String = TAG,
) {
    if (!isLoggingEnabled()) return
    Log.v(tag, buildLogMessage(message))
}

// Debug 로그
fun logd(
    message: String,
    tag: String = TAG,
) {
    if (!isLoggingEnabled()) return
    Log.d(tag, buildLogMessage(message))
}

// Info 로그
fun logi(
    message: String,
    tag: String = TAG,
) {
    if (!isLoggingEnabled()) return
    Log.i(tag, buildLogMessage(message))
}

// Warning 로그
fun logw(
    message: String,
    tag: String = TAG,
) {
    if (!isLoggingEnabled()) return
    Log.w(tag, buildLogMessage(message))
}

// Error 로그
fun loge(
    message: String,
    tag: String = TAG,
) {
    if (!isLoggingEnabled()) return
    Log.e(tag, buildLogMessage(message))
}

// Error 로그 (이름 포함)
fun loge(
    name: String,
    message: String,
    tag: String = TAG,
) {
    if (!isLoggingEnabled()) return
    Log.e(tag, buildLogMessage("$name: $message"))
}

// Error 로그 (예외 포함)
fun loge(
    message: String,
    throwable: Throwable,
    tag: String = TAG,
) {
    if (!isLoggingEnabled()) return
    Log.e(tag, buildLogMessage(message), throwable)
}

// Error 로그 (이름과 예외 모두 포함)
fun loge(
    name: String,
    message: String,
    throwable: Throwable,
    tag: String = TAG,
) {
    if (!isLoggingEnabled()) return
    Log.e(tag, buildLogMessage("$name: $message"), throwable)
}
