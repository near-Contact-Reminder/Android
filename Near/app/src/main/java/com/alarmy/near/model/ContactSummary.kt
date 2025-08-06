package com.alarmy.near.model

import androidx.compose.runtime.Immutable
import java.time.LocalDate

@Immutable
data class ContactSummary(
    val id: Long,
    val name: String,
    val profileImageUrl: String,
    val lastContactedAt: LocalDate,
    val isContacted: Boolean,
    val contactFrequency: ContactFrequency,
)
