package com.alarmy.near.presentation.feature.contact

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alarmy.near.data.repository.ContactRepository
import com.alarmy.near.presentation.feature.contact.state.ContactUiEvent
import com.alarmy.near.presentation.feature.contact.state.ContactUiState
import com.alarmy.near.presentation.feature.contact.state.SelectedContactUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactViewModel
    @Inject
    constructor(
        contactRepository: ContactRepository,
    ) : ViewModel() {
        private val _uiEvent = Channel<ContactUiEvent>()
        val uiEvent = _uiEvent.receiveAsFlow()

        private val selectedIds = MutableStateFlow<Set<Long>>(emptySet())
        private val _searchQuery = MutableStateFlow("")
        val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

        // 원본 연락처 리스트
        private val contactsFlow = contactRepository.fetchAllContacts()

        val uiState: StateFlow<ContactUiState> =
            combine(
                contactsFlow,
                selectedIds,
                _searchQuery,
            ) { contacts, selectedIds, query ->
                val filtered =
                    if (query.isBlank()) {
                        contacts
                    } else {
                        contacts.filter { contact ->
                            contact.name.contains(query, ignoreCase = true)
                        }
                    }

                val uiContacts =
                    filtered.map { contact ->
                        SelectedContactUiState(
                            contact = contact,
                            isSelected = contact.id in selectedIds,
                        )
                    }

                // groupBy → 정렬된 Map 으로 변환
                val grouped = uiContacts.groupBy { getInitial(it.contact.name) }
                val sorted = grouped.toSortedMap(initialComparator)

                ContactUiState.Success(
                    contacts = sorted,
                )
            }.stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000L),
                ContactUiState.Loading,
            )

        // 한글 - 영어 - 특수문자 순으로 정렬하는 Comparator
        private val initialComparator =
            Comparator<String> { a, b ->
                val orderA = categoryOrder(a)
                val orderB = categoryOrder(b)

                if (orderA == orderB) {
                    a.compareTo(b) // 같은 카테고리 안에서는 알파벳/자음 순 정렬
                } else {
                    orderA - orderB
                }
            }

        // 한글=0, 영어=1, 그 외=2
        private fun categoryOrder(initial: String): Int =
            when {
                initial.first() in 'ㄱ'..'ㅎ' -> 0
                initial.first().isLetter() -> 1
                else -> 2
            }

        fun onContactSelect(
            isSelected: Boolean,
            contactId: Long,
        ) {
            selectedIds.update { ids ->
                if (isSelected) ids + contactId else ids - contactId
            }
        }

        fun onSearchTextChange(value: String) {
            _searchQuery.value = value
        }

        fun onCompleteClick() {
            val selected =
                (uiState.value as? ContactUiState.Success)
                    ?.contacts
                    ?.flatMap { it.value }
                    ?.filter { it.isSelected }
                    ?.map {
                        it.contact
                    } ?: return
            viewModelScope.launch {
                _uiEvent.send(ContactUiEvent.Completed(selected))
            }
        }

        private fun getInitial(name: String): String {
            if (name.isEmpty()) return "#"

            val ch = name.first()
            return if (ch in '가'..'힣') {
                val base = ch.code - 0xAC00
                val initialIndex = base / (21 * 28)

                val initials =
                    listOf(
                        "ㄱ",
                        "ㄲ",
                        "ㄴ",
                        "ㄷ",
                        "ㄸ",
                        "ㄹ",
                        "ㅁ",
                        "ㅂ",
                        "ㅃ",
                        "ㅅ",
                        "ㅆ",
                        "ㅇ",
                        "ㅈ",
                        "ㅉ",
                        "ㅊ",
                        "ㅋ",
                        "ㅌ",
                        "ㅍ",
                        "ㅎ",
                    )
                val initial = initials[initialIndex]
                when (initial) {
                    "ㄲ" -> "ㄱ"
                    "ㄸ" -> "ㄷ"
                    "ㅃ" -> "ㅂ"
                    "ㅆ" -> "ㅅ"
                    "ㅉ" -> "ㅈ"
                    else -> initial
                }
            } else {
                if (ch.isLetter()) ch.uppercaseChar().toString() else "#"
            }
        }
    }
