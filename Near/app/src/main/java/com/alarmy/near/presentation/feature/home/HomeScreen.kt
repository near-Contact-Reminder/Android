package com.alarmy.near.presentation.feature.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alarmy.near.R
import com.alarmy.near.presentation.feature.home.model.HomeUiState
import com.alarmy.near.presentation.ui.theme.NearTheme

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeScreen(
    modifier: Modifier = Modifier,
    uiState: HomeUiState,
    onContactClick: (Long) -> Unit = { _ -> },
    onRemoveContact: (Long) -> Unit = { _ -> },
) {
    val density = LocalDensity.current
    val statusBarHeightDp = with(density) { WindowInsets.statusBars.getTop(density).toDp() }

    Column(
        modifier =
            Modifier
                .paint(
                    painter =
                        painterResource(
                            R.drawable.img_bg,
                        ),
                    contentScale = ContentScale.FillBounds,
                ).fillMaxSize(),
    ) {
        Spacer(modifier = Modifier.height(statusBarHeightDp))
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
