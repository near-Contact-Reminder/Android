package com.alarmy.near.presentation.feature.friendprofileedittor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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
    }
}

@Preview(showBackground = true)
@Composable
fun FriendProfileEditorScreenPreview() {
}
