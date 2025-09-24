package com.alarmy.near.utils.extensions

import java.util.Calendar
import java.util.Locale

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
}
