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
                val photoIndex = it.getColumnIndex(ContactsContract.Contacts.PHOTO_URI)

                while (it.moveToNext()) {
                    val id = it.getLong(idIndex)
                    val name = it.getString(nameIndex) ?: ""
                    val hasPhone = it.getInt(hasPhoneIndex) > 0
                    val photoUri = it.getString(photoIndex)

                    // 전화번호 가져오기
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

                    // 메모(Note) 가져오기
                    var memo: String? = null
                    val noteCursor =
                        contentResolver.query(
                            ContactsContract.Data.CONTENT_URI,
                            arrayOf(ContactsContract.CommonDataKinds.Note.NOTE),
                            "${ContactsContract.Data.CONTACT_ID} = ? AND ${ContactsContract.Data.MIMETYPE} = ?",
                            arrayOf(id.toString(), ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE),
                            null,
                        )
                    noteCursor?.use { nc ->
                        if (nc.moveToFirst()) {
                            memo = nc.getString(0)
                        }
                    }

                    // 생일(Birthday) 가져오기
                    var birthDay: String? = null
                    val birthdayCursor =
                        contentResolver.query(
                            ContactsContract.Data.CONTENT_URI,
                            arrayOf(ContactsContract.CommonDataKinds.Event.START_DATE),
                            "${ContactsContract.Data.CONTACT_ID} = ? AND ${ContactsContract.Data.MIMETYPE} = ? AND ${ContactsContract.CommonDataKinds.Event.TYPE} = ?",
                            arrayOf(
                                id.toString(),
                                ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE,
                                ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY
                                    .toString(),
                            ),
                            null,
                        )
                    birthdayCursor?.use { bc ->
                        if (bc.moveToFirst()) {
                            birthDay = bc.getString(0)
                        }
                    }

                    contacts.add(ContactEntity(id, name, phones, photoUri, birthDay, memo))
                }
            }

            return contacts
        }
    }
