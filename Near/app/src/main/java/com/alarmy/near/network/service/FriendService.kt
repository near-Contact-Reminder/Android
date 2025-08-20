package com.alarmy.near.network.service

import com.alarmy.near.network.response.FriendEntity
import retrofit2.http.GET

interface FriendService {
    @GET("/friend/list")
    suspend fun fetchFriends(): List<FriendEntity>
}
