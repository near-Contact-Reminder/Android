package com.alarmy.near.data.repository

interface MessagingRepository {
    fun registerToken(token: String)

    fun unregisterToken(token: String)
}
