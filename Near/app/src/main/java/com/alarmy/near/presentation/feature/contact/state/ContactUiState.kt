package com.alarmy.near.presentation.feature.contact.state

import com.alarmy.near.model.contact.Contact

sealed class ContactUiState {
    object Loading : ContactUiState()

    data class Success(
        val contacts: Map<String, List<SelectedContactUiState>>,
    ) : ContactUiState()

    data class Error(
        val throwable: Throwable,
    ) : ContactUiState()
}

data class SelectedContactUiState(
    val contact: Contact,
    val isSelected: Boolean = false,
)
