package com.alarmy.near.presentation.feature.login

import androidx.compose.foundation.Image
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alarmy.near.R
import com.alarmy.near.presentation.ui.theme.NearTheme
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient

@Composable
internal fun LoginRoute(
    onNavigateToHome: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loginSuccessEvent.collect {
            onNavigateToHome()
        }
    }

    /**
     * 카카오 로그인 처리 함수
     */
    fun handleKakaoLogin() {
        // 카카오톡으로 로그인 가능 여부 확인
        val isKakaoTalkAvailable = UserApiClient.instance.isKakaoTalkLoginAvailable(context)

        if (isKakaoTalkAvailable) {
            // 카카오톡 앱이 설치되어 있으면 카카오톡으로 로그인
            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                handleKakaoLoginResult(token, error, "카카오톡 앱", viewModel, context)
            }
        } else {
            // 카카오톡 앱이 없으면 카카오계정으로 웹 로그인
            UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
                handleKakaoLoginResult(token, error, "카카오계정 웹", viewModel, context)
            }
        }
    }

    LoginScreen(
        uiState = uiState,
        performKakaoLogin = ::handleKakaoLogin,
    )
}

/**
 * 카카오 로그인 결과 처리
 */
private fun handleKakaoLoginResult(
    token: OAuthToken?,
    error: Throwable?,
    loginMethod: String,
    viewModel: LoginViewModel,
    context: android.content.Context,
) {
    when {
        error != null -> {
            if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                return
            }

            // 카카오톡 앱 로그인 실패 시 카카오계정 웹으로 재시도
            if (loginMethod.contains("카카오톡 앱")) {
                UserApiClient.instance.loginWithKakaoAccount(context) { retryToken, retryError ->
                    handleKakaoLoginResult(retryToken, retryError, "카카오계정 웹 (재시도)", viewModel, context)
                }
            }
        }

        token != null -> {
            // ViewModel에 토큰 전달
            viewModel.performKakaoLogin(token.accessToken)
        }
    }
}

@Composable
fun LoginScreen(
    uiState: LoginUiState,
    performKakaoLogin: () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .systemBarsPadding(),
    ) {
        LoginIntroductionSection(modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.weight(1f))

        KakaoLoginButton(
            enable = uiState.isLoading,
            onLoginClick = performKakaoLogin,
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
        painter = painterResource(R.drawable.ic_near_logo_title),
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
private fun ColumnScope.KakaoLoginButton(
    enable: Boolean,
    onLoginClick: () -> Unit,
) {
    Image(
        modifier =
            Modifier
                .wrapContentSize()
                .align(Alignment.CenterHorizontally)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    enabled = !enable,
                ) {
                    onLoginClick()
                },
        painter = painterResource(R.drawable.btn_kakao_login),
        contentDescription = stringResource(R.string.login_kakao_login_button_text),
    )

    Spacer(modifier = Modifier.size(LoginScreenConstants.BOTTOM_SPACING.dp))
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
            performKakaoLogin = {},
        )
    }
}
