package com.alarmy.near.presentation.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import com.alarmy.near.presentation.ui.theme.NearTheme

@Composable
fun NearFrame(
    modifier: Modifier = Modifier,
    backgroundColor: Color = NearTheme.colors.WHITE_FFFFFF,
    content: @Composable ColumnScope.() -> Unit,
) {
    val density = LocalDensity.current
    val statusBarHeightDp = with(density) { WindowInsets.statusBars.getTop(density).toDp() }
    val navigationBarHeightDp = with(density) { WindowInsets.navigationBars.getBottom(density).toDp() }
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(
                    color = backgroundColor,
                ).padding(top = statusBarHeightDp, bottom = navigationBarHeightDp),
        content = content,
    )
}
