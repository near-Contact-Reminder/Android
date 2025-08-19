package com.alarmy.near.network.service

import com.alarmy.near.network.response.FriendEntity
import retrofit2.http.GET

interface FriendService {
    @GET
    suspend fun fetchFriends(): List<FriendEntity>
}
