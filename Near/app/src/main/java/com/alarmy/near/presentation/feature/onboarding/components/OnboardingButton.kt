package com.alarmy.near.presentation.feature.onboarding.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alarmy.near.R
import com.alarmy.near.presentation.ui.component.button.NearBasicButton
import com.alarmy.near.presentation.ui.theme.NearTheme

/**
 * 온보딩 화면용 버튼 컴포넌트
 */
@Composable
fun OnboardingButton(
    currentPage: Int,
    totalPages: Int,
    isLoading: Boolean = false,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isLastPage = currentPage == totalPages - 1
    val buttonText = if (isLastPage) stringResource(R.string.onboarding_auth_button_text) else stringResource(R.string.onboarding_next_button_text)

    NearBasicButton(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
        onClick = onNextClick,
        enabled = !isLoading,
        contentPadding = PaddingValues(vertical = 17.dp),
    ) {
        Text(
            text = buttonText,
            style =
                NearTheme.typography.B1_16_BOLD,
        )
    }
}

@Preview
@Composable
fun OnboardingButtonPreview() {
    NearTheme {
        OnboardingButton(currentPage = 0, totalPages = 5, onNextClick = {})
    }
}

@Preview
@Composable
fun OnboardingButtonLastPreview() {
    NearTheme {
        OnboardingButton(currentPage = 4, totalPages = 5, onNextClick = {})
    }
}

