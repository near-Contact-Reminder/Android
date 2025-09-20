package com.alarmy.near.presentation.feature.alarm

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
            items(1) {
                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(start = 24.dp, end = 20.dp),
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top,
                    ) {
                        Text(
                            text = "스님께 가볍게 안부를 전해보면 어떨까요?",
                            style = NearTheme.typography.B2_14_BOLD,
                            color = NearTheme.colors.BLACK_1A1A1A,
                        )
                        Spacer(modifier = Modifier.width(39.dp))
                        Text(
                            "읽지않음",
                            style = NearTheme.typography.FC_12_MEDIUM,
                            color = NearTheme.colors.GRAY01_888888,
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        "25.03.03",
                        style = NearTheme.typography.FC_12_MEDIUM,
                        color = NearTheme.colors.GRAY01_888888,
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
                if (true) { // TODO 아이템 크기가 마지막일 경우 하단 divider 미 포함
                    HorizontalDivider(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp),
                        color = NearTheme.colors.GRAY03_EBEBEB,
                    )
                }
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
