package com.alarmy.near.presentation.feature.main

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch

@Composable
internal fun NearApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val snackBarState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarState,
                modifier =
                    Modifier.windowInsetsPadding(
                        WindowInsets.safeDrawing.exclude(
                            WindowInsets.ime,
                        ),
                    ),
            )
        },
    ) { innerPadding ->
        NearNavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            onShowSnackbar = {
                scope.launch {
                    snackBarState.showSnackbar(
                        message = it?.message ?: return@launch,
                        duration = SnackbarDuration.Short,
                    )
                }
            },
        )
    }
}
