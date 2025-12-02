package com.alarmy.near.data.mapper

import com.alarmy.near.model.chatbot.ChatbotRecord
import com.alarmy.near.presentation.feature.chatbotrecord.model.ChatbotRecordUIModel

fun ChatbotRecord.toModel(): ChatbotRecordUIModel =
    ChatbotRecordUIModel(
        id = id,
        title = title,
        date = date,
    )
