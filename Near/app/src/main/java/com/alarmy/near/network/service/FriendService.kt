package com.alarmy.near.network.service

import com.alarmy.near.network.response.FriendEntity
import com.alarmy.near.network.response.MonthlyFriendEntity
import retrofit2.http.GET

interface FriendService {
    @GET("/friend/list")
    suspend fun fetchFriends(): List<FriendEntity>

    @GET("friends/monthly")
    suspend fun fetchMonthlyFriends(): List<MonthlyFriendEntity>
}
