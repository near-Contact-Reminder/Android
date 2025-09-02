package com.alarmy.near.utils.logger

import android.util.Log
import com.alarmy.near.BuildConfig

private const val TAG = "Near"
private const val STACK_TRACE_INDEX = 5

/**
 * 호출자 정보를 포함한 로그 메시지를 생성합니다
 * 스택 트레이스 추출에 실패하면 원본 메시지를 반환합니다
 */
private fun buildLogMessage(message: String): String {
    return try {
        val stackTrace = Thread.currentThread().stackTrace

        // 실제 호출자를 찾기 위해 스택을 순회
        for (i in 4 until stackTrace.size) {
            val element = stackTrace[i]
            val fileName = element.fileName ?: continue
            val methodName = element.methodName ?: continue

            // 로그 관련 메서드들을 건너뛰고 실제 호출자 찾기
            if (!methodName.startsWith("log") &&
                !methodName.contains("\$default") &&
                !fileName.contains("Log")
            ) {
                val cleanFileName =
                    fileName
                        .replace(".java", "")
                        .replace(".kt", "")

                return "[$cleanFileName::$methodName (${element.fileName}:${element.lineNumber})] $message"
            }
        }

        // 찾지 못하면 기본 인덱스 사용
        if (stackTrace.size > STACK_TRACE_INDEX) {
            val element = stackTrace[STACK_TRACE_INDEX]
            val fileName =
                element.fileName
                    ?.replace(".java", "")
                    ?.replace(".kt", "")
                    ?: "Unknown"

            "[$fileName::${element.methodName} (${element.fileName}:${element.lineNumber})] $message"
        } else {
            message
        }
    } catch (exception: Exception) {
        // 스택 트레이스 추출 실패 시 원본 메시지 반환
        message
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
