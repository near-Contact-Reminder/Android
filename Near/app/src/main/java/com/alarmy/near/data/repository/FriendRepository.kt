package com.alarmy.near.data.repository

import com.alarmy.near.model.Friend
import com.alarmy.near.model.MonthlyFriend
import kotlinx.coroutines.flow.Flow

interface FriendRepository {
    fun fetchFriends(): Flow<List<Friend>>

    fun fetchMonthlyFriends(): Flow<List<MonthlyFriend>>
}
