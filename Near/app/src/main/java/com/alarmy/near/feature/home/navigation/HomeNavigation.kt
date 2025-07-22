package com.alarmy.near.feature.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.alarmy.near.feature.home.HomeRoute
import kotlinx.serialization.Serializable

@Serializable
object RouteHome

/*
* 추후 홈으로 화면 이동이 필요할 때 이 함수를 사용합니다.
* */
fun NavController.navigateToHome(navOptions: NavOptions) {
    navigate(RouteHome, navOptions)
}

fun NavGraphBuilder.homeNavGraph(
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
    onClickContact: (id: Long) -> Unit,
) {
    composable<RouteHome> { backStackEntry ->
        HomeRoute(
            onShowErrorSnackBar = onShowErrorSnackBar,
        )
    }
}
