package com.alarmy.near.presentation.feature.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
internal fun NearApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        NearNavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
        )
    }
}
