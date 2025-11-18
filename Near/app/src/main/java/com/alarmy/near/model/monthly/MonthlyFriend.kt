package com.alarmy.near.model.monthly

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

data class MonthlyFriend(
    val friendId: String,
    val name: String,
    val type: MonthlyFriendType,
    val nextContactAt: String,
) {
    fun daysUntilNextContact(today: LocalDate): String {
        val daysBetween = getDaysBetween(today)
        return when {
            daysBetween == 0L -> "D-DAY"
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
