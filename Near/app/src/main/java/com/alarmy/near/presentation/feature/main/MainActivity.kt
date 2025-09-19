package com.alarmy.near.presentation.feature.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.alarmy.near.presentation.ui.theme.NearTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setupSplashScreen(splashScreen)

        setContent {
            NearTheme {
                val uiState by mainViewModel.uiState.collectAsStateWithLifecycle()
                if (!uiState.isLoading) {
                    NearApp(
                        startDestination = uiState.startDestination,
                    )
                }
            }
        }
    }

    /**
     * 스플래시 스크린을 설정하고 MainViewModel의 상태를 관찰합니다.
     * API 스플래시가 표시되는 동안 백그라운드에서 검증을 수행합니다.
     */
    private fun setupSplashScreen(splashScreen: SplashScreen) {
        lifecycleScope.launch {
            mainViewModel.uiState.collect { uiState ->
                splashScreen.setKeepOnScreenCondition { uiState.isLoading }
            }
        }
    }
}
