package com.alarmy.near.model

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

data class MonthlyContact(
    val friendId: String,
    val name: String,
    val type: String,
    val nextContactAt: String,
) {
    val dDay: String
        get() {
            val daysBetween = getDaysBetween()
            return when {
                daysBetween == 0L -> "D-day"
                daysBetween > 0L -> "D-$daysBetween"
                else -> "D+${-daysBetween}" // 과거 날짜
            }
        }

    val isDDay: Boolean
        get() = getDaysBetween() == 0L

    private fun getDaysBetween(): Long {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val targetDate = LocalDate.parse(nextContactAt, formatter)
        val today = LocalDate.now()
        return ChronoUnit.DAYS.between(today, targetDate)
    }
}
