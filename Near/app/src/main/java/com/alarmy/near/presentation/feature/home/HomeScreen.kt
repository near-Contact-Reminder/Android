package com.alarmy.near.presentation.feature.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alarmy.near.R
import com.alarmy.near.presentation.feature.home.model.HomeUiState
import com.alarmy.near.presentation.ui.extension.onNoRippleClick
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

    Surface(modifier = modifier) {
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
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .padding(end = 20.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    stringResource(R.string.home_my_profile_button_text),
                    style = NearTheme.typography.H2_18_BOLD.copy(letterSpacing = 0.sp),
                    color = NearTheme.colors.WHITE_FFFFFF,
                )
                Spacer(modifier = Modifier.width(12.dp))
                Image(painterResource(R.drawable.icon_32_bell), contentDescription = "")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text =
                    buildAnnotatedString {
                        append("정하은님,\n")
                        withStyle(
                            SpanStyle(
                                fontWeight = FontWeight.Bold,
                            ),
                        ) {
                            append("누구를 챙길지")
                        }
                        append(" 정해볼까요?")
                    },
                modifier = Modifier.padding(horizontal = 24.dp),
                style = NearTheme.typography.H1_24_REGULAR,
                color = NearTheme.colors.WHITE_FFFFFF,
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = stringResource(R.string.home_this_month_people),
                modifier = Modifier.padding(horizontal = 24.dp),
                style = NearTheme.typography.B1_16_BOLD,
                color = NearTheme.colors.WHITE_FFFFFF,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Surface(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .clip(RoundedCornerShape(12.dp)),
                color = NearTheme.colors.WHITE_FFFFFF.copy(alpha = 0.2f),
            ) {
                Text(
                    text = stringResource(R.string.home_no_people_this_month),
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 14.dp),
                    textAlign = TextAlign.Center,
                    style =
                        NearTheme.typography.B2_14_MEDIUM.copy(
                            fontWeight = FontWeight.Normal,
                        ),
                    color = NearTheme.colors.WHITE_FFFFFF,
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(
                            color = NearTheme.colors.WHITE_FFFFFF,
                            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                        ),
            ) {
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .align(Alignment.TopCenter)
                            .padding(top = 20.dp, start = 24.dp, end = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        stringResource(R.string.home_my_people),
                        style = NearTheme.typography.H2_18_BOLD,
                        color = NearTheme.colors.BLACK_1A1A1A,
                    )
                    Icon(
                        painterResource(R.drawable.icon_32_menu),
                        contentDescription = stringResource(R.string.home_my_people_setting),
                    )
                }
                AddingUserButton()
            }
        }
    }
}

@Composable
private fun BoxScope.AddingUserButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Column(
        modifier =
            modifier
                .align(Alignment.Center)
                .onNoRippleClick(
                    onClick = onClick,
                ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painterResource(R.drawable._icon_64_adduser),
            contentDescription =
                stringResource(
                    R.string.home_add_contact,
                ),
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            stringResource(R.string.home_add_contact_description),
            style = NearTheme.typography.B2_14_MEDIUM,
            color = NearTheme.colors.BLACK_1A1A1A.copy(alpha = 0.3f),
            textAlign = TextAlign.Center,
        )
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
