package com.alarmy.near.presentation.feature.myprofile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alarmy.near.presentation.feature.myprofile.components.NearLogoutButton
import com.alarmy.near.presentation.feature.myprofile.components.NearServiceInfoRow
import com.alarmy.near.presentation.feature.myprofile.components.NearSocialLoginBadge
import com.alarmy.near.presentation.feature.myprofile.components.NearSwitch
import com.alarmy.near.presentation.feature.myprofile.model.LoginType
import com.alarmy.near.presentation.feature.myprofile.model.MyProfileInfo
import com.alarmy.near.presentation.ui.component.NearFrame
import com.alarmy.near.presentation.ui.component.appbar.NearTopAppbar
import com.alarmy.near.presentation.ui.extension.ImageLoader
import com.alarmy.near.presentation.ui.theme.NearTheme

@Composable
internal fun MyProfileRoute(
    viewModel: MyProfileViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // 에러 이벤트 처리
    LaunchedEffect(viewModel.errorEvent) {
        viewModel.errorEvent.collect { throwable ->
            throwable?.let { onShowErrorSnackBar(it) }
        }
    }

    // UI 이벤트 처리
    LaunchedEffect(viewModel.uiEvent) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is MyProfileUiEvent.NavigateBack -> {
                    onNavigateBack()
                }

                is MyProfileUiEvent.ShowError -> {
                    onShowErrorSnackBar(event.throwable)
                }

                is MyProfileUiEvent.Logout -> {
                    onNavigateToLogin()
                }
            }
        }
    }

    // 로딩 상태 처리
    if (uiState.isLoading) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator(
                color = NearTheme.colors.BLUE01_5AA2E9,
            )
        }
    } else {
        MyProfileScreen(
            uiState = uiState,
            onNavigateBack = { viewModel.onNavigateBack() },
            onLogout = { viewModel.onLogout() },
        )
    }
}

@Composable
fun MyProfileScreen(
    uiState: MyProfileUiState,
    onNavigateBack: () -> Unit = {},
    onLogout: () -> Unit = {},
) {
    NearFrame {
        // 앱바
        NearTopAppbar(
            modifier = Modifier.fillMaxWidth(),
            title = "MY",
            onClickBackButton = onNavigateBack,
        )

        Spacer(modifier = Modifier.size(16.dp))

        ImageLoader(
            uri = uiState.memberInfo.imageUrl,
            contentScale = ContentScale.Crop,
            contentDescription = "프로필 이미지",
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .align(Alignment.CenterHorizontally),
        )

        Spacer(modifier = Modifier.size(16.dp))

        // 프로필 네임 - 가운데 정렬
        Text(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
            text = uiState.memberInfo.nickname,
            style = NearTheme.typography.B1_16_BOLD,
            color = NearTheme.colors.BLACK_1A1A1A,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.size(40.dp))

        // 일반 정보 섹션
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            Text(
                text = "일반",
                style = NearTheme.typography.B1_16_BOLD,
                color = NearTheme.colors.BLACK_1A1A1A,
            )

            Spacer(modifier = Modifier.size(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "연결계정",
                    style = NearTheme.typography.B2_14_MEDIUM,
                    color = NearTheme.colors.BLACK_1A1A1A,
                )

                // 로그인 타입에 따른 뱃지
                NearSocialLoginBadge(
                    loginType = uiState.memberInfo.providerType,
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 16.dp),
                color = NearTheme.colors.GRAY03_EBEBEB,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "알림 설정",
                    style = NearTheme.typography.B2_14_MEDIUM,
                    color = NearTheme.colors.BLACK_1A1A1A,
                )

                NearSwitch { }
            }

            Spacer(modifier = Modifier.size(64.dp))

            Text(
                text = "서비스 정보",
                style = NearTheme.typography.B1_16_BOLD,
                color = NearTheme.colors.BLACK_1A1A1A,
            )

            Spacer(modifier = Modifier.size(32.dp))

            // 서비스 이용 약관
            NearServiceInfoRow(
                label = "서비스 이용 약관",
                onClick = { /* 서비스 이용 약관 클릭 처리 */ },
            )

            // 개인정보 수집 및 이용 동의서
            NearServiceInfoRow(
                label = "개인정보 수집 및 이용 동의서",
                onClick = { /* 개인정보 수집 및 이용 동의서 클릭 처리 */ },
            )

            // 개인정보 처리방침
            NearServiceInfoRow(
                label = "개인정보 처리방침",
                onClick = { /* 개인정보 처리방침 클릭 처리 */ },
                showDivider = false, // 마지막 항목이므로 구분선 제거
            )

            Spacer(modifier = Modifier.weight(1f))

            NearLogoutButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = onLogout,
            )

            Spacer(modifier = Modifier.size(24.dp))

            Text(
                text = "탈퇴하기",
                textDecoration = TextDecoration.Underline,
                style =
                    NearTheme.typography.H1_24_REGULAR.copy(
                        fontSize = 14.sp,
                        color = NearTheme.colors.GRAY01_888888,
                    ),
            )

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    NearTheme {
        MyProfileScreen(
            uiState =
                MyProfileUiState(
                    isLoading = false,
                    memberInfo =
                        MyProfileInfo(
                            nickname = "테스트유저",
                            imageUrl = null,
                            notificationAgreedAt = null,
                            providerType = LoginType.KAKAO,
                        ),
                ),
            onNavigateBack = {},
            onLogout = {},
        )
    }
}
