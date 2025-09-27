package com.alarmy.near.presentation.feature.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.alarmy.near.R
import com.alarmy.near.model.ProviderType
import com.alarmy.near.presentation.feature.login.model.TermType
import com.alarmy.near.presentation.ui.theme.NearTheme

@Composable
internal fun LoginRoute(
    onNavigateToHome: () -> Unit,
    onNavigateToWebView: (title: String, url: String) -> Unit,
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val showPrivacyBottomSheet by viewModel.showPrivacyBottomSheet.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current

    // 약관 제목을 미리 가져옴
    val termsTitles =
        mapOf(
            TermType.SERVICE_TERMS to stringResource(TermType.SERVICE_TERMS.titleRes),
            TermType.PRIVACY_COLLECTION to stringResource(TermType.PRIVACY_COLLECTION.titleRes),
            TermType.PRIVACY_POLICY to stringResource(TermType.PRIVACY_POLICY.titleRes),
        )

    // 웹뷰에서 돌아올 때 바텀시트 복원
    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            viewModel.restoreBottomSheetIfNeeded()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is LoginEvent.NavigateToHome -> {
                    onNavigateToHome()
                }

                is LoginEvent.ShowTermsDetail -> {
                    val title = termsTitles[event.termType] ?: ""
                    onNavigateToWebView(title, event.termType.url)
                }

                is LoginEvent.ShowError -> {
                    onShowErrorSnackBar(event.throwable)
                }
            }
        }
    }

    LoginScreen(
        uiState = uiState,
        onLoginClick = { providerType ->
            viewModel.performLogin(providerType)
        },
    )

    // 개인정보 동의 바텀시트
    PrivacyConsentBottomSheet(
        isVisible = showPrivacyBottomSheet,
        onDismiss = {
            viewModel.dismissPrivacyBottomSheet()
        },
        onConsentComplete = {
            viewModel.onPrivacyConsentComplete()
        },
        viewModel = viewModel,
    )
}

@Composable
fun LoginScreen(
    uiState: LoginUiState,
    onLoginClick: (ProviderType) -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(NearTheme.colors.WHITE_FFFFFF)
                .systemBarsPadding(),
    ) {
        LoginIntroductionSection(modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.weight(1f))

        // 소셜 로그인 버튼
        SocialLoginButtons(
            isLoading = uiState.isLoading,
            onLoginClick = onLoginClick,
        )
    }
}

@Composable
private fun LoginIntroductionSection(modifier: Modifier = Modifier) {
    Spacer(modifier = Modifier.height(LoginScreenConstants.TOP_SPACING.dp))

    Image(
        modifier = modifier.size(LoginScreenConstants.LOGO_SIZE.dp),
        alignment = Alignment.Center,
        painter = painterResource(R.drawable.img_40_character),
        contentDescription = stringResource(R.string.near_logo),
    )

    Image(
        modifier = modifier.wrapContentSize(Alignment.Center),
        alignment = Alignment.Center,
        painter = painterResource(R.drawable.ic_near_logo_title_primary),
        contentDescription = stringResource(R.string.near_logo_title),
    )

    Spacer(modifier = Modifier.size(LoginScreenConstants.DESCRIPTION_SPACING.dp))

    Text(
        modifier = modifier.wrapContentSize(Alignment.Center),
        text = stringResource(R.string.login_near_description),
        style = NearTheme.typography.B1_16_MEDIUM,
        color = NearTheme.colors.GRAY01_888888,
    )
}

@Composable
private fun ColumnScope.SocialLoginButtons(
    isLoading: Boolean,
    onLoginClick: (ProviderType) -> Unit,
) {
    // 카카오 로그인 버튼
    SocialLoginButton(
        isEnabled = !isLoading,
        providerType = ProviderType.KAKAO,
        buttonResource = R.drawable.btn_kakao_login,
        contentDescription = stringResource(R.string.login_kakao_login_button_text),
        onLoginClick = onLoginClick,
    )

    Spacer(modifier = Modifier.size(LoginScreenConstants.BOTTOM_SPACING.dp))
}

@Composable
private fun ColumnScope.SocialLoginButton(
    isEnabled: Boolean,
    providerType: ProviderType,
    buttonResource: Int,
    contentDescription: String,
    onLoginClick: (ProviderType) -> Unit,
) {
    Image(
        modifier =
            Modifier
                .wrapContentSize()
                .align(Alignment.CenterHorizontally)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    enabled = isEnabled,
                ) {
                    onLoginClick(providerType)
                },
        painter = painterResource(buttonResource),
        contentDescription = contentDescription,
    )
}

private object LoginScreenConstants {
    const val TOP_SPACING = 170
    const val LOGO_SIZE = 160
    const val DESCRIPTION_SPACING = 12
    const val BOTTOM_SPACING = 96
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    NearTheme {
        LoginScreen(
            uiState = LoginUiState(),
            onLoginClick = { },
        )
    }
}
