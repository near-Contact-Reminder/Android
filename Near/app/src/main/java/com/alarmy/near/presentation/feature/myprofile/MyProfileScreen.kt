package com.alarmy.near.presentation.feature.myprofile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alarmy.near.R
import com.alarmy.near.presentation.feature.myprofile.components.NearLogoutButton
import com.alarmy.near.presentation.feature.myprofile.components.NearServiceInfoRow
import com.alarmy.near.presentation.feature.myprofile.components.NearSocialLoginBadge
import com.alarmy.near.presentation.feature.myprofile.components.NearSwitch
import com.alarmy.near.presentation.feature.myprofile.model.LoginType
import com.alarmy.near.presentation.feature.myprofile.model.MyProfileInfoUIModel
import com.alarmy.near.presentation.feature.myprofile.model.TermsType
import com.alarmy.near.presentation.ui.component.NearFrame
import com.alarmy.near.presentation.ui.component.appbar.NearTopAppbar
import com.alarmy.near.presentation.ui.extension.ImageLoader
import com.alarmy.near.presentation.ui.extension.onNoRippleClick
import com.alarmy.near.presentation.ui.theme.NearTheme

@Composable
internal fun MyProfileRoute(
    viewModel: MyProfileViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToWithdraw: (nickname: String) -> Unit,
    onNavigateToTerms: (title: String, url: String) -> Unit,
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val termsDetailFormat = stringResource(R.string.my_profile_terms_detail)

    val termsTitles =
        mapOf(
            TermsType.SERVICE_AGREED_TERMS to stringResource(TermsType.SERVICE_AGREED_TERMS.titleRes),
            TermsType.PERSONAL_INFO_TERMS to stringResource(TermsType.PERSONAL_INFO_TERMS.titleRes),
            TermsType.PRIVACY_POLICY_TERMS to stringResource(TermsType.PRIVACY_POLICY_TERMS.titleRes),
        )

    // 에러 이벤트 처리
    LaunchedEffect(viewModel.errorEvent) {
        viewModel.errorEvent.collect { throwable ->
            onShowErrorSnackBar(throwable)
        }
    }

    // UI 이벤트 처리
    LaunchedEffect(viewModel.uiEvent) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is MyProfileUiEvent.NavigateBack -> {
                    onNavigateBack()
                }

                is MyProfileUiEvent.Logout -> {
                    onNavigateToLogin()
                }

                is MyProfileUiEvent.NavigateToWithdraw -> {
                    onNavigateToWithdraw(event.nickname)
                }

                is MyProfileUiEvent.NavigateToTerms -> {
                    val title = termsTitles[event.termsType] ?: ""
                    onNavigateToTerms(termsDetailFormat.format(title), event.termsType.url)
                }
            }
        }
    }

    // 로딩 상태 처리
    if (uiState.isLoading) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(NearTheme.colors.WHITE_FFFFFF),
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
            onWithdraw = { viewModel.onWithdraw() },
            onTermsClick = { termsType -> viewModel.onTermsClick(termsType) },
        )
    }
}

@Composable
fun MyProfileScreen(
    uiState: MyProfileUiState,
    onNavigateBack: () -> Unit = {},
    onLogout: () -> Unit = {},
    onWithdraw: () -> Unit = {},
    onTermsClick: (TermsType) -> Unit = {},
) {
    NearFrame {
        // 앱바
        NearTopAppbar(
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(R.string.my_profile_title),
            onClickBackButton = onNavigateBack,
        )

        MyProfileInfoSection(uiState)

        // 일반 정보 섹션
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
        ) {
            MyProfileGeneralSection(uiState)
            MyProfileServiceInfoSection(onLogout, onWithdraw, onTermsClick)
        }
    }
}

@Composable
private fun ColumnScope.MyProfileInfoSection(uiState: MyProfileUiState) {
    Spacer(modifier = Modifier.size(16.dp))

    ImageLoader(
        uri = uiState.memberInfo.imageUrl,
        contentScale = ContentScale.Crop,
        contentDescription = stringResource(R.string.my_profile_image_description),
        modifier =
            Modifier
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
}

@Composable
private fun MyProfileGeneralSection(uiState: MyProfileUiState) {
    Text(
        text = stringResource(R.string.my_profile_general_section),
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
            text = stringResource(R.string.my_profile_connected_account),
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
            text = stringResource(R.string.my_profile_notification_settings),
            style = NearTheme.typography.B2_14_MEDIUM,
            color = NearTheme.colors.BLACK_1A1A1A,
        )

        NearSwitch { }
    }

    Spacer(modifier = Modifier.size(64.dp))
}

@Composable
private fun ColumnScope.MyProfileServiceInfoSection(
    onLogout: () -> Unit,
    onWithdraw: () -> Unit,
    onTermsClick: (TermsType) -> Unit,
) {
    Text(
        text = stringResource(R.string.my_profile_service_info_section),
        style = NearTheme.typography.B1_16_BOLD,
        color = NearTheme.colors.BLACK_1A1A1A,
    )

    Spacer(modifier = Modifier.size(32.dp))

    // 약관 및 정책 목록
    TermsType.entries.forEachIndexed { index, termsType ->
        NearServiceInfoRow(
            label = stringResource(termsType.titleRes),
            onClick = { onTermsClick(termsType) },
            showDivider = index < TermsType.entries.size - 1,
        )
    }

    Spacer(modifier = Modifier.weight(1f))

    NearLogoutButton(
        modifier = Modifier.fillMaxWidth(),
        onClick = onLogout,
    )

    Spacer(modifier = Modifier.size(24.dp))

    Text(
        modifier = Modifier.onNoRippleClick(onClick = onWithdraw),
        text = stringResource(R.string.my_profile_withdraw),
        textDecoration = TextDecoration.Underline,
        style =
            NearTheme.typography.H1_24_REGULAR.copy(
                fontSize = 14.sp,
                color = NearTheme.colors.GRAY01_888888,
            ),
    )

    Spacer(modifier = Modifier.weight(1f))
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
                        MyProfileInfoUIModel(
                            nickname = "테스트유저",
                            imageUrl = null,
                            notificationAgreedAt = null,
                            providerType = LoginType.KAKAO,
                        ),
                ),
            onNavigateBack = {},
            onLogout = {},
            onWithdraw = {},
        )
    }
}
