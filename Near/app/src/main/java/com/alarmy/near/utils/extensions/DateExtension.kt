package com.alarmy.near.utils.extensions

import com.alarmy.near.model.DayOfWeek
import com.alarmy.near.model.ReminderInterval
import java.util.Calendar

/**
 * Date 관련 유틸리티 확장 함수들
 */
object DateExtension {
    // 요일 상수 정의
    private val DAY_OF_WEEK_KOREAN_FULL =
        mapOf(
            Calendar.SUNDAY to "일요일",
            Calendar.MONDAY to "월요일",
            Calendar.TUESDAY to "화요일",
            Calendar.WEDNESDAY to "수요일",
            Calendar.THURSDAY to "목요일",
            Calendar.FRIDAY to "금요일",
            Calendar.SATURDAY to "토요일",
        )

    // Calendar.DAY_OF_WEEK를 한글 요일로 변환하는 공통 함수
    // isFull로 "월요일"을 반환할지, '월' 을 반환할지 결정합니다.
    private fun convertDayOfWeekToKorean(
        dayOfWeek: Int,
        isFull: Boolean = true,
    ): String {
        val fullDay = DAY_OF_WEEK_KOREAN_FULL[dayOfWeek] ?: "알 수 없음"
        return if (isFull) fullDay else fullDay.first().toString()
    }

    // 오늘 요일을 한글로 반환
    fun getTodayDayOfWeekInKorean(): String {
        val calendar = Calendar.getInstance()
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        return convertDayOfWeekToKorean(dayOfWeek, isFull = true)
    }

    // 특정 날짜의 요일을 한글로 반환
    fun getDayOfWeekInKorean(
        year: Int,
        month: Int,
        day: Int,
    ): String {
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, day) // Calendar의 월은 0부터 시작
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        return convertDayOfWeekToKorean(dayOfWeek, isFull = true)
    }

    // 다음 주 같은 요일의 날짜를 반환
    fun getNextWeekSameDay(): String {
        val calendar = Calendar.getInstance()

        // 다음 주 같은 요일로 이동
        calendar.add(Calendar.WEEK_OF_YEAR, 1)

        val month = calendar.get(Calendar.MONTH) + 1 // Calendar의 월은 0부터 시작
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

        val dayOfWeekKorean = convertDayOfWeekToKorean(dayOfWeek, isFull = false)

        return "$month/$day $dayOfWeekKorean"
    }

    // 선택된 주기에 따른 다음 날짜를 반환
    fun getNextCycleDate(reminderInterval: ReminderInterval): String {
        val calendar = Calendar.getInstance()

        when (reminderInterval) {
            ReminderInterval.EVERY_DAY -> {
                // 매일: 다음 날
                calendar.add(Calendar.DAY_OF_MONTH, 1)
            }

            ReminderInterval.EVERY_WEEK -> {
                // 매주: 다음 주 같은 요일
                calendar.add(Calendar.WEEK_OF_YEAR, 1)
            }

            ReminderInterval.EVERY_TWO_WEEK -> {
                // 격주: 2주 후 같은 요일
                calendar.add(Calendar.WEEK_OF_YEAR, 2)
            }

            ReminderInterval.EVERY_MONTH -> {
                // 매월: 다음 달 같은 날
                calendar.add(Calendar.MONTH, 1)
            }

            ReminderInterval.EVERY_SIX_MONTH -> {
                // 반년: 6개월 후 같은 날
                calendar.add(Calendar.MONTH, 6)
            }
        }

        val month = calendar.get(Calendar.MONTH) + 1 // Calendar의 월은 0부터 시작
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

        val dayOfWeekKorean = convertDayOfWeekToKorean(dayOfWeek, isFull = false)

        return "$month/$day $dayOfWeekKorean"
    }

    // 선택된 주기를 사용자 친화적인 텍스트로 변환
    fun getCycleText(reminderInterval: ReminderInterval): String {
        val todayDayOfWeek = getTodayDayOfWeekInKorean()

        return when (reminderInterval) {
            ReminderInterval.EVERY_DAY -> "매일"
            ReminderInterval.EVERY_WEEK -> "매주 $todayDayOfWeek"
            ReminderInterval.EVERY_TWO_WEEK -> "2주마다 $todayDayOfWeek"
            ReminderInterval.EVERY_MONTH -> {
                val calendar = Calendar.getInstance()
                val day = calendar.get(Calendar.DAY_OF_MONTH)
                "매달 ${day}일"
            }

            ReminderInterval.EVERY_SIX_MONTH -> {
                val calendar = Calendar.getInstance()
                val month = calendar.get(Calendar.MONTH) + 1 // Calendar의 월은 0부터 시작
                val day = calendar.get(Calendar.DAY_OF_MONTH)
                // 6개월 후의 월을 계산
                val nextSixMonthCalendar =
                    (calendar.clone() as Calendar).apply {
                        add(Calendar.MONTH, 6)
                    }
                val nextSixMonth = nextSixMonthCalendar.get(Calendar.MONTH) + 1
                "매년 $month/$day, $nextSixMonth/$day"
            }
        }
    }

    // ReminderInterval을 contactWeek 문자열로 변환
    fun toContactWeekString(reminderInterval: ReminderInterval): String = reminderInterval.name

    // 오늘 날짜의 일(day)을 반환해서 반복 주기 표기에 사용한다.
    fun getTodayDayOfMonth(): Int {
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.DAY_OF_MONTH)
    }

    // 오늘 요일을 DayOfWeek enum으로 반환
    fun getTodayDayOfWeek(): DayOfWeek {
        val calendar = Calendar.getInstance()
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        return when (dayOfWeek) {
            Calendar.SUNDAY -> DayOfWeek.SUNDAY
            Calendar.MONDAY -> DayOfWeek.MONDAY
            Calendar.TUESDAY -> DayOfWeek.TUESDAY
            Calendar.WEDNESDAY -> DayOfWeek.WEDNESDAY
            Calendar.THURSDAY -> DayOfWeek.THURSDAY
            Calendar.FRIDAY -> DayOfWeek.FRIDAY
            Calendar.SATURDAY -> DayOfWeek.SATURDAY
            else -> throw IllegalStateException("Invalid day of week: $dayOfWeek")
        }
    }

    // 오늘 요일을 API 요청용 영어 문자열로 반환
    fun getTodayDayOfWeekInEnglish(): String {
        val calendar = Calendar.getInstance()
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        return when (dayOfWeek) {
            Calendar.SUNDAY -> "SUNDAY"
            Calendar.MONDAY -> "MONDAY"
            Calendar.TUESDAY -> "TUESDAY"
            Calendar.WEDNESDAY -> "WEDNESDAY"
            Calendar.THURSDAY -> "THURSDAY"
            Calendar.FRIDAY -> "FRIDAY"
            Calendar.SATURDAY -> "SATURDAY"
            else -> throw IllegalStateException("Invalid day of week: $dayOfWeek")
        }
    }
}
