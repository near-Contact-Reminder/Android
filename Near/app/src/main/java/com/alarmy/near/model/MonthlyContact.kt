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
    fun daysUntilNextContact(today: LocalDate): String {
        val daysBetween = getDaysBetween(today)
        return when {
            daysBetween == 0L -> "D-day"
            daysBetween > 0L -> "D-$daysBetween"
            else -> "D+${-daysBetween}" // 과거 날짜
        }
    }

    fun isNextContactDay(today: LocalDate): Boolean = getDaysBetween(today) == 0L

    private fun getDaysBetween(today: LocalDate): Long {
        val targetDate = LocalDate.parse(nextContactAt, formatter)
        return ChronoUnit.DAYS.between(today, targetDate)
    }

    companion object {
        private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    }
}
