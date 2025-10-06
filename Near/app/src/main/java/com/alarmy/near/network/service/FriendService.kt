package com.alarmy.near.network.service

import com.alarmy.near.network.request.FriendInitRequest
import com.alarmy.near.network.request.FriendRequest
import com.alarmy.near.network.response.CommonMessageEntity
import com.alarmy.near.network.response.FriendEntity
import com.alarmy.near.network.response.FriendInitEntity
import com.alarmy.near.network.response.FriendRecordEntity
import com.alarmy.near.network.response.FriendSummaryEntity
import com.alarmy.near.network.response.MonthlyFriendEntity
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface FriendService {
    @GET("/friend/list")
    suspend fun fetchFriends(): List<FriendSummaryEntity>

    @GET("/friend/monthly")
    suspend fun fetchMonthlyFriends(): List<MonthlyFriendEntity>

    @GET("/friend/{friendId}")
    suspend fun fetchFriendById(
        @Path("friendId") friendId: String,
    ): FriendEntity

    @PUT("/friend/{friendId}")
    suspend fun updateFriend(
        @Path("friendId") friendId: String,
        @Body friendRequest: FriendRequest,
    ): FriendEntity

    @DELETE("/friend/{friendId}")
    suspend fun deleteFriend(
        @Path("friendId") friendId: String,
    )

    @GET("/friend/record/{friendId}")
    suspend fun fetchFriendRecord(
        @Path("friendId") friendId: String,
    ): List<FriendRecordEntity>

    @POST("/friend/record/{friendId}")
    suspend fun recordContact(
        @Path("friendId") friendId: String,
    ): CommonMessageEntity

    @POST("/friend/init")
    suspend fun initFriends(
        @Body friendInitRequest: FriendInitRequest,
    ): FriendInitEntity
}
