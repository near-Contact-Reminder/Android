package com.alarmy.near.presentation.feature.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.alarmy.near.R
import com.alarmy.near.presentation.feature.onboarding.components.BackgroundArea
import com.alarmy.near.presentation.feature.onboarding.components.OnboardingButton
import com.alarmy.near.presentation.feature.onboarding.components.PageIndicator
import com.alarmy.near.presentation.feature.onboarding.model.OnboardingPage
import com.alarmy.near.presentation.ui.theme.NearTheme
import kotlinx.coroutines.launch

/**
 * 온보딩 화면 메인 컴포넌트
 * 5페이지로 구성된 뷰페이저 형태의 온보딩 화면
 */
@Composable
fun OnboardingScreen(
    onNavigateToLogin: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel(),
) {
    // 온보딩 페이지 데이터
    val pages =
        listOf(
            OnboardingPage(
                title = stringResource(R.string.first_onboarding_title),
                image = R.drawable.img_onboarding_page_first,
            ),
            OnboardingPage(
                title = stringResource(R.string.second_onboarding_title),
                image = R.drawable.img_onboarding_page_second,
            ),
            OnboardingPage(
                title = stringResource(R.string.third_onboarding_title),
                image = R.drawable.img_onboarding_page_third,
            ),
            OnboardingPage(
                title = stringResource(R.string.fourth_onboarding_title),
                image = R.drawable.img_onboarding_page_forth,
            ),
            OnboardingPage(
                title = stringResource(R.string.fifth_onboarding_title),
                image = R.drawable.img_onboarding_page_fifth,
            ),
        )

    // 상태바와 네비게이션 바 높이 계산
    val density = LocalDensity.current
    val statusBarHeightDp = with(density) { WindowInsets.statusBars.getTop(density).toDp() }
    val navigationBarHeightDp = with(density) { WindowInsets.navigationBars.getBottom(density).toDp() }

    // 페이저 상태 관리
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        BackgroundArea()
        Column(
            modifier = Modifier
                .padding(top = statusBarHeightDp, bottom = navigationBarHeightDp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
        // 뷰페이저
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f),
        ) { page ->
            OnboardingPageContent(
                page = pages[page],
                modifier = Modifier.fillMaxSize(),
            )
        }

        Spacer(modifier = Modifier.size(25.dp))

        // 페이지 인디케이터
        PageIndicator(
            pageCount = pages.size,
            currentPage = pagerState.currentPage,
        )

        Spacer(modifier = Modifier.size(32.dp))

        // 다음/완료 버튼
        OnboardingButton(
            currentPage = pagerState.currentPage,
            totalPages = pages.size,
            onNextClick = {
                if (pagerState.currentPage < pages.size - 1) {
                    scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                } else {
                    scope.launch {
                        viewModel.completeOnboarding()
                        onNavigateToLogin()
                    }
                }
            },
        )
        Spacer(modifier = Modifier.size(24.dp))
        }
    }
}

/**
 * 온보딩 페이지 콘텐츠 컴포넌트
 * 각 페이지의 제목과 설명을 표시
 */
@Composable
private fun OnboardingPageContent(
    page: OnboardingPage,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.size(45.dp))

        // 각 온보딩 페이지 타이틀
        Text(
            text = createAnnotatedText(page.title),
            textAlign = TextAlign.Center,
            style =
                NearTheme.typography.H1_24_BOLD.copy(
                    fontSize = 20.sp,
                    lineHeight = 30.sp,
                ),
        )

        Spacer(modifier = Modifier.size(24.dp))

        Image(
            modifier = Modifier.weight(1f),
            painter = painterResource(page.image),
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )

    }
}

/**
 * \n 이후 텍스트에 다른 색상을 적용하는 AnnotatedString 생성
 * 현 페이지에서 \n 이후 텍스트 색상이 다른 규칙이 있습니다.
 */
@Composable
private fun createAnnotatedText(
    text: String,
    defaultColor: Color = NearTheme.colors.BLACK_1A1A1A,
    highlightColor: Color = NearTheme.colors.BLUE01_5AA2E9,
): AnnotatedString =
    buildAnnotatedString {
        val newLineIndex = text.indexOf("\n")

        if (newLineIndex != -1) {
            // \n 이전 텍스트 (기본 색상)
            appendStyledText(text.substring(0, newLineIndex), defaultColor)
            // \n 이후 텍스트 (강조 색상)
            appendStyledText(text.substring(newLineIndex), highlightColor)
        } else {
            // 텍스트에 \n가 없는 경우
            appendStyledText(text, defaultColor)
        }
    }

/**
 * 지정된 색상으로 텍스트를 추가하는 함수
 */
private fun AnnotatedString.Builder.appendStyledText(text: String, color: Color) {
    withStyle(style = SpanStyle(color = color)) {
        append(text)
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingScreenPreview() {
    NearTheme {
        OnboardingScreen(
            onNavigateToLogin = {},
        )
    }
}
