package com.alarmy.near.data.repository

import com.alarmy.near.model.Friend
import kotlinx.coroutines.flow.Flow

interface FriendRepository {
    fun fetchFriends(): Flow<List<Friend>>
}
