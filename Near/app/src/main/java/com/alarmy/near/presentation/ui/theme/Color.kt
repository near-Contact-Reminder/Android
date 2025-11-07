@file:Suppress("SpellCheckingInspection")

package com.alarmy.near.presentation.ui.theme

import androidx.compose.ui.graphics.Color

object NearColorPallete {
    val BLACK_1A1A1A = Color(0xFF1A1A1A)
    val BLACK_222222 = Color(0xFF222222)
    val WHITE_FFFFFF = Color(0xFFFFFFFF)
    val GRAY01_888888 = Color(0xFF888888)
    val GRAY02_B7B7B7 = Color(0xFFB7B7B7)
    val GRAY03_EBEBEB = Color(0xFFEBEBEB)
    val GRAY04_F7F7F7 = Color(0xFFF7F7F7)
    val BLUE01_5AA2E9 = Color(0xFF5AA2E9)
    val BLUE02_8ACCFF = Color(0xFF8ACCFF)
    val BLUE03_58ABEC = Color(0xFF58ABEC)
    val BG01_E3F0F9 = Color(0xFFE3F0F9)
    val BG02_F4F9FD = Color(0xFFF4F9FD)
    val NEGATIVE_F04E4E = Color(0xFFF04E4E)
    val DIM_000000 = Color(0x99000000)

    val PURPLE01_4E3EC7 = Color(0xFF4E3EC7)
}

@Suppress("PropertyName")
data class NearColor(
    val BLACK_1A1A1A: Color = NearColorPallete.BLACK_1A1A1A,
    val BLACK_222222: Color = NearColorPallete.BLACK_222222,
    val WHITE_FFFFFF: Color = NearColorPallete.WHITE_FFFFFF,
    val GRAY01_888888: Color = NearColorPallete.GRAY01_888888,
    val GRAY02_B7B7B7: Color = NearColorPallete.GRAY02_B7B7B7,
    val GRAY03_EBEBEB: Color = NearColorPallete.GRAY03_EBEBEB,
    val GRAY04_F7F7F7: Color = NearColorPallete.GRAY04_F7F7F7,
    val BLUE01_5AA2E9: Color = NearColorPallete.BLUE01_5AA2E9,
    val BLUE02_8ACCFF: Color = NearColorPallete.BLUE02_8ACCFF,
    val BG01_E3F0F9: Color = NearColorPallete.BG01_E3F0F9,
    val BG02_F4F9FD: Color = NearColorPallete.BG02_F4F9FD,
    val NEGATIVE_F04E4E: Color = NearColorPallete.NEGATIVE_F04E4E,
    val DIM_000000: Color = NearColorPallete.DIM_000000,
    // 온보딩 배경 장식 색상
    val BLUE03_58ABEC: Color = NearColorPallete.BLUE03_58ABEC,
    val PURPLE01_4E3EC7: Color = NearColorPallete.PURPLE01_4E3EC7,
)

val darkColor = NearColor()
val lightColor = NearColor()
