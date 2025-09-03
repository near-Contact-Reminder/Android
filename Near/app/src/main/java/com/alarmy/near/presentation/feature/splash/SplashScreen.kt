package com.alarmy.near.presentation.feature.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.alarmy.near.R

@Composable
fun SplashScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToHome: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.img_bg),
            contentDescription = "스플래쉬 배경",
            contentScale = ContentScale.Crop,
        )

        Image(
            painter = painterResource(id = R.drawable.ic_near_logo_title_white),
            contentDescription = stringResource(R.string.near_logo_title),
        )
    }
}


@Preview
@Composable
fun SplashScreenPreview() {
    SplashScreen(onNavigateToLogin = {}, onNavigateToHome = {})
}
