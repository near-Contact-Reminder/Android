package com.alarmy.near.presentation.feature.friendprofile.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alarmy.near.R
import com.alarmy.near.presentation.ui.component.button.NearBasicButton
import com.alarmy.near.presentation.ui.theme.NearTheme

@Composable
fun CallButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = false,
) {
    NearBasicButton(
        modifier = modifier,
        onClick = onClick,
        colors =
            ButtonDefaults.buttonColors(
                containerColor = NearTheme.colors.BG02_F4F9FD,
                contentColor = NearTheme.colors.BLACK_1A1A1A,
                disabledContainerColor = Color(0xfff7f7f7),
                disabledContentColor = NearTheme.colors.GRAY02_B7B7B7,
            ),
        contentPadding = PaddingValues(start = 42.dp, end = 45.dp, top = 12.dp, bottom = 12.dp),
        enabled = enabled,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(if (enabled) R.drawable.ic_visual_24_call else R.drawable.ic_visual_24_call_gray),
                contentDescription = stringResource(R.string.friend_profile_call),
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = stringResource(R.string.friend_profile_call),
                style = NearTheme.typography.B2_14_MEDIUM,
            )
        }
    }
}

@Preview(widthDp = 360, heightDp = 300, showBackground = true)
@Composable
fun CallButtonPreview() {
    NearTheme {
        Column {
            CallButton(onClick = {}, enabled = false)
            Spacer(modifier = Modifier.height(10.dp))
            CallButton(onClick = {}, enabled = true)
        }
    }
}
