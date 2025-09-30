package com.alarmy.near.data.repository

import com.alarmy.near.model.contact.Contact
import kotlinx.coroutines.flow.Flow

interface ContactRepository {
    fun fetchAllContacts(): Flow<List<Contact>>
}
