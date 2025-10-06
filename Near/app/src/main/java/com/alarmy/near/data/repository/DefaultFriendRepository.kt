package com.alarmy.near.data.repository

import com.alarmy.near.data.mapper.toFriendInitItemRequest
import com.alarmy.near.data.mapper.toModel
import com.alarmy.near.data.mapper.toRequest
import com.alarmy.near.model.Friend
import com.alarmy.near.model.FriendRecord
import com.alarmy.near.model.friendsummary.FriendSummary
import com.alarmy.near.model.monthly.MonthlyFriend
import com.alarmy.near.network.request.FriendInitRequest
import com.alarmy.near.network.response.FriendInitItemEntity
import com.alarmy.near.network.service.FriendService
import com.alarmy.near.presentation.feature.friendcontactcycle.model.FriendContactUIModel
import com.alarmy.near.utils.extensions.apiCallFlow
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

        override fun fetchFriendById(friendId: String): Flow<Friend> =
            flow {
                emit(friendService.fetchFriendById(friendId).toModel())
            }

        override fun updateFriend(
            friendId: String,
            friend: Friend,
        ): Flow<Friend> =
            flow {
                emit(friendService.updateFriend(friendId, friend.toRequest()).toModel())
            }

        override fun deleteFriend(friendId: String): Flow<Unit> =
            flow {
                friendService.deleteFriend(friendId)
                emit(Unit)
            }

        override fun fetchFriendRecord(friendId: String): Flow<List<FriendRecord>> =
            flow {
                emit(friendService.fetchFriendRecord(friendId).map { it.toModel() })
            }

        override fun recordContact(friendId: String): Flow<String> =
            flow {
                val response = friendService.recordContact(friendId)
                emit(response.message) // CommonMessageEntity.message 라고 가정
            }

        override fun initFriends(
            contacts: List<FriendContactUIModel>,
            providerType: String,
        ): Flow<List<FriendInitItemEntity>> =
            apiCallFlow {
                // UI 모델을 Data 모델로 변환
                val friendInitRequest =
                    FriendInitRequest(
                        friendList =
                            contacts
                                .filter { it.reminderInterval != null }
                                .map { it.toFriendInitItemRequest(providerType) },
                    )

                // 서버 요청 및 응답 반환
                friendService.initFriends(friendInitRequest).friendList
            }
    }
