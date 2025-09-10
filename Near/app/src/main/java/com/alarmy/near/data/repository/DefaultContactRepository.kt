package com.alarmy.near.data.repository

import com.alarmy.near.data.mapper.toModel
import com.alarmy.near.local.contact.ContactLocalDataSource
import com.alarmy.near.model.contact.Contact
import javax.inject.Inject

class DefaultContactRepository
    @Inject
    constructor(
        private val contactDataSource: ContactLocalDataSource,
    ) : ContactRepository {
        override fun fetchAllContacts(): List<Contact> = contactDataSource.getAllContacts().map { it.toModel() }
    }
