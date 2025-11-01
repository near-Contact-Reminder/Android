package com.alarmy.near.presentation.feature.mothlyreminderall

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun MonthlyReminderAllScreen(
    viewModel: MonthlyReminderAllViewModel = hiltViewModel(),
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
) {
    Surface(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Color.Black),
    ) { }
}
