package com.alarmy.near.presentation.feature.alarm.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object RouteAlarm

// 로그인 화면으로 이동하는 확장 함수
fun NavController.navigateToAlarm(navOptions: NavOptions? = null) {
    navigate(RouteAlarm, navOptions)
}

// 로그인 화면 NavGraph 정의
fun NavGraphBuilder.alarmNavGraph(onShowErrorSnackBar: (throwable: Throwable?) -> Unit) {
    composable<RouteAlarm> {
    }
}
