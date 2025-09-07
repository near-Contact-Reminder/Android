package com.alarmy.near.local.contact

import android.content.ContentResolver
import android.provider.ContactsContract
import com.alarmy.near.local.entity.ContactEntity
import javax.inject.Inject

class ContactLocalDataSource
    @Inject
    constructor(
        private val contentResolver: ContentResolver,
    ) {
        fun getAllContacts(): List<ContactEntity> {
            val contacts = mutableListOf<ContactEntity>()

            val cursor =
                contentResolver.query(
                    ContactsContract.Contacts.CONTENT_URI,
                    null, // projection
                    null, // selection
                    null, // selectionArgs
                    "${ContactsContract.Contacts.DISPLAY_NAME} ASC",
                )

            cursor?.use {
                val idIndex = it.getColumnIndex(ContactsContract.Contacts._ID)
                val nameIndex = it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                val hasPhoneIndex = it.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)

                while (it.moveToNext()) {
                    val id = it.getLong(idIndex)
                    val name = it.getString(nameIndex) ?: ""
                    val hasPhone = it.getInt(hasPhoneIndex) > 0

                    val phones = mutableListOf<String>()
                    if (hasPhone) {
                        val phoneCursor =
                            contentResolver.query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?",
                                arrayOf(id.toString()),
                                null,
                            )
                        phoneCursor?.use { pc ->
                            val phoneIndex =
                                pc.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                            while (pc.moveToNext()) {
                                phones.add(pc.getString(phoneIndex))
                            }
                        }
                    }

                    contacts.add(ContactEntity(id, name, phones))
                }
            }

            return contacts
        }
    }
