package com.alarmy.near.presentation.feature.chatbot

import androidx.lifecycle.ViewModel
import com.alarmy.near.presentation.feature.chatbot.state.ChatbotUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ChatbotViewModel
    @Inject
    constructor() : ViewModel() {
        private val _uiState = MutableStateFlow<ChatbotUiState>(ChatbotUiState.Loading)
        val uiState: StateFlow<ChatbotUiState> = _uiState.asStateFlow()

        private val _inputText = MutableStateFlow("")
        val inputText: StateFlow<String> = _inputText.asStateFlow()

        fun updateInputText(text: String) {
            _inputText.value = text
        }

        fun sendMessage() {
            val message = _inputText.value
            if (message.isNotEmpty()) {
                // TODO: 실제 메시지 전송 로직 구현
                _inputText.value = ""
            }
        }
    }
