package com.alarmy.near.data.repository

import com.alarmy.near.model.contact.Contact

interface ContactRepository {
    fun fetchAllContacts(): List<Contact>
}
