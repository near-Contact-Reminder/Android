package com.alarmy.near.presentation.feature.alarm

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alarmy.near.presentation.ui.component.NearFrame
import com.alarmy.near.presentation.ui.component.appbar.NearTopAppbar
import com.alarmy.near.presentation.ui.theme.NearTheme

@Composable
fun AlarmRoute() {
    AlarmScreen()
}

@Composable
fun AlarmScreen(modifier: Modifier = Modifier) {
    NearFrame(modifier = modifier) {
        NearTopAppbar(title = "알림", onClickBackButton = {})
        Spacer(modifier = Modifier.height(12.dp))
        LazyColumn {
            items(10) {
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AlarmScreenPreview() {
    NearTheme {
        AlarmScreen()
    }
}
