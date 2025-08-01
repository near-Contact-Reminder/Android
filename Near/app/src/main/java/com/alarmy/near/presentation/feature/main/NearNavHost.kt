package com.alarmy.near.presentation.feature.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.alarmy.near.presentation.feature.home.navigation.RouteHome
import com.alarmy.near.presentation.feature.home.navigation.homeNavGraph

@Composable
internal fun NearNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onShowSnackbar: (Throwable?) -> Unit = { _ -> },
) {
    /*
     * 화면 이동 및 구성을 위한 컴포저블 함수입니다.
     * */
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = RouteHome,
    ) {
        homeNavGraph(onShowErrorSnackBar = onShowSnackbar, onClickContact = {
            // 예시: navController.navigate(RouteContact(it))
        })
    }
}
