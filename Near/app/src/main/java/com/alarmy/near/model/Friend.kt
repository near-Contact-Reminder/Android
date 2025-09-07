package com.alarmy.near.model

import android.os.Parcelable
import androidx.annotation.StringRes
import com.alarmy.near.R
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Parcelize
@Serializable
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
) : Parcelable {
    val isContactedToday: Boolean
        get() = lastContactAt?.isToday() ?: false

    private fun String.isToday(): Boolean {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.KOREA)
        val targetDate = LocalDate.parse(this, formatter)
        val today = LocalDate.now()
        return targetDate == today
    }
}

@Serializable
@Parcelize
data class ContactFrequency(
    val reminderInterval: ReminderInterval,
    val dayOfWeek: DayOfWeek,
) : Parcelable

@Serializable
@Parcelize
data class Anniversary(
    val id: Int? = null,
    val title: String,
    val date: String? = null,
) : Parcelable

@Serializable
enum class DayOfWeek(
    @param:StringRes val resId: Int,
) {
    MONDAY(R.string.day_of_week_monday),
    TUESDAY(R.string.day_of_week_tuesday),
    WEDNESDAY(R.string.day_of_week_wednesday),
    THURSDAY(R.string.day_of_week_thursday),
    FRIDAY(R.string.day_of_week_friday),
    SATURDAY(R.string.day_of_week_saturday),
    SUNDAY(R.string.day_of_week_sunday),
}
