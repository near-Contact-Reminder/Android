package com.alarmy.near.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alarmy.near.feature.home.model.HomeUiState
import com.alarmy.near.ui.theme.NearTheme

@Composable
internal fun HomeRoute(
    viewModel: HomeViewModel = hiltViewModel(),
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
) {
    val uiState = viewModel.uiStateFlow.collectAsStateWithLifecycle()
    HomeScreen(
        uiState = uiState.value,
        onContactClick = {},
        onRemoveContact = viewModel::removeContact,
    )
}

@Composable
internal fun HomeScreen(
    modifier: Modifier = Modifier,
    uiState: HomeUiState,
    onContactClick: (Long) -> Unit = { _ -> },
    onRemoveContact: (Long) -> Unit = { _ -> },
) {
    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Text("홈 화면")
    }
}

@Preview
@Composable
internal fun HomeScreenPreview() {
    NearTheme {
        HomeScreen(
            uiState = HomeUiState.Loading,
            onContactClick = {},
            onRemoveContact = {},
        )
    }
}
