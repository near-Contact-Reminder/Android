package com.alarmy.near.presentation.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Density
import androidx.core.view.WindowCompat

val LocalCustomColors =
    staticCompositionLocalOf {
        NearColor()
    }

val LocalCustomTypography =
    staticCompositionLocalOf {
        Typography
    }

@Composable
fun NearTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme =
        when {
            darkTheme -> lightColor // TODO DarkTheme 추가시 수정
            else -> lightColor
        }
    val currentDensity = LocalDensity.current
    val themeDensity =
        Density(
            density = currentDensity.density,
            fontScale = 1f,
        )
    CompositionLocalProvider(
        LocalDensity provides themeDensity,
        LocalCustomColors provides colorScheme,
        LocalCustomTypography provides Typography,
        content = content,
    )

/* 스크린에서 상태바 아이콘 색상
*  */
    val view = LocalView.current
//    val isDarkTheme = isSystemInDarkTheme() 시스템 다크 모드 여부를 Boolean으로 반환
    val isDarkTheme = false

    SideEffect {
        if (!view.isInEditMode) {
            val window = (view.context as Activity).window
            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = !isDarkTheme
        }
    }
}

object NearTheme {
    val colors: NearColor
        @Composable
        get() = LocalCustomColors.current
    val typography: NearTypography
        @Composable
        get() = LocalCustomTypography.current
}
