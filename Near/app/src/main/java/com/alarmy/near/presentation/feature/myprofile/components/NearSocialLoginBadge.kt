package com.alarmy.near.presentation.feature.myprofile.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alarmy.near.presentation.feature.myprofile.LoginType
import com.alarmy.near.presentation.ui.theme.NearTheme

@Composable
fun NearSocialLoginBadge(loginType: LoginType) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // 소셜 로그인 텍스트
        Text(
            text = loginType.typeTitle,
            style = NearTheme.typography.FC_12_MEDIUM,
            color = NearTheme.colors.BLACK_1A1A1A,
        )

        // 소셜 로그인 아이콘
        loginType.logoTitle?.let { logo ->
            Image(
                painter = painterResource(id = logo),
                contentDescription = "소셜 로그인 아이콘",
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NearSocialLoginBadgePreview() {
    NearTheme {
        NearSocialLoginBadge(
            loginType = LoginType.KAKAO,
        )
    }
}
