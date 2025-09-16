package com.alarmy.near.presentation.ui.component.appbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alarmy.near.R
import com.alarmy.near.presentation.ui.extension.onNoRippleClick
import com.alarmy.near.presentation.ui.theme.NearTheme

@Composable
fun NearCancelTopAppBar(
    modifier: Modifier = Modifier,
    title: String = "",
    onCancelClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = NearTheme.typography.B1_16_BOLD,
            color = NearTheme.colors.BLACK_1A1A1A
        )

        Image(
            modifier = Modifier
                .onNoRippleClick(onClick = onCancelClick),
            painter = painterResource(id = R.drawable.ic_32_cancel),
            contentDescription = "나가기"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NearCancelTopAppBarPreview() {
    NearTheme {
        NearCancelTopAppBar(
            title = "탈퇴하기",
            onCancelClick = { }
        )
    }
}
