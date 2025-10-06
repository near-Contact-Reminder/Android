package com.alarmy.near.utils

import android.content.Context
import io.michaelrocks.libphonenumber.android.NumberParseException
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil

/**
 * 전화번호 포맷팅을 위한 유틸리티 object
 * libphonenumber-android를 사용하여 다양한 전화번호 형식을 처리합니다.
 */
object PhoneNumberFormatter {
    private var phoneNumberUtil: PhoneNumberUtil? = null

    /**
     * Context를 사용하여 PhoneNumberUtil을 초기화합니다.
     * Application Context에서 한 번만 초기화하면 됩니다.
     * 사용시 초기화 할 수 있지만 현재 Repository에서 사용하고 있기 때문에 Application에서 초기화를 진행합니다
     */
    fun initialize(context: Context) {
        if (phoneNumberUtil == null) {
            phoneNumberUtil = PhoneNumberUtil.createInstance(context)
        }
    }

    /**
     * 전화번호를 한국 형식으로 포맷팅합니다.
     * - 다양한 전화번호 형식 지원 (휴대폰, 지역번호 등)
     * - 한국 전화번호 형식으로 통일 (010-0000-0000, 02-0000-0000 등)
     * - 잘못된 형식의 경우 원본 반환
     */
    fun formatPhoneNumber(phoneNumber: String): String {
        val util = phoneNumberUtil ?: return phoneNumber

        return try {
            // 불필요한 문자들 제거
            val cleanedNumber =
                phoneNumber
                    .replace("//", "") // // 제거
                    .replace("-", "") // 하이픈 제거
                    .replace(" ", "") // 공백 제거
                    .replace("(", "") // 괄호 제거
                    .replace(")", "") // 괄호 제거
                    .trim()

            // 한국 국가 코드로 파싱 시도
            val parsedNumber = util.parse(cleanedNumber, "KR")

            // 유효한 전화번호인지 확인
            if (util.isValidNumber(parsedNumber)) {
                // 한국 형식으로 포맷팅
                util.format(parsedNumber, PhoneNumberUtil.PhoneNumberFormat.NATIONAL)
            } else {
                // 유효하지 않은 경우 원본 반환
                phoneNumber
            }
        } catch (e: NumberParseException) {
            // 파싱 실패 시 원본 반환
            phoneNumber
        } catch (e: Exception) {
            // 기타 예외 발생 시 원본 반환
            phoneNumber
        }
    }
}
