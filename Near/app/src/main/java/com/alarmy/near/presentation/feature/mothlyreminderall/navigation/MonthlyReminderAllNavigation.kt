package com.alarmy.near.presentation.feature.mothlyreminderall.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.alarmy.near.presentation.feature.mothlyreminderall.MonthlyReminderAllRoute
import kotlinx.serialization.Serializable

@Serializable
object RouteMonthlyReminderAll

fun NavController.navigateToMonthlyReminderAll() {
    navigate(RouteMonthlyReminderAll)
}

fun NavGraphBuilder.monthlyReminderAllNavGraph(
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
    onNavigateBack: () -> Unit,
) {
    composable<RouteMonthlyReminderAll> { backStackEntry ->
        MonthlyReminderAllRoute(
            onShowErrorSnackBar = onShowErrorSnackBar,
            onNavigateBack = onNavigateBack,
        )
    }
}
