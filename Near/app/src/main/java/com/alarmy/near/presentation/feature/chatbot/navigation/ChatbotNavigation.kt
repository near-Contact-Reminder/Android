package com.alarmy.near.presentation.feature.chatbot.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.alarmy.near.presentation.feature.chatbot.ChatbotRoute
import kotlinx.serialization.Serializable

@Serializable
object RouteChatbot

// 챗봇 화면으로 이동할때 활용하는 함수
fun NavController.navigateToChatbot(navOptions: NavOptions? = null) {
    navigate(RouteChatbot, navOptions)
}

fun NavGraphBuilder.chatbotNavGraph(
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
    onNavigateBack: () -> Unit,
    onChatbotRecordClick: () -> Unit = {},
) {
    composable<RouteChatbot> {
        ChatbotRoute(
            onShowErrorSnackBar = onShowErrorSnackBar,
            onNavigateBack = { onNavigateBack() },
            onChatbotRecordClick = { onChatbotRecordClick() },
        )
    }
}
