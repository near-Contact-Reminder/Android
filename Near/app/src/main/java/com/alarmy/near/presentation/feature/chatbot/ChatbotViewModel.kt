package com.alarmy.near.presentation.feature.chatbot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alarmy.near.presentation.feature.chatbot.state.ChatbotScreenState
import com.alarmy.near.presentation.feature.chatbot.state.ChatbotUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatbotViewModel
    @Inject
    constructor() : ViewModel() {
        private val _uiState = MutableStateFlow(ChatbotUiState())
        val uiState: StateFlow<ChatbotUiState> = _uiState.asStateFlow()

        init {
            fetchChatbotRecords()
        }

        private fun fetchChatbotRecords() {
            viewModelScope.launch {
                _uiState.update { it.copy(screenState = ChatbotScreenState.Loading) }
                // TODO: records 가져오기
                // 성공 시: _uiState.update { it.copy(screenState = ChatbotScreenState.Success(records)) }
                // 빈 값: _uiState.update { it.copy(screenState = ChatbotScreenState.Empty) }
                // 실패 시: _uiState.update { it.copy(screenState = ChatbotScreenState.Error(throwable)) }
                _uiState.update { it.copy(screenState = ChatbotScreenState.Empty) }
            }
        }

        fun updateInputText(text: String) {
            _uiState.update { it.copy(inputText = text) }
        }

        fun sendMessage() {
            val message = _uiState.value.inputText
            if (message.isNotEmpty()) {
                viewModelScope.launch {
                    _uiState.update { it.copy(inputText = "", isSending = true) }
                    // TODO: 실제 메시지 전송 로직 구현
                    _uiState.update { it.copy(isSending = false) }
                }
            }
        }
    }
