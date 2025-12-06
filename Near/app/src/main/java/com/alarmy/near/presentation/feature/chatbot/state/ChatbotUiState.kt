package com.alarmy.near.presentation.feature.chatbot.state

import com.alarmy.near.presentation.feature.chatbotrecord.model.ChatbotRecordUIModel

sealed class ChatbotUiState {
    object Loading : ChatbotUiState()

    data class Success(
        val records: List<ChatbotRecordUIModel>,
    ) : ChatbotUiState()

    data class Error(
        val throwable: Throwable,
    ) : ChatbotUiState()
}
