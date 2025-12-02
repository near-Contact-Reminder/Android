package com.alarmy.near.presentation.feature.chatbotrecord

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alarmy.near.presentation.feature.chatbotrecord.components.ChatbotRecordAppbar
import com.alarmy.near.presentation.feature.chatbotrecord.components.ChatbotRecordItem
import com.alarmy.near.presentation.feature.chatbotrecord.model.ChatbotRecordUIModel
import com.alarmy.near.presentation.feature.chatbotrecord.state.ChatbotRecordUiState
import com.alarmy.near.presentation.ui.component.NearFrame
import com.alarmy.near.presentation.ui.theme.NearTheme

@Composable
fun ChatbotRecordRoute(
    viewModel: ChatbotRecordViewModel = hiltViewModel(),
    onShowErrorSnackBar: (Throwable?) -> Unit,
    onNavigateBack: () -> Unit,
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    when (uiState.value) {
        is ChatbotRecordUiState.Error -> {
            val exception = (uiState.value as ChatbotRecordUiState.Error).throwable
            onShowErrorSnackBar(exception)
        }

        ChatbotRecordUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
            ) {
                CircularProgressIndicator(
                    color = NearTheme.colors.BLUE01_5AA2E9,
                )
            }
        }

        is ChatbotRecordUiState.Success -> {
            ChatbotRecordScreen(
                records = (uiState.value as ChatbotRecordUiState.Success).records,
                onNavigateBack = { onNavigateBack() },
            )
        }
    }
}

@Composable
fun ChatbotRecordScreen(
    records: List<ChatbotRecordUIModel>,
    onNavigateBack: () -> Unit = {},
) {
    NearFrame {
        ChatbotRecordAppbar(
            onNavigateBack = { onNavigateBack() },
            onRecordClick = {},
        )

        LazyColumn(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(
                items = records,
                key = { it.id },
            ) { record ->
                ChatbotRecordItem(
                    record = record,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatbotRecordScreenPreview() {
    NearTheme {
        ChatbotRecordScreen(
            records =
                listOf(
                    ChatbotRecordUIModel("1", "Record 1", "2023.06.01"),
                    ChatbotRecordUIModel("2", "Record 2", "2023.06.02"),
                    ChatbotRecordUIModel("3", "Record 3", "2023.06.03"),
                    ChatbotRecordUIModel("4", "Record 4", "2023.06.04"),
                    ChatbotRecordUIModel("5", "Record 5", "2023.06.05"),
                    ChatbotRecordUIModel("6", "Record 6", "2023.06.06"),
                    ChatbotRecordUIModel("7", "Record 6", "2023.06.06"),
                    ChatbotRecordUIModel("8", "Record 6", "2023.06.06"),
                    ChatbotRecordUIModel("9", "Record 6", "2023.06.06"),
                    ChatbotRecordUIModel("10", "Record 6", "2023.06.06"),
                    ChatbotRecordUIModel("11", "Record 6", "2023.06.06"),
                ),
        )
    }
}
