package com.alarmy.near.presentation.feature.myprofile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alarmy.near.presentation.ui.component.appbar.NearCancelTopAppBar
import com.alarmy.near.presentation.feature.myprofile.model.WithdrawReason
import com.alarmy.near.presentation.ui.component.NearFrame
import com.alarmy.near.presentation.ui.component.button.NearBasicButton
import com.alarmy.near.presentation.ui.component.button.NearLineTypeButton
import com.alarmy.near.presentation.ui.component.radiobutton.NearLargeRadioButton
import com.alarmy.near.presentation.ui.component.textfield.NearOutlinedTextField
import com.alarmy.near.presentation.ui.extension.onNoRippleClick
import com.alarmy.near.presentation.ui.theme.NearTheme
import kotlinx.coroutines.delay

@Composable
fun WithdrawRoute(
    viewModel: WithdrawViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.uiEvent) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is WithdrawUiEvent.NavigateBack -> {
                    onNavigateBack()
                }
                is WithdrawUiEvent.NavigateToLogin -> {
                    onNavigateToLogin()
                }
            }
        }
    }

    WithdrawScreen(
        uiState = uiState,
        onSelectReason = viewModel::selectReason,
        onUpdateOtherReasonText = viewModel::updateOtherReasonText,
        onSubmitWithdrawRequest = viewModel::submitWithdrawRequest,
        onNavigateBack = viewModel::onNavigateBack,
    )
}

@Composable
fun WithdrawScreen(
    uiState: WithdrawUiState,
    onSelectReason: (WithdrawReason) -> Unit,
    onUpdateOtherReasonText: (String) -> Unit,
    onSubmitWithdrawRequest: () -> Unit,
    onNavigateBack: () -> Unit,
) {
    // 4개의 탈퇴 사유 리스트 생성
    val withdrawReasons = remember { WithdrawReason.entries }
    val textFieldFocusRequester = remember { FocusRequester() }

    // 기타 사유 선택 시 먼저 키보드를 올리고, 키보드가 완전히 올라온 후에 에러 상태 생성
    LaunchedEffect(uiState.isOtherReasonSelected) {
        if (uiState.isOtherReasonSelected) {
            textFieldFocusRequester.requestFocus()
            delay(300)
            onUpdateOtherReasonText("")
        }
    }

    NearFrame(
        modifier =
            Modifier
                .fillMaxSize()
                .background(NearTheme.colors.WHITE_FFFFFF)
                .padding(horizontal = 24.dp),
    ) {
        NearCancelTopAppBar(
            title = "탈퇴하기",
            onCancelClick = onNavigateBack,
        )

        Spacer(modifier = Modifier.size(48.dp))

        Text(
            text = "${uiState.nickname}님,\n떠나는 이유를 알려주시면\n큰 도움이 될 거예요.",
            style = NearTheme.typography.H1_24_MEDIUM,
        )

        Spacer(modifier = Modifier.size(12.dp))

        Text(
            text = "소중한 의견을 받아\n더 나은 서비스를 만들어갈게요.",
            style =
                NearTheme.typography.B1_16_MEDIUM.copy(
                    color = NearTheme.colors.GRAY01_888888,
                ),
        )

        Spacer(modifier = Modifier.size(48.dp))

        // 탈퇴 사유 버튼들을 표시
        withdrawReasons.forEach { reason ->
            WithdrawReasonButtonAndLabel(
                label = reason.displayText,
                isSelected = uiState.selectedReason == reason,
                onClick = {
                    onSelectReason(reason)
                },
            )

            // 마지막 항목이 아닌 경우에만 Spacer 추가
            if (reason != withdrawReasons.last()) {
                Spacer(modifier = Modifier.size(32.dp))
            }
        }

        Spacer(modifier = Modifier.size(16.dp))

        NearOutlinedTextField(
            value = uiState.otherReasonText,
            onValueChange = onUpdateOtherReasonText,
            placeholder = "편하게 의견을 남겨주세요.",
            enabled = uiState.isOtherReasonTextFieldEnabled,
            isError = !uiState.isOtherReasonTextValid,
            focusRequester = textFieldFocusRequester,
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            NearBasicButton(
                modifier = Modifier.weight(1f),
                onClick = onNavigateBack,
                contentPadding = PaddingValues(16.dp),
            ) {
                Text(
                    text = "그만두기",
                    style = NearTheme.typography.B1_16_BOLD,
                )
            }

            Spacer(modifier = Modifier.size(7.dp))

            // 탈퇴하기 버튼
            NearLineTypeButton(
                modifier = Modifier.weight(1f),
                enabled = uiState.isWithdrawButtonEnabled,
                onClick = {
                    onSubmitWithdrawRequest()
                },
                text = "탈퇴하기",
                contentPadding = PaddingValues(vertical = 16.dp),
            )
        }
        Spacer(modifier = Modifier.size(24.dp))
    }
}

@Composable
private fun WithdrawReasonButtonAndLabel(
    modifier: Modifier = Modifier,
    label: String,
    isSelected: Boolean = false,
    onClick: () -> Unit = { },
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .onNoRippleClick(onClick = onClick),
    ) {
        NearLargeRadioButton(
            selected = isSelected,
            onClick = { newState ->
                if (newState) {
                    onClick()
                }
            },
        )

        Spacer(modifier = Modifier.size(8.dp))

        Text(
            text = label,
            style = NearTheme.typography.B1_16_MEDIUM,
        )
    }
}

@Preview
@Composable
fun WithdrawScreenPreview() {
    NearTheme {
        WithdrawScreen(
            uiState = WithdrawUiState(),
            onSelectReason = {},
            onUpdateOtherReasonText = {},
            onSubmitWithdrawRequest = {},
            onNavigateBack = {},
        )
    }
}
