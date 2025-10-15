package com.alarmy.near.core.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alarmy.near.core.exception.NearException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * ViewModel의 베이스 클래스
 * 공통된 에러 처리 로직을 제공하여 일관된 에러 처리 패턴을 구현
 */
abstract class BaseViewModel : ViewModel() {
    // 에러 이벤트
    private val _errorEvent = Channel<Throwable>()
    val errorEvent: Flow<Throwable> = _errorEvent.receiveAsFlow()

    /**
     * 에러를 채널로 전송하는 함수
     * 하위 ViewModel에서 에러 발생 시 호출
     */
    protected fun sendErrorEvent(throwable: Throwable) {
        viewModelScope.launch {
            _errorEvent.send(throwable)
        }
    }

    /**
     * Flow에 에러 처리를 추가하는 확장 함수
     *
     * repository.fetchData()
     *     .handleError()
     *     .stateIn(...)
     */
    protected fun <T> Flow<T>.handleError(): Flow<T> =
        this.catch { throwable ->
            sendErrorEvent(throwable)
        }

    /**
     * 서버에서 받은 NearException을 처리합니다.
     *
     */
    protected fun handleAppException(
        throwable: Throwable,
        customMessage: String? = null,
    ) {
        val exceptionToSend =
            customMessage?.let {
                // customMessage가 있으면 DefaultException 생성
                NearException.DefaultException(
                    customMessage = customMessage,
                    originalException = throwable,
                )
            } ?: run {
                // customMessage가 없으면 원본 throwable 사용
                throwable
            }

        sendErrorEvent(exceptionToSend)
    }

    /**
     * 공통적으로 사용되는 에러 처리 로직
     *
     * try-catch 블록에서 사용:
     * catch (exception: Exception) {
     *     handleError(exception, "데이터를 불러오는데 실패했습니다")
     * }
     */
    protected fun handleError(
        throwable: Throwable,
        userMessage: String? = null,
    ) {
        // 에러 이벤트 전송
        handleAppException(throwable, userMessage)
    }
}
