package com.alarmy.near.presentation.feature.chatbotrecord.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.alarmy.near.presentation.feature.chatbotrecord.ChatbotRecordRoute
import kotlinx.serialization.Serializable

@Serializable
object RouteChatbotRecord

fun NavController.navigateToChatbotRecord(navOptions: NavOptions? = null) {
    navigate(RouteChatbotRecord, navOptions)
}

fun NavGraphBuilder.chatbotRecordNavGraph(
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
    onNavigateBack: () -> Unit,
) {
    composable<RouteChatbotRecord> {
        ChatbotRecordRoute(
            onShowErrorSnackBar = onShowErrorSnackBar,
            onNavigateBack = { onNavigateBack() },
        )
    }
}
