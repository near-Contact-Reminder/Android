package com.alarmy.near.utils.extensions

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

inline fun <T> apiCallFlow(crossinline apiCall: suspend () -> T): Flow<T> =
    flow {
        emit(apiCall())
    }
