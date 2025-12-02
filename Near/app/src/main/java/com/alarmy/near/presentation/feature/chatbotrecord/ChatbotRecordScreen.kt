package com.alarmy.near.presentation.feature.chatbotrecord

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.alarmy.near.presentation.feature.chatbotrecord.components.ChatbotRecordAppbar
import com.alarmy.near.presentation.ui.component.NearFrame
import com.alarmy.near.presentation.ui.theme.NearTheme

@Composable
fun ChatbotRecordRoute(
    onShowErrorSnackBar: (Throwable?) -> Unit,
    onNavigateBack: () -> Unit,
) {
    ChatbotRecordScreen(
        onNavigateBack = { onNavigateBack() },
    )
}

@Composable
fun ChatbotRecordScreen(onNavigateBack: () -> Unit = {}) {
    NearFrame {
        ChatbotRecordAppbar(
            onNavigateBack = { onNavigateBack() },
            onRecordClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ChatbotRecordScreenPreview() {
    NearTheme {
        ChatbotRecordScreen()
    }
}
