package com.alarmy.near.presentation.feature.contact.state

import com.alarmy.near.model.contact.Contact

sealed class ContactUiEvent {
    data class Completed(val selectedContacts: List<Contact>) : ContactUiEvent()
}
