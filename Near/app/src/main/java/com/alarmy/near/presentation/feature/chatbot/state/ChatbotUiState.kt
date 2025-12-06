package com.alarmy.near.presentation.feature.chatbot.state

import com.alarmy.near.presentation.feature.chatbotrecord.model.ChatbotRecordUIModel

sealed class ChatbotScreenState {
    data object Loading : ChatbotScreenState()

    data object Empty : ChatbotScreenState()

    data class Success(
        val records: List<ChatbotRecordUIModel>,
    ) : ChatbotScreenState()

    data class Error(
        val throwable: Throwable,
    ) : ChatbotScreenState()
}

data class ChatbotUiState(
    val screenState: ChatbotScreenState = ChatbotScreenState.Loading,
    val inputText: String = "",
    val isSending: Boolean = false,
)
