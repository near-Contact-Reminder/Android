package com.alarmy.near.presentation.feature.contact

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alarmy.near.R
import com.alarmy.near.presentation.feature.contact.state.ContactUiState
import com.alarmy.near.presentation.feature.contact.state.SelectedContactUiState
import com.alarmy.near.presentation.ui.component.NearFrame
import com.alarmy.near.presentation.ui.component.button.NearSolidTypeButton
import com.alarmy.near.presentation.ui.component.checkbox.NearBackgroundCheckbox
import com.alarmy.near.presentation.ui.component.textfield.NearSearchTextField
import com.alarmy.near.presentation.ui.theme.NearTheme

// 선택 완료 및 백 클릭 이벤트 처리
@Composable
fun ContactRoute(
    viewModel: ContactViewModel = hiltViewModel(),
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (uiState) {
        is ContactUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                )
            }
        }

        is ContactUiState.Error -> {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(modifier = Modifier.align(Alignment.Center), text = stringResource(R.string.contact_load_error))
            }
        }

        is ContactUiState.Success -> {
            val contacts = (uiState as ContactUiState.Success).contacts
            ContactScreen(
                contacts = contacts,
                onContactCheckedChange = { contactId, isSelected ->
                    viewModel.onContactSelect(isSelected, contactId)
                },
            )
        }
    }
}

@Composable
fun ContactScreen(
    modifier: Modifier = Modifier,
    contacts: Map<String, List<SelectedContactUiState>> = emptyMap(),
    onBackClick: () -> Unit = {},
    onSearchTextChange: (String) -> Unit = {},
    onSearchClick: () -> Unit = {},
    onContactCheckedChange: (Long, Boolean) -> Unit = { _, _ -> },
) {
    NearFrame(modifier = modifier) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 24.dp,
                        end = 20.dp,
                        top = 8.dp,
                        bottom = 8.dp,
                    ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                stringResource(R.string.contact_title_text),
                style = NearTheme.typography.B1_16_BOLD,
                color = NearTheme.colors.BLACK_1A1A1A,
            )
            Image(
                painter = painterResource(R.drawable.ic_close_32_black),
                contentDescription = stringResource(R.string.contact_close_screen),
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        NearSearchTextField(
            placeHolderText = stringResource(R.string.context_search_placeholder),
            modifier = Modifier.padding(horizontal = 20.dp),
            value = "",
            onValueChange = onSearchTextChange,
            onSearchClick = onSearchClick,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .fillMaxSize(),
        ) {
            ContactList(
                groupedContacts = contacts,
                onContactCheckedChange = onContactCheckedChange,
            )
            NearSolidTypeButton(
                modifier =
                    Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 24.dp),
                contentPadding = PaddingValues(vertical = 17.dp),
                onClick = {},
                text = "${contacts.values.flatten().count { it.isSelected }}명 선택 완료",
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ContactList(
    groupedContacts: Map<String, List<SelectedContactUiState>>,
    onContactCheckedChange: (Long, Boolean) -> Unit,
) {
    val sectionedContacts = groupedContacts.toList()
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        sectionedContacts.forEach { (initial, contacts) ->
            stickyHeader {
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .background(NearTheme.colors.BG02_F4F9FD)
                            .padding(vertical = 12.dp, horizontal = 24.dp),
                ) {
                    Text(
                        text = initial,
                        style = NearTheme.typography.B1_16_BOLD,
                        color = NearTheme.colors.BLACK_1A1A1A,
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
            }
            itemsIndexed(contacts) { index, contact ->
                Column {
                    Row(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onContactCheckedChange(contact.contact.id, !contact.isSelected)
                                }.padding(horizontal = 24.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        NearBackgroundCheckbox(
                            checked = contact.isSelected,
                            onCheckedChange = { checked ->
                                onContactCheckedChange(contact.contact.id, checked)
                            },
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = contact.contact.name,
                            textAlign = TextAlign.Center,
                            style = NearTheme.typography.B2_14_MEDIUM,
                            color = NearTheme.colors.BLACK_1A1A1A,
                        )
                    }
                    if (index < contacts.lastIndex) {
                        HorizontalDivider(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp),
                            color = NearTheme.colors.GRAY03_EBEBEB,
                            thickness = 1.dp,
                        )
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(18.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ContactScreenPreview() {
    NearTheme {
        ContactScreen()
    }
}
