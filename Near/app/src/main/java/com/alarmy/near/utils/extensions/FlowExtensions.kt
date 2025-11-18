package com.alarmy.near.utils.extensions

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

inline fun <T> apiCallFlow(crossinline apiCall: suspend () -> T): Flow<T> =
    flow {
        emit(apiCall())
    }

fun <T, E> Flow<T>.handleError(
    scope: CoroutineScope,
    eventChannel: Channel<E>,
    createErrorEvent: (Throwable) -> E,
): Flow<T> =
    catch { exception ->
        scope.launch {
            eventChannel.send(createErrorEvent(exception))
        }
    }
