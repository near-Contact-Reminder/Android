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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alarmy.near.R
import com.alarmy.near.presentation.ui.component.NearFrame
import com.alarmy.near.presentation.ui.component.button.NearSolidTypeButton
import com.alarmy.near.presentation.ui.component.checkbox.NearBackgroundCheckbox
import com.alarmy.near.presentation.ui.component.textfield.NearSearchTextField
import com.alarmy.near.presentation.ui.theme.NearTheme

@Composable
fun ContactRoute(onShowErrorSnackBar: (throwable: Throwable?) -> Unit) {
    ContactScreen()
}

@Composable
fun ContactScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onSearchTextChange: (String) -> Unit = {},
    onSearchClick: () -> Unit = {},
) {
    val sectionedContacts = groupedContacts.toList()
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
                groupedContacts = groupedContacts,
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
                text = "4명 선택 완료",
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ContactList(groupedContacts: Map<String, List<Contact>>) {
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
                                .clickable { /* 선택 토글 */ }
                                .padding(horizontal = 24.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        NearBackgroundCheckbox(
                            checked = contact.isSelected,
                            onCheckedChange = { /* 선택 처리 */ },
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = contact.name,
                            textAlign = TextAlign.Center,
                            style = NearTheme.typography.B2_14_MEDIUM,
                            color = NearTheme.colors.BLACK_1A1A1A,
                        )
                    }
                    // Divider는 마지막 아이템에는 안 그리도록 처리
                    if (index < contacts.lastIndex) {
                        HorizontalDivider(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp),
                            // 체크박스 공간만큼 들여쓰기
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
        ContactScreen { }
    }
}

data class Contact(
    val name: String,
    val isSelected: Boolean = false,
)

val groupedContacts: Map<String, List<Contact>> =
    mapOf(
        "ㄱ" to listOf(Contact("강민철"), Contact("곽명숙"), Contact("김경이")),
        "ㄴ" to listOf(Contact("나윤희"), Contact("노진구")),
        "ㅅ" to listOf(Contact("서지혜"), Contact("손록형")),
    )
