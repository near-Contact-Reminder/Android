package com.alarmy.near.presentation.feature.friendprofileedittor

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.alarmy.near.data.repository.FriendRepository
import com.alarmy.near.model.Friend
import com.alarmy.near.model.Relation
import com.alarmy.near.model.ReminderInterval
import com.alarmy.near.presentation.feature.friendprofileedittor.navigation.RouteFriendProfileEditor
import com.alarmy.near.presentation.feature.friendprofileedittor.uistate.AnniversaryUIState
import com.alarmy.near.presentation.feature.friendprofileedittor.uistate.FriendProfileEditorUIState
import com.alarmy.near.presentation.feature.friendprofileedittor.uistate.toModel
import com.alarmy.near.presentation.feature.friendprofileedittor.uistate.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class FriendProfileEditorViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        private val friendRepository: FriendRepository,
    ) : ViewModel() {
        private val routeFriendProfileEditor: RouteFriendProfileEditor =
            savedStateHandle.toRoute<RouteFriendProfileEditor>(RouteFriendProfileEditor.routeTypeMap)
        private val friend: Friend =
            routeFriendProfileEditor.friend.apply {
                copy()
            }
        private val _uiState: MutableStateFlow<FriendProfileEditorUIState> =
            MutableStateFlow(friend.toUiModel())
        val uiState = _uiState.asStateFlow()

        fun onNameChanged(value: String) {
            if (value.length > MAX_NAME_LENGTH) {
                return
            }
            _uiState.update {
                it.copy(
                    name =
                        it.name.copy(
                            value = value,
                            isDirty = true,
                            error = value.isEmpty(),
                        ),
                )
            }
        }

        fun onRelationChanged(value: Relation) {
            _uiState.update { it.copy(relation = value) }
        }

        fun onRemindIntervalChanged(value: ReminderInterval) {
            _uiState.update {
                it.copy(
                    contactFrequency =
                        it.contactFrequency.copy(
                            reminderInterval = value,
                        ),
                )
            }
        }

        fun onBirthdayChanged(value: Long) {
            _uiState.update {
                it.copy(
                    birthday =
                        it.birthday.copy(
                            value = convertMillisToDate(value),
                            isDirty = true,
                        ),
                )
            }
        }

        fun onAnniversaryTitleChanged(
            index: Int,
            value: String,
        ) {
            _uiState.update {
                it.copy(
                    anniversaries =
                        it.anniversaries.toMutableList().apply {
                            this[index] =
                                this[index].copy(
                                    title =
                                        this[index].title.copy(
                                            value = value,
                                            isDirty = true,
                                            error = value.isEmpty(),
                                        ),
                                )
                        },
                )
            }
        }

        fun onAnniversaryDateChanged(
            index: Int,
            value: Long,
        ) {
            _uiState.update {
                it.copy(
                    anniversaries =
                        it.anniversaries.toMutableList().apply {
                            this[index] =
                                this[index].copy(
                                    date =
                                        this[index].date.copy(
                                            value = convertMillisToDate(value),
                                            isDirty = true,
                                        ),
                                )
                        },
                )
            }
        }

        fun onAddAnniversary() {
            _uiState.update { it.copy(anniversaries = it.anniversaries + AnniversaryUIState()) }
        }

        fun onRemoveAnniversary(index: Int) {
            _uiState.update {
                it.copy(anniversaries = it.anniversaries.toMutableList().apply { removeAt(index) })
            }
        }

        fun onMemoChanged(value: String?) {
            value?.length?.let {
                if (it > MAX_MEMO_LENGTH) {
                    return
                }
            }
            _uiState.update {
                it.copy(
                    memo =
                        it.memo.copy(
                            value = value,
                            isDirty = true,
                        ),
                )
            }
        }

        private fun convertMillisToDate(millis: Long): String {
            val formatter = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
            return formatter.format(Date(millis))
        }

        fun onSubmit() {
            val updatedFriend = _uiState.value
            if (updatedFriend.name.error || updatedFriend.anniversaries.any { it.title.error }) {
                // error
                return
            }

            viewModelScope.launch {
                friendRepository
                    .updateFriend(
                        friendId = friend.friendId,
                        friend =
                            updatedFriend.toModel(
                                friendId = friend.friendId,
                                imageUrl = friend.imageUrl ?: "",
                                phone = friend.phone ?: "",
                                lastContactAt = friend.lastContactAt ?: "",
                            ),
                    ).collect {
                    }
            }
        }

        //                fun onSubmit() {
//            val model = _uiState.value
//            val validated =
//                model.copy(
//                    name = model.name.copy(error = if (model.name.value.isBlank()) "이름을 입력해주세요." else null),
//                    anniversaries =
//                        model.anniversaries.map { anniversary ->
//                            anniversary.copy(
//                                title = anniversary.title.copy(error = if (anniversary.title.value.isBlank()) "기념일 이름을 입력해주세요." else null),
//                                date = anniversary.date.copy(error = if (anniversary.date.value == null) "날짜를 선택해주세요." else null),
//                            )
//                        },
//                )
//
// //            _uiState.update { it.copy( = validated) }
//
//            // Validation 성공 시 Repository 저장
//        if (validated.name.error == null &&
//            validated.anniversaries.all { it.title.error == null && it.date.error == null }
//        ) {
//            _uiState.update { it.copy(isSubmitting = true) }
//            viewModelScope.launch {
//                // repository.save(validated.toDomain())
//                _uiState.update { it.copy(isSubmitting = false, isSuccess = true) }
//            }
//        }
//    }
//
//    private fun update(transform: FriendUiModel.() -> FriendUiModel) {
//        _uiState.update { state -> state.copy(model = state.model.transform()) }
//        }
        companion object {
            private const val MAX_NAME_LENGTH = 20
            private const val MAX_MEMO_LENGTH = 200
        }
    }
