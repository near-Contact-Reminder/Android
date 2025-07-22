package com.alarmy.near.feature.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.alarmy.near.feature.home.navigation.RouteHome
import com.alarmy.near.feature.home.navigation.homeNavGraph

@Composable
internal fun NearNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onShowSnackbar: (Throwable?) -> Unit = { _ -> },
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = RouteHome,
    ) {
        homeNavGraph(onShowErrorSnackBar = onShowSnackbar)
    }
}
