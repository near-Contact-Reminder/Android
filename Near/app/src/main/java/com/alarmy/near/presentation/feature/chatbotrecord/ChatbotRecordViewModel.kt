package com.alarmy.near.presentation.feature.chatbotrecord

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alarmy.near.data.repository.ChatbotRepository
import com.alarmy.near.presentation.feature.chatbotrecord.state.ChatbotRecordUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ChatbotRecordViewModel
    @Inject
    constructor(
        private val chatbotRepository: ChatbotRepository,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow<ChatbotRecordUiState>(ChatbotRecordUiState.Loading)
        val uiState: StateFlow<ChatbotRecordUiState> = _uiState.asStateFlow()

        init {
            getAllChatbotRecords()
        }

        fun getAllChatbotRecords() {
            chatbotRepository
                .getAllChatbotRecords()
                .onStart {
                    _uiState.update { ChatbotRecordUiState.Loading }
                }.onEach { chatbotRecords ->
                    if (chatbotRecords.isEmpty()) {
                        _uiState.update { ChatbotRecordUiState.Empty }
                        return@onEach
                    }
                    _uiState.update { ChatbotRecordUiState.Success(chatbotRecords) }
                }.catch { exception ->
                    _uiState.update { ChatbotRecordUiState.Error(exception) }
                }.launchIn(viewModelScope)
        }
    }
