package com.alarmy.near.presentation.feature.chatbotrecord.state

import com.alarmy.near.presentation.feature.chatbotrecord.model.ChatbotRecordUIModel

sealed class ChatbotRecordUiState {
    object Loading : ChatbotRecordUiState()

    data class Success(
        val records: List<ChatbotRecordUIModel>,
    ) : ChatbotRecordUiState()

    data class Error(
        val throwable: Throwable,
    ) : ChatbotRecordUiState()
}
