package com.alarmy.near.model

import androidx.compose.runtime.Immutable
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Immutable
data class ContactSummary(
    val id: String,
    val name: String,
    val profileImageUrl: String,
    val lastContactedAt: LocalDate,
    val isContacted: Boolean,
    val contactFrequency: ContactFrequency,
) {
    val formattedDate: String
        get() = lastContactedAt.format(formatter)

    companion object {
        private val formatter = DateTimeFormatter.ofPattern("MMM d, yyyy")
    }
}
