package com.alarmy.near.data.repository

import com.alarmy.near.data.mapper.toModel
import com.alarmy.near.model.Friend
import com.alarmy.near.model.MonthlyFriend
import com.alarmy.near.network.service.FriendService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DefaultFriendRepository
    @Inject
    constructor(
        private val friendService: FriendService,
    ) : FriendRepository {
        override fun fetchFriends(): Flow<List<Friend>> =
            flow {
                emit(
                    friendService.fetchFriends().map {
                        it.toModel()
                    },
                )
            }

        override fun fetchMonthlyFriends(): Flow<List<MonthlyFriend>> =
            flow {
                emit(
                    friendService.fetchMonthlyFriends().map {
                        it.toModel()
                    },
                )
        }
}
