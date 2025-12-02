package com.alarmy.near.data.repository

import com.alarmy.near.data.mapper.toModel
import com.alarmy.near.model.chatbot.ChatbotRecord
import com.alarmy.near.presentation.feature.chatbotrecord.model.ChatbotRecordUIModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class ChatbotRepositoryImpl
    @Inject
    constructor() : ChatbotRepository {
        override fun getAllChatbotRecords(): Flow<List<ChatbotRecordUIModel>> {
            val chatbotRecords =
                listOf(
                    ChatbotRecord("1", "Record 1", "2023.06.01"),
                    ChatbotRecord("2", "Record 2", "2023.06.02"),
                    ChatbotRecord("3", "Record 3", "2023.06.03"),
                    ChatbotRecord("4", "Record 4", "2023.06.04"),
                    ChatbotRecord("5", "Record 5", "2023.06.05"),
                    ChatbotRecord("6", "Record 6", "2023.06.06"),
                    ChatbotRecord("7", "Record 6", "2023.06.06"),
                    ChatbotRecord("8", "Record 6", "2023.06.06"),
                    ChatbotRecord("9", "Record 6", "2023.06.06"),
                    ChatbotRecord("10", "Record 6", "2023.06.06"),
                    ChatbotRecord("11", "Record 6", "2023.06.06"),
                    ChatbotRecord("12", "Record 6", "2023.06.06"),
                    ChatbotRecord("13", "Record 6", "2023.06.06"),
                    ChatbotRecord("14", "Record 6", "2023.06.06"),
                )
            return flowOf(chatbotRecords.map { it.toModel() })
        }
    }
