package com.alarmy.near.presentation.feature.friendprofileedittor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alarmy.near.presentation.ui.component.appbar.NearTopAppbar
import com.alarmy.near.presentation.ui.component.textfield.NearTextField
import com.alarmy.near.presentation.ui.theme.NearTheme

@Composable
fun FriendProfileEditorRoute(onShowErrorSnackBar: (throwable: Throwable?) -> Unit) {
    FriendProfileEditorScreen()
}

@Composable
fun FriendProfileEditorScreen(modifier: Modifier = Modifier) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(NearTheme.colors.WHITE_FFFFFF),
    ) {
        NearTopAppbar(
            modifier = Modifier.padding(end = 24.dp),
            title = "",
            onClickBackButton = {},
            menuButton = {
                Text(
                    text = "완료",
                    style = NearTheme.typography.B1_16_BOLD,
                    color = NearTheme.colors.BLACK_1A1A1A,
                )
            },
        )
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 20.dp),
        ) {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth(),
            ) {
                Text(
                    text =
                        buildAnnotatedString {
                            append("이름")
                            withStyle(
                                style =
                                    SpanStyle(
                                        color = NearTheme.colors.BLUE01_5AA2E9,
                                    ),
                            ) {
                                append("*")
                            }
                        },
                    textAlign = TextAlign.Center,
                    style = NearTheme.typography.B2_14_MEDIUM,
                    color = NearTheme.colors.GRAY01_888888,
                )
                NearTextField(
                    value = "test",
                    onValueChange = {
                    },
                )
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun FriendProfileEditorScreenPreview() {
    NearTheme {
        FriendProfileEditorScreen()
    }
}
