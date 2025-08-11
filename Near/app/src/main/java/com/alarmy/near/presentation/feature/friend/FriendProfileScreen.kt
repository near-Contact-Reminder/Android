package com.alarmy.near.presentation.feature.friend

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.alarmy.near.presentation.ui.theme.NearTheme

@Composable
fun FriendProfileRoute(onShowErrorSnackBar: (throwable: Throwable?) -> Unit) {
}

@Composable
fun FriendProfileScreen(modifier: Modifier = Modifier) {
}

@Preview(showBackground = true)
@Composable
fun FriendProfileScreenPreview() {
    NearTheme {
        FriendProfileScreen()
    }
}
