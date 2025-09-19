package com.alarmy.near.presentation.feature.myprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alarmy.near.data.mapper.toMyProfileInfo
import com.alarmy.near.data.repository.AuthRepository
import com.alarmy.near.data.repository.MemberRepository
import com.alarmy.near.presentation.feature.myprofile.model.LoginType
import com.alarmy.near.presentation.feature.myprofile.model.MyProfileInfo
import com.alarmy.near.presentation.feature.myprofile.model.TermsType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyProfileViewModel
    @Inject
    constructor(
        private val memberRepository: MemberRepository,
        private val authRepository: AuthRepository,
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
                        memberInfo = memberInfo.toMyProfileInfo(),
                    )
                }.stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue =
                        MyProfileUiState(
                            isLoading = true,
                            memberInfo =
                                MyProfileInfo(
                                    nickname = "",
                                    imageUrl = null,
                                    notificationAgreedAt = null,
                                    providerType = LoginType.KAKAO,
                                ),
                        ),
                )

        /**
         * 백 네비게이션 이벤트 발생
         */
        fun onNavigateBack() {
            _uiEvent.trySend(MyProfileUiEvent.NavigateBack)
        }

        /**
         * 로그아웃 이벤트 발생
         */
        fun onLogout() {
            viewModelScope.launch {
                runCatching {
                    authRepository.logout()
                }.onSuccess {
                    _uiEvent.trySend(MyProfileUiEvent.Logout)
                }.onFailure { exception ->
                    _errorEvent.send(exception)
                }
            }
        }

        /**
         * 탈퇴하기 이벤트 발생
         */
        fun onWithdraw() {
            val currentState = uiState.value
            _uiEvent.trySend(MyProfileUiEvent.NavigateToWithdraw(currentState.memberInfo.nickname))
        }

        /**
         * 약관 및 정책 클릭 이벤트 발생
         */
        fun onTermsClick(termsType: TermsType) {
            _uiEvent.trySend(MyProfileUiEvent.NavigateToTerms(termsType))
        }
    }

/**
 * MyProfile UI 상태
 */
data class MyProfileUiState(
    val isLoading: Boolean = false,
    val memberInfo: MyProfileInfo,
)

/**
 * MyProfile UI 이벤트
 */
sealed class MyProfileUiEvent {
    data class ShowError(
        val throwable: Throwable,
    ) : MyProfileUiEvent()

    object NavigateBack : MyProfileUiEvent()

    object Logout : MyProfileUiEvent()

    data class NavigateToWithdraw(
        val nickname: String,
    ) : MyProfileUiEvent()

    data class NavigateToTerms(
        val termsType: TermsType,
    ) : MyProfileUiEvent()
}
