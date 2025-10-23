package com.alarmy.near.presentation.feature.login

import android.content.Context
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ApiError
import com.kakao.sdk.common.model.AuthError
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

@Composable
internal fun LoginRoute(
    onNavigateToFriendContactCycle: () -> Unit,
    onNavigateToWebView: (title: String, url: String) -> Unit,
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val showPrivacyBottomSheet by viewModel.showPrivacyBottomSheet.collectAsStateWithLifecycle()
    val termsAgreementState by viewModel.termsAgreementState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

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
                    onNavigateToFriendContactCycle()
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
            when (providerType) {
                ProviderType.KAKAO -> {
                    scope.launch {
                        performKakaoLogin(
                            context = context,
                            onSuccess = { accessToken ->
                                viewModel.onSocialLoginSuccess(accessToken, ProviderType.KAKAO)
                            },
                            onFailure = { exception ->
                                viewModel.onSocialLoginFailure(exception)
                            },
                        )
                    }
                }

                // 다른 로그인 타입이 추가된 경우 ProviderType 수정
            }
        },
    )

    // 개인정보 동의 바텀시트
    PrivacyConsentBottomSheet(
        isVisible = showPrivacyBottomSheet,
        termsAgreementState = termsAgreementState,
        onDismiss = {
            viewModel.dismissPrivacyBottomSheet()
        },
        onConsentComplete = {
            viewModel.onPrivacyConsentComplete()
        },
        onToggleAllTerms = {
            viewModel.toggleAllTermsAgreement()
        },
        onToggleIndividualTerms = { termType ->
            viewModel.toggleIndividualTermsAgreement(termType)
        },
        onTermsClick = { termType ->
            viewModel.markNavigatedToWebView()
            val title = termsTitles[termType] ?: ""
            onNavigateToWebView(title, termType.url)
        },
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

/**
 * 카카오 로그인 수행
 */
private suspend fun performKakaoLogin(
    context: Context,
    onSuccess: (String) -> Unit,
    onFailure: (Throwable) -> Unit,
) {
    unlinkKakao()
    try {
        val accessToken =
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
                loginWithKakaoTalk(context)
            } else {
                loginWithKakaoAccount(context)
            }
        onSuccess(accessToken)
    } catch (error: AuthError) {
        // OAuth 인증 과정 에러
        onFailure(Exception(context.getString(R.string.login_auth_error)))
    } catch (error: ApiError) {
        // API 호출 에러
        onFailure(Exception(context.getString(R.string.login_api_error)))
    } catch (error: ClientError) {
        // SDK 내부 에러
        onFailure(Exception(context.getString(R.string.login_client_error)))
    } catch (error: KakaoSdkError) {
        // 카카오 SDK 에러
        onFailure(Exception(context.getString(R.string.login_sdk_error)))
    } catch (exception: Exception) {
        onFailure(exception)
    }
}

/**
 * 카카오 로그아웃
 */
private suspend fun unlinkKakao(): Unit =
    suspendCancellableCoroutine { continuation ->
        UserApiClient.instance.unlink { error ->
            continuation.resume(Unit)
        }
    }

/**
 * 카카오톡으로 로그인
 */
private suspend fun loginWithKakaoTalk(context: Context): String =
    suspendCancellableCoroutine { continuation ->
        UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
            handleKakaoLoginResult(
                token,
                error,
                context.getString(R.string.login_user_cancelled),
                continuation,
            )
        }
    }

/**
 * 카카오 계정으로 로그인
 */
private suspend fun loginWithKakaoAccount(context: Context): String =
    suspendCancellableCoroutine { continuation ->
        UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
            handleKakaoLoginResult(
                token,
                error,
                context.getString(R.string.login_user_cancelled),
                continuation,
            )
        }
    }

/**
 * 카카오 로그인 결과 처리
 */
private fun handleKakaoLoginResult(
    token: OAuthToken?,
    error: Throwable?,
    cancelledMessage: String,
    continuation: CancellableContinuation<String>,
) {
    when {
        error != null -> {
            continuation.resumeWith(Result.failure(Exception(cancelledMessage)))
        }

        token != null -> continuation.resume(token.accessToken)
        else ->
            continuation.resumeWith(
                Result.failure(Exception(cancelledMessage)),
            )
    }
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
