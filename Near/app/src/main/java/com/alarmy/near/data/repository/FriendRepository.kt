package com.alarmy.near.data.repository

import com.alarmy.near.model.Friend
import com.alarmy.near.model.FriendRecord
import com.alarmy.near.model.FriendSummary
import com.alarmy.near.model.monthly.MonthlyFriend
import kotlinx.coroutines.flow.Flow

interface FriendRepository {
    fun fetchFriends(): Flow<List<FriendSummary>>

    fun fetchMonthlyFriends(): Flow<List<MonthlyFriend>>

    fun fetchFriendById(friendId: String): Flow<Friend>

    fun updateFriend(
        friendId: String,
        friend: Friend,
    ): Flow<Friend>

    fun deleteFriend(friendId: String): Flow<Unit>

    fun fetchFriendRecord(friendId: String): Flow<List<FriendRecord>>

    fun recordContact(friendId: String): Flow<String>
}
