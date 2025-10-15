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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alarmy.near.R
import com.alarmy.near.model.contact.Contact
import com.alarmy.near.presentation.feature.contact.state.ContactUiEvent
import com.alarmy.near.presentation.feature.contact.state.ContactUiState
import com.alarmy.near.presentation.feature.contact.state.SelectedContactUiState
import com.alarmy.near.presentation.ui.component.NearFrame
import com.alarmy.near.presentation.ui.component.button.NearSolidTypeButton
import com.alarmy.near.presentation.ui.component.checkbox.NearBackgroundCheckbox
import com.alarmy.near.presentation.ui.component.textfield.NearSearchTextField
import com.alarmy.near.presentation.ui.extension.onNoRippleClick
import com.alarmy.near.presentation.ui.theme.NearTheme
import kotlinx.coroutines.launch

// 선택 완료 및 백 클릭 이벤트 처리
@Composable
fun ContactRoute(
    viewModel: ContactViewModel = hiltViewModel(),
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
    onBackClick: () -> Unit,
    onCompletedSelection: (List<Contact>) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

    // UI 이벤트 처리
    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is ContactUiEvent.Completed -> {
                    onCompletedSelection(event.selectedContacts)
                }
            }
        }
    }

    // 에러 이벤트 처리
    LaunchedEffect(Unit) {
        viewModel.errorEvent.collect { throwable ->
            onShowErrorSnackBar(throwable)
        }
    }

    when (uiState) {
        is ContactUiState.Loading -> {
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(NearTheme.colors.WHITE_FFFFFF),
            ) {
                CircularProgressIndicator(
                    color = NearTheme.colors.BLUE01_5AA2E9,
                    modifier = Modifier.align(Alignment.Center),
                )
            }
        }

        is ContactUiState.Error -> {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(R.string.contact_load_error),
                )
            }
        }

        is ContactUiState.Success -> {
            val contacts = (uiState as ContactUiState.Success).contacts
            ContactScreen(
                contacts = contacts,
                searchQuery = searchQuery,
                onContactCheckedChange = { contactId, isSelected ->
                    viewModel.onContactSelect(isSelected, contactId)
                },
                onBackClick = onBackClick,
                onCompleteClick = viewModel::onCompleteClick,
                onSearchClick = {},
                onSearchTextChange = viewModel::onSearchTextChange,
            )
        }
    }
}

@Composable
fun ContactScreen(
    modifier: Modifier = Modifier,
    contacts: Map<String, List<SelectedContactUiState>> = emptyMap(),
    searchQuery: String = "",
    onBackClick: () -> Unit = {},
    onSearchTextChange: (String) -> Unit = {},
    onSearchClick: () -> Unit = {},
    onContactCheckedChange: (Long, Boolean) -> Unit = { _, _ -> },
    onCompleteClick: () -> Unit = {},
) {
    val selectedContactCount = contacts.values.flatten().count { it.isSelected }
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
                modifier = Modifier.onNoRippleClick { onBackClick() },
                painter = painterResource(R.drawable.ic_close_32_black),
                contentDescription = stringResource(R.string.contact_close_screen),
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        NearSearchTextField(
            placeHolderText = stringResource(R.string.context_search_placeholder),
            modifier = Modifier.padding(horizontal = 20.dp),
            value = searchQuery,
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
                enabled = selectedContactCount != 0,
                modifier =
                    Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 24.dp),
                contentPadding = PaddingValues(vertical = 17.dp),
                onClick = onCompleteClick,
                text = "${selectedContactCount}명 선택 완료",
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
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // 섹션별 첫 번째 아이템 인덱스 계산
    val sectionIndexMap =
        remember(sectionedContacts) {
            val map = mutableMapOf<String, Int>()
            var index = 0
            sectionedContacts.forEach { (initial, contacts) ->
                map[initial] = index // stickyHeader 위치
                index += 1 + contacts.size + 1 // header + items + spacer
            }
            map
        }

    Box {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState,
        ) {
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
                                        onContactCheckedChange(
                                            contact.contact.id,
                                            !contact.isSelected,
                                        )
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
                item { Spacer(modifier = Modifier.height(18.dp)) }
            }
            item {
                Spacer(modifier = Modifier.height(90.dp))
            }
        }

        // 우측 인덱스 바
        val allInitials =
            listOf(
                "ㄱ",
                "ㄴ",
                "ㄷ",
                "ㄹ",
                "ㅁ",
                "ㅂ",
                "ㅅ",
                "ㅇ",
                "ㅈ",
                "ㅊ",
                "ㅋ",
                "ㅌ",
                "ㅍ",
                "ㅎ",
            ) + ('A'..'Z').map { it.toString() } + "#"

        Column(
            modifier =
                Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 12.dp, top = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            allInitials.forEach { initial ->
                Text(
                    text = initial,
                    style =
                        NearTheme.typography.FC_12_BOLD.copy(
                            fontSize = 10.sp,
                            lineHeight = 13.sp,
                            lineHeightStyle =
                                LineHeightStyle(
                                    alignment = LineHeightStyle.Alignment.Center,
                                    trim = LineHeightStyle.Trim.None,
                                ),
                        ),
                    color = NearTheme.colors.BLUE01_5AA2E9,
                    modifier =
                        Modifier
                            .onNoRippleClick {
                                // 실제 존재하는 섹션 중 가장 가까운 이전 섹션 찾기
                                val available = sectionIndexMap.keys.sorted()
                                val target = available.lastOrNull { it <= initial }
                                val index = sectionIndexMap[target]
                                if (index != null) {
                                    coroutineScope.launch {
                                        listState.animateScrollToItem(index)
                                    }
                                }
                            },
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ContactScreenPreview() {
    NearTheme {
        ContactScreen(
            contacts =
                mapOf(
                    "ㄱ" to
                        listOf(
                            SelectedContactUiState(
                                contact =
                                    Contact(
                                        id = 1L,
                                        name = "김철수",
                                        phones = listOf("010-1234-5678"),
                                        photoUri = null,
                                        birthDay = "1995-03-15",
                                        memo = "고등학교 친구",
                                    ),
                                isSelected = false,
                            ),
                            SelectedContactUiState(
                                contact =
                                    Contact(
                                        id = 2L,
                                        name = "강민수",
                                        phones = listOf("010-2222-3333"),
                                        photoUri = null,
                                        birthDay = null,
                                        memo = "회사 동료",
                                    ),
                                isSelected = true,
                            ),
                        ),
                    "ㅂ" to
                        listOf(
                            SelectedContactUiState(
                                contact =
                                    Contact(
                                        id = 3L,
                                        name = "박영희",
                                        phones = listOf("010-9876-5432"),
                                        photoUri = null,
                                        birthDay = null,
                                        memo = null,
                                    ),
                                isSelected = false,
                            ),
                        ),
                    "ㅊ" to
                        listOf(
                            SelectedContactUiState(
                                contact =
                                    Contact(
                                        id = 4L,
                                        name = "최수정",
                                        phones = listOf("010-4444-5555"),
                                        photoUri = null,
                                        birthDay = "1998-07-22",
                                        memo = "대학 동기",
                                    ),
                                isSelected = false,
                            ),
                        ),
                ),
        )
    }
}
