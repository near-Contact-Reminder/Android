package com.alarmy.near.presentation.feature.myprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alarmy.near.data.repository.MemberRepository
import com.alarmy.near.model.member.MemberInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MyProfileViewModel
    @Inject
    constructor(
        memberRepository: MemberRepository,
    ) : ViewModel() {
    // 에러 이벤트 관리
    private val _errorEvent = Channel<Throwable?>()
    val errorEvent = _errorEvent.receiveAsFlow()

    // UI 이벤트 관리
    private val _uiEvent = Channel<MyProfileUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

        // UI 상태 관리
        val uiState: StateFlow<MyProfileUiState> =
            memberRepository
                .getMyInfo()
                .catch { throwable ->
                    _errorEvent.send(throwable)
                }.map { memberInfo ->
                    MyProfileUiState(
                        isLoading = false,
                        memberInfo = memberInfo,
                        error = null,
                    )
                }.stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue =
                        MyProfileUiState(
                            isLoading = true,
                            memberInfo =
                                MemberInfo(
                                    memberId = "",
                                    username = "",
                                    nickname = "",
                                    imageUrl = null,
                                    notificationAgreedAt = null,
                                    providerType = "",
                                ),
                 ),
             )

    /**
     * 백 네비게이션 이벤트 발생
     */
    fun onNavigateBack() {
        _uiEvent.trySend(MyProfileUiEvent.NavigateBack)
    }
}

/**
 * MyProfile UI 상태
 */
data class MyProfileUiState(
    val isLoading: Boolean = false,
    val memberInfo: MemberInfo,
    val error: String? = null,
)

/**
 * MyProfile UI 이벤트
 */
sealed class MyProfileUiEvent {
    data class ShowError(
        val throwable: Throwable,
    ) : MyProfileUiEvent()

    object NavigateBack : MyProfileUiEvent()
}
