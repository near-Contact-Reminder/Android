package com.alarmy.near.presentation.feature.chatbot

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alarmy.near.R
import com.alarmy.near.presentation.feature.chatbot.components.ChatbotAppbar
import com.alarmy.near.presentation.feature.chatbot.components.ChatbotTextField
import com.alarmy.near.presentation.feature.chatbot.state.ChatbotScreenState
import com.alarmy.near.presentation.feature.chatbot.state.ChatbotUiState
import com.alarmy.near.presentation.ui.component.NearFrame
import com.alarmy.near.presentation.ui.theme.NearTheme
import kotlinx.coroutines.launch

@Composable
fun ChatbotRoute(
    viewModel: ChatbotViewModel = hiltViewModel(),
    onShowErrorSnackBar: (Throwable?) -> Unit,
    onNavigateBack: () -> Unit,
    onChatbotRecordClick: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ChatbotScreen(
        uiState = uiState,
        onInputTextChanged = viewModel::updateInputText,
        onSendClick = viewModel::sendMessage,
        onNavigateBack = onNavigateBack,
        onChatbotRecordClick = onChatbotRecordClick,
    )
}

@Composable
fun ChatbotScreen(
    uiState: ChatbotUiState,
    onInputTextChanged: (String) -> Unit = {},
    onSendClick: () -> Unit = {},
    onNavigateBack: () -> Unit = {},
    onChatbotRecordClick: () -> Unit = {},
) {
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    // 키보드 상태 감지
    val imeHeightDp = WindowInsets.ime.asPaddingValues().calculateBottomPadding()
    val imeVisible = imeHeightDp > 0.dp
    val bottomPadding = if (imeVisible) 16.dp else 32.dp

    // 키보드가 내려가면 포커스 해제
    LaunchedEffect(imeVisible) {
        if (!imeVisible) {
            focusManager.clearFocus()
        } else {
            scope.launch {
                scrollState.animateScrollTo(scrollState.maxValue)
            }
        }
    }

    NearFrame(
        modifier =
            if (imeVisible) {
                Modifier.imePadding()
            } else {
                Modifier
            },
    ) {
        // 앱바
        ChatbotAppbar(
            onNavigateBack = { onNavigateBack() },
            onChatbotRecordClick = { onChatbotRecordClick() },
            isRecordEmpty = uiState.screenState is ChatbotScreenState.Empty,
        )

        // 스크롤 가능한 컨텐츠 영역
        Box(
            modifier =
                Modifier
                    .weight(1f)
                    .fillMaxWidth(),
        ) {
            when (val screenState = uiState.screenState) {
                is ChatbotScreenState.Loading -> {
                    // TODO: 로딩 UI
                }

                is ChatbotScreenState.Empty -> {
                    ChatbotBodyInit()
                }

                is ChatbotScreenState.Success -> {
                    // TODO: 채팅 메시지 리스트 UI
                }

                is ChatbotScreenState.Error -> {
                    // TODO: 에러 UI
                }
            }
        }

        // 텍스트필드를 하단에 배치
        ChatbotTextField(
            value = uiState.inputText,
            onValueChange = onInputTextChanged,
            onSendClick = onSendClick,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .background(NearTheme.colors.WHITE_FFFFFF)
                    .padding(horizontal = 20.dp)
                    .padding(top = 16.dp, bottom = bottomPadding),
        )
    }
}

@Composable
private fun ChatbotBodyInit() {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
    ) {
        // 앱바와의 간격 (전체 높이의 21.6% = 165/764)
        Spacer(modifier = Modifier.weight(0.216f))

        Image(
            painter = painterResource(R.drawable.img_100_character_question),
            contentDescription = null,
        )

        Spacer(modifier = Modifier.size(12.dp))

        Text(
            text = "어떤 메시지를\n보내고 싶으신가요?",
            style = NearTheme.typography.H1_24_MEDIUM,
        )

        // 아래쪽 여백
        Spacer(modifier = Modifier.weight(0.784f))
    }
}

@Preview()
@Composable
fun ChatbotScreenPreview() {
    NearTheme {
        ChatbotScreen(uiState = ChatbotUiState())
    }
}
