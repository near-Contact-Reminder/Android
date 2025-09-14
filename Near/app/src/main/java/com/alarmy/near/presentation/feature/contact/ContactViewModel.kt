package com.alarmy.near.presentation.feature.contact

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alarmy.near.data.repository.ContactRepository
import com.alarmy.near.presentation.feature.contact.state.ContactUiState
import com.alarmy.near.presentation.feature.contact.state.SelectedContactUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ContactViewModel
    @Inject
    constructor(
        contactRepository: ContactRepository,
    ) : ViewModel() {
        private val selectedIds = MutableStateFlow<Set<Long>>(emptySet())

        val uiState: StateFlow<ContactUiState> =
            combine(
                contactRepository.fetchAllContacts(), // Flow<List<Contact>>
                selectedIds,
            ) { contacts, selectedIds ->
                val uiContacts =
                    contacts.map { contact ->
                        SelectedContactUiState(
                            contact = contact,
                            isSelected = contact.id in selectedIds,
                        )
                    }
                // 필요하다면 groupBy { 초성 } 해서 Map 형태로 반환 가능
                ContactUiState.Success(
                    contacts = uiContacts.groupBy { getInitial(it.contact.name) },
                )
            }.stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000L),
                ContactUiState.Loading,
            )

        fun onContactSelect(
            isSelected: Boolean,
            contactId: Long,
        ) {
            selectedIds.update { ids ->
                if (isSelected) {
                    ids + contactId // 선택 추가
                } else {
                    ids - contactId // 선택 해제
                }
            }
        }

        fun getInitial(name: String): String {
            if (name.isEmpty()) return "#"

            val firstChar = name.first()

            // 한글 범위 (가 ~ 힣)
            if (firstChar in '가'..'힣') {
                val base = firstChar.code - 0xAC00
                val initialIndex = base / (21 * 28) // 초성 인덱스
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
            return initials[initialIndex]
        }

        // 알파벳 → 대문자
        if (firstChar.isLetter()) {
            return firstChar.uppercaseChar().toString()
        }

        // 그 외 → #
        return "#"
    }
}
