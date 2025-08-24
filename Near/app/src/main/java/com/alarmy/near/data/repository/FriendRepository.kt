package com.alarmy.near.data.repository

import com.alarmy.near.model.FriendSummary
import com.alarmy.near.model.monthly.MonthlyFriend
import kotlinx.coroutines.flow.Flow

interface FriendRepository {
    fun fetchFriends(): Flow<List<FriendSummary>>

    fun fetchMonthlyFriends(): Flow<List<MonthlyFriend>>
}
