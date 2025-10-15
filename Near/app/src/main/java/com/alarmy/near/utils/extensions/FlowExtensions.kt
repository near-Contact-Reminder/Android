package com.alarmy.near.utils.extensions

import com.alarmy.near.core.exception.ExceptionMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

inline fun <T> apiCallFlow(crossinline apiCall: suspend () -> T): Flow<T> =
    flow {
        try {
            emit(apiCall())
        } catch (throwable: Throwable) {
            // NearException으로 변환 후 재throw
            val nearException = ExceptionMapper.mapToAppException(throwable)
            throw nearException
        }
    }
