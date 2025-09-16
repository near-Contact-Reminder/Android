package com.alarmy.near.presentation.feature.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alarmy.near.model.friendsummary.ContactFrequencyLevel
import com.alarmy.near.model.friendsummary.FriendSummary
import com.alarmy.near.presentation.ui.theme.NearTheme

private const val OVERFLOW_WIDTH_OF_CONTACT_ITEM_BY_NAME_TEXT = 34

@Composable
fun MyContacts(
    modifier: Modifier = Modifier,
    contactsWithPage: List<List<FriendSummary>>,
    pagerState: PagerState =
        rememberPagerState(
            initialPage = 0,
            pageCount = {
                contactsWithPage.count() + if (contactsWithPage.lastOrNull()?.count() == 5) 1 else 0
            },
        ),
    onAddContactClick: () -> Unit = {},
    onContactClick: (contactId: String) -> Unit = {},
) {
    Column(modifier = modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        if (contactsWithPage.isEmpty()) { // 연락처가 아무도 없는 경우
            Column {
                Spacer(modifier = Modifier.height(166.dp))
                AddInitialContactButton(
                    onClick = onAddContactClick,
                )
            }
        } else {
            HorizontalPager(
                modifier = Modifier.height(362.dp),
                state = pagerState,
            ) { page ->
                if (page == pagerState.pageCount - 1 && contactsWithPage
                        .lastOrNull()
                        ?.count() == 5
                ) { // 마지막 Page의 연락처가 5개인 경우
                    Column {
                        Spacer(modifier = Modifier.height(166.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            AddContactButton(
                                onClick = onAddContactClick,
                            )
                        }
                    }
                } else {
                    when (contactsWithPage[page].count()) {
                        1 -> {
                            Column {
                                Spacer(modifier = Modifier.height(166.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center,
                                ) {
                                    ContactItem(
                                        friendSummary = contactsWithPage[page][0],
                                        onClick = onContactClick,
                                    )
                                    Spacer(modifier = Modifier.width((60 - OVERFLOW_WIDTH_OF_CONTACT_ITEM_BY_NAME_TEXT).dp))
                                    AddContactButton(
                                        onClick = onAddContactClick,
                                    )
                                }
                            }
                        }

                        2 -> {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Spacer(modifier = Modifier.height(112.dp))
                                ContactItem(
                                    friendSummary = contactsWithPage[page][0],
                                    onClick = onContactClick,
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Row {
                                    ContactItem(
                                        friendSummary = contactsWithPage[page][1],
                                        onClick = onContactClick,
                                    )
                                    Spacer(modifier = Modifier.width((118 - OVERFLOW_WIDTH_OF_CONTACT_ITEM_BY_NAME_TEXT).dp))
                                    AddContactButton(
                                        onClick = onAddContactClick,
                                    )
                                }
                            }
                        }

                        3 -> {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Spacer(modifier = Modifier.height(76.dp))
                                Box {
                                    ContactItem(
                                        modifier = Modifier.align(Alignment.TopCenter),
                                        friendSummary = contactsWithPage[page][0],
                                        onClick = onContactClick,
                                    )
                                    Row(modifier = Modifier.padding(top = 92.dp, bottom = 78.dp)) {
                                        ContactItem(
                                            friendSummary = contactsWithPage[page][1],
                                            onClick = onContactClick,
                                        )
                                        Spacer(modifier = Modifier.width((141 - OVERFLOW_WIDTH_OF_CONTACT_ITEM_BY_NAME_TEXT).dp))
                                        ContactItem(
                                            friendSummary = contactsWithPage[page][2],
                                            onClick = onContactClick,
                                        )
                                    }
                                    AddContactButton(
                                        modifier = Modifier.align(Alignment.BottomCenter),
                                        onClick = onAddContactClick,
                                    )
                                }
                            }
                        }

                        4 -> {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Spacer(modifier = Modifier.height(68.dp))
                                Box {
                                    ContactItem(
                                        modifier = Modifier.align(Alignment.TopCenter),
                                        friendSummary = contactsWithPage[page][0],
                                        onClick = onContactClick,
                                    )
                                    Row(modifier = Modifier.padding(top = 62.dp)) {
                                        ContactItem(
                                            friendSummary = contactsWithPage[page][1],
                                            onClick = onContactClick,
                                        )
                                        Spacer(modifier = Modifier.width((138 - OVERFLOW_WIDTH_OF_CONTACT_ITEM_BY_NAME_TEXT).dp))
                                        ContactItem(
                                            friendSummary = contactsWithPage[page][2],
                                            onClick = onContactClick,
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                                Row {
                                    ContactItem(
                                        friendSummary = contactsWithPage[page][3],
                                        onClick = onContactClick,
                                    )
                                    Spacer(modifier = Modifier.width((51 - OVERFLOW_WIDTH_OF_CONTACT_ITEM_BY_NAME_TEXT).dp))
                                    AddContactButton(
                                        onClick = onAddContactClick,
                                    )
                                }
                            }
                        }

                        5 -> {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Spacer(modifier = Modifier.height(68.dp))
                                Box {
                                    ContactItem(
                                        modifier = Modifier.align(Alignment.TopCenter),
                                        friendSummary = contactsWithPage[page][0],
                                        onClick = onContactClick,
                                    )
                                    Row(modifier = Modifier.padding(top = 62.dp)) {
                                        ContactItem(
                                            friendSummary = contactsWithPage[page][1],
                                            onClick = onContactClick,
                                        )
                                        Spacer(modifier = Modifier.width((138 - OVERFLOW_WIDTH_OF_CONTACT_ITEM_BY_NAME_TEXT).dp))
                                        ContactItem(
                                            friendSummary = contactsWithPage[page][2],
                                            onClick = onContactClick,
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                                Row {
                                    ContactItem(
                                        friendSummary = contactsWithPage[page][3],
                                        onClick = onContactClick,
                                    )
                                    Spacer(modifier = Modifier.width((51 - OVERFLOW_WIDTH_OF_CONTACT_ITEM_BY_NAME_TEXT).dp))
                                    ContactItem(
                                        friendSummary = contactsWithPage[page][4],
                                        onClick = onContactClick,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 500)
@Composable
fun MyContactsPreview() {
    NearTheme {
        Box {
            MyContacts(
                modifier = Modifier.align(Alignment.Center),
                contactsWithPage =
                    List(5) {
                        FriendSummary(
                            id = "2003",
                            name = "일이삼사오육칠팔구",
                            profileImageUrl = "https://search.yahoo.com/search?p=partiendo",
                            lastContactedAt = "2025-04-21",
                            isContacted = false,
                            contactFrequencyLevel = ContactFrequencyLevel.LOW,
                        )
                    }.chunked(5),
            )
        }
    }
}
