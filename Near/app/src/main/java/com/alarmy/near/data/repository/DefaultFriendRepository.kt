package com.alarmy.near.data.repository

import com.alarmy.near.data.mapper.toModel
import com.alarmy.near.model.FriendSummary
import com.alarmy.near.model.monthly.MonthlyFriend
import com.alarmy.near.network.service.FriendService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DefaultFriendRepository
    @Inject
    constructor(
        private val friendService: FriendService,
    ) : FriendRepository {
        override fun fetchFriends(): Flow<List<FriendSummary>> =
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
