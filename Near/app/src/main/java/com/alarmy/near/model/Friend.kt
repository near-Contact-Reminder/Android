package com.alarmy.near.model

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

data class Friend(
    val friendId: String,
    val imageUrl: String?,
    val relation: Relation,
    val name: String,
    val contactFrequency: ContactFrequency,
    val birthday: String?,
    val anniversaryList: List<Anniversary>,
    val memo: String?,
    val phone: String?,
    val lastContactAt: String?, // "2025-07-16"
) {
    val isContactedToday: Boolean
        get() = lastContactAt?.isToday() ?: false

    private fun String.isToday(): Boolean {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.KOREA)
        val targetDate = LocalDate.parse(this, formatter)
        val today = LocalDate.now()
        return targetDate == today
    }
}

data class ContactFrequency(
    val reminderInterval: ReminderInterval,
    val dayOfWeek: String,
)

data class Anniversary(
    val id: Int,
    val title: String,
    val date: String,
)
