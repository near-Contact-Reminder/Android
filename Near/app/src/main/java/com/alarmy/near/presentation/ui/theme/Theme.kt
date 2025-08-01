package com.alarmy.near.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

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
    CompositionLocalProvider(
        LocalCustomColors provides colorScheme,
        LocalCustomTypography provides Typography,
        content = content,
    )
}

object NearTheme {
    val colors: NearColor
        @Composable
        get() = LocalCustomColors.current
    val typography: NearTypography
        @Composable
        get() = LocalCustomTypography.current
}
