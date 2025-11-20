package com.alarmy.near.data.repository

import com.alarmy.near.data.mapper.toFriendInitItemRequest
import com.alarmy.near.data.mapper.toImageUploadRequest
import com.alarmy.near.data.mapper.toModel
import com.alarmy.near.data.mapper.toRequest
import com.alarmy.near.local.contact.ContactImageData
import com.alarmy.near.local.contact.ContactImageReader
import com.alarmy.near.model.Friend
import com.alarmy.near.model.FriendRecord
import com.alarmy.near.model.friendsummary.FriendSummary
import com.alarmy.near.model.monthly.MonthlyFriend
import com.alarmy.near.network.request.FriendInitItemRequest
import com.alarmy.near.network.request.FriendInitRequest
import com.alarmy.near.network.response.FriendInitItemEntity
import com.alarmy.near.network.service.FriendService
import com.alarmy.near.network.uploader.ImageUploader
import com.alarmy.near.presentation.feature.friendcontactcycle.model.FriendContactUIModel
import com.alarmy.near.utils.extensions.apiCallFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DefaultFriendRepository
    @Inject
    constructor(
        private val friendService: FriendService,
        private val contactImageReader: ContactImageReader,
        private val imageUploader: ImageUploader,
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

        override fun fetchMonthlyCompleteFriends(): Flow<List<MonthlyFriend>> =
            apiCallFlow {
                friendService.fetchMonthlyCompleteFriends().map { it.toModel() }
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
                val payloads =
                    contacts
                        .filter { it.reminderInterval != null }
                        .map { contact ->
                            val imageData = contact.photoUri?.let { uri -> contactImageReader.read(uri) }
                            val request =
                                contact.toFriendInitItemRequest(
                                    providerType = providerType,
                                    imageUploadRequest = imageData?.toImageUploadRequest(PROFILE_IMAGE_CATEGORY),
                                )
                            FriendInitRequestPayload(
                                request = request,
                                imageData = imageData,
                            )
                        }
                val friendInitRequest = FriendInitRequest(friendList = payloads.map { it.request })
                val response = friendService.initFriends(friendInitRequest)
                response.friendList.forEachIndexed { index, entity ->
                    val uploadUrl = entity.preSignedImageUrl
                    val imageData = payloads.getOrNull(index)?.imageData
                    if (uploadUrl != null && imageData != null) {
                        imageUploader.upload(
                            url = uploadUrl,
                            contentType = imageData.contentType,
                            data = imageData.data,
                        )
                    }
                }
                response.friendList
            }

        private data class FriendInitRequestPayload(
            val request: FriendInitItemRequest,
            val imageData: ContactImageData?,
        )

        companion object {
            private const val PROFILE_IMAGE_CATEGORY = "PROFILE"
        }
    }
