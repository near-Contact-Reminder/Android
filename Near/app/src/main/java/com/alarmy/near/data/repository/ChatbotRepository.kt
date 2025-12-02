package com.alarmy.near.data.repository

import com.alarmy.near.presentation.feature.chatbotrecord.model.ChatbotRecordUIModel
import kotlinx.coroutines.flow.Flow

interface ChatbotRepository {
    fun getAllChatbotRecords(): Flow<List<ChatbotRecordUIModel>>
}
