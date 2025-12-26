package com.alarmy.near.data.repository

import kotlinx.coroutines.flow.Flow

interface MessagingRepository {
    fun registerToken(token: String): Flow<Unit>

    fun unregisterToken(token: String): Flow<Unit>
}
