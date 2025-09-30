package com.alarmy.near.local.contact

import android.content.ContentResolver
import android.provider.ContactsContract
import com.alarmy.near.local.entity.ContactEntity
import com.alarmy.near.local.entity.ImportantDate
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
                    null,
                    null,
                    null,
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

                    // 전화번호
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

                    // 메모
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

                    // 생일
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

                    // 그룹
                    val groups = mutableListOf<String>()
                    val groupCursor =
                        contentResolver.query(
                            ContactsContract.Data.CONTENT_URI,
                            arrayOf(ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID),
                            "${ContactsContract.Data.CONTACT_ID} = ? AND ${ContactsContract.Data.MIMETYPE} = ?",
                            arrayOf(
                                id.toString(),
                                ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE,
                            ),
                            null,
                        )
                    groupCursor?.use { gc ->
                        val groupIdIndex =
                            gc.getColumnIndex(
                                ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID,
                            )
                        while (gc.moveToNext()) {
                            val groupId = gc.getLong(groupIdIndex)
                            val groupNameCursor =
                                contentResolver.query(
                                    ContactsContract.Groups.CONTENT_URI,
                                    arrayOf(ContactsContract.Groups.TITLE),
                                    "${ContactsContract.Groups._ID} = ?",
                                    arrayOf(groupId.toString()),
                                    null,
                                )
                            groupNameCursor?.use { gnc ->
                                if (gnc.moveToFirst()) {
                                    groups.add(gnc.getString(0))
                                }
                            }
                        }
                    }

                    // 중요한 날 (기념일)
                    val importantDates = mutableListOf<ImportantDate>()
                    val eventCursor =
                        contentResolver.query(
                            ContactsContract.Data.CONTENT_URI,
                            arrayOf(
                                ContactsContract.CommonDataKinds.Event.START_DATE,
                                ContactsContract.CommonDataKinds.Event.TYPE,
                                ContactsContract.CommonDataKinds.Event.LABEL,
                            ),
                            "${ContactsContract.Data.CONTACT_ID} = ? AND ${ContactsContract.Data.MIMETYPE} = ?",
                            arrayOf(
                                id.toString(),
                                ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE,
                            ),
                            null,
                        )
                    eventCursor?.use { ec ->
                        val dateIndex =
                            ec.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE)
                        val typeIndex =
                            ec.getColumnIndex(ContactsContract.CommonDataKinds.Event.TYPE)
                        val labelIndex =
                            ec.getColumnIndex(ContactsContract.CommonDataKinds.Event.LABEL)

                        while (ec.moveToNext()) {
                            val date = ec.getString(dateIndex)
                            val type = ec.getInt(typeIndex)
                            val customLabel = ec.getString(labelIndex)

                            val label =
                                when (type) {
                                    ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY -> {
                                        continue // 위에서 생일은 포함했으므로 스킵
                                    }

                                    ContactsContract.CommonDataKinds.Event.TYPE_ANNIVERSARY -> "기념일"
                                    ContactsContract.CommonDataKinds.Event.TYPE_OTHER ->
                                        customLabel
                                            ?: "기타"

                                    else -> "알 수 없음"
                                }

                            if (date != null) {
                                importantDates.add(ImportantDate(label, date))
                            }
                        }
                    }

                    contacts.add(
                        ContactEntity(
                            id = id,
                            name = name,
                            phones = phones,
                            photoUri = photoUri,
                            birthDay = birthDay,
                            memo = memo,
                            groups = groups,
                            importantDates = importantDates,
                        ),
                    )
                }
            }

            return contacts
        }
    }
