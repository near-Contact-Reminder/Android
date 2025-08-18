package com.alarmy.near.presentation.ui.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.PlatformParagraphStyle
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alarmy.near.R

@Suppress("PropertyName")
@Immutable
data class NearTypography(
    val H1_24_BOLD: TextStyle,
    val H1_24_MEDIUM: TextStyle,
    val H1_24_REGULAR: TextStyle,
    val H2_18_BOLD: TextStyle,
    val B1_16_BOLD: TextStyle,
    val B1_16_MEDIUM: TextStyle,
    val B2_14_BOLD: TextStyle,
    val B2_14_MEDIUM: TextStyle,
    val FC_12_BOLD: TextStyle,
    val FC_12_MEDIUM: TextStyle,
)

val Pretendard =
    FontFamily(
        Font(
            resId = R.font.pretendard_bold,
            weight = FontWeight.Bold,
        ),
        Font(
            resId = R.font.pretendard_regular,
            weight = FontWeight.Normal,
        ),
        Font(
            resId = R.font.pretendard_extra_bold,
            weight = FontWeight.ExtraBold,
        ),
        Font(
            resId = R.font.pretendard_extra_light,
            weight = FontWeight.ExtraLight,
        ),
        Font(
            resId = R.font.pretendard_extra_light,
            weight = FontWeight.Light,
        ),
        Font(
            resId = R.font.pretendard_medium,
            weight = FontWeight.Medium,
        ),
        Font(
            resId = R.font.pretendard_semi_bold,
            weight = FontWeight.SemiBold,
        ),
        Font(
            resId = R.font.pretendard_thin,
            weight = FontWeight.Thin,
        ),
    )

internal val Typography =
    NearTypography(
        H1_24_BOLD =
            TextStyle(
                fontFamily = Pretendard,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                lineHeight = 34.sp,
                letterSpacing =
                    letterSpacingToSp(
                        fontSize = 24.sp,
                        letterSpacingPercent = -0.25f,
                    ),
                lineHeightStyle =
                    LineHeightStyle(
                        alignment = LineHeightStyle.Alignment.Center,
                        trim = LineHeightStyle.Trim.None,
                    ),
            ),
        H1_24_MEDIUM =
            TextStyle(
                fontFamily = Pretendard,
                fontWeight = FontWeight.Medium,
                fontSize = 24.sp,
                lineHeight = 34.sp,
                letterSpacing =
                    letterSpacingToSp(
                        fontSize = 24.sp,
                        letterSpacingPercent = -0.25f,
                    ),
                lineHeightStyle =
                    LineHeightStyle(
                        alignment = LineHeightStyle.Alignment.Center,
                        trim = LineHeightStyle.Trim.None,
                    ),
            ),
        H1_24_REGULAR =
            TextStyle(
                fontFamily = Pretendard,
                fontWeight = FontWeight.Normal,
                fontSize = 24.sp,
                lineHeight = 34.sp,
                letterSpacing =
                    letterSpacingToSp(
                        fontSize = 24.sp,
                        letterSpacingPercent = -0.25f,
                    ),
                lineHeightStyle =
                    LineHeightStyle(
                        alignment = LineHeightStyle.Alignment.Center,
                        trim = LineHeightStyle.Trim.None,
                    ),
            ),
        H2_18_BOLD =
            TextStyle(
                fontFamily = Pretendard,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                lineHeight = 24.sp,
                letterSpacing =
                    letterSpacingToSp(
                        fontSize = 18.sp,
                        letterSpacingPercent = -0.25f,
                    ),
                lineHeightStyle =
                    LineHeightStyle(
                        alignment = LineHeightStyle.Alignment.Center,
                        trim = LineHeightStyle.Trim.None,
                    ),
            ),
        B1_16_BOLD =
            TextStyle(
                fontFamily = Pretendard,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                lineHeight = 22.sp,
                letterSpacing =
                    letterSpacingToSp(
                        fontSize = 16.sp,
                        letterSpacingPercent = -0.25f,
                    ),
                lineHeightStyle =
                    LineHeightStyle(
                        alignment = LineHeightStyle.Alignment.Center,
                        trim = LineHeightStyle.Trim.None,
                    ),
            ),
        B1_16_MEDIUM =
            TextStyle(
                fontFamily = Pretendard,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                lineHeight = 22.sp,
                letterSpacing =
                    letterSpacingToSp(
                        fontSize = 16.sp,
                        letterSpacingPercent = -0.25f,
                    ),
                lineHeightStyle =
                    LineHeightStyle(
                        alignment = LineHeightStyle.Alignment.Center,
                        trim = LineHeightStyle.Trim.None,
                    ),
            ),
        B2_14_BOLD =
            TextStyle(
                fontFamily = Pretendard,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                letterSpacing =
                    letterSpacingToSp(
                        fontSize = 14.sp,
                        letterSpacingPercent = -0.25f,
                    ),
                lineHeightStyle =
                    LineHeightStyle(
                        alignment = LineHeightStyle.Alignment.Center,
                        trim = LineHeightStyle.Trim.None,
                    ),
            ),
        B2_14_MEDIUM =
            TextStyle(
                fontFamily = Pretendard,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                letterSpacing =
                    letterSpacingToSp(
                        fontSize = 14.sp,
                        letterSpacingPercent = -0.25f,
                    ),
                lineHeightStyle =
                    LineHeightStyle(
                        alignment = LineHeightStyle.Alignment.Center,
                        trim = LineHeightStyle.Trim.None,
                    ),
            ),
        FC_12_BOLD =
            TextStyle(
                fontFamily = Pretendard,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                lineHeight = 18.sp,
                letterSpacing =
                    letterSpacingToSp(
                        fontSize = 12.sp,
                        letterSpacingPercent = -0.25f,
                    ),
                lineHeightStyle =
                    LineHeightStyle(
                        alignment = LineHeightStyle.Alignment.Center,
                        trim = LineHeightStyle.Trim.None,
                    ),
            ),
        FC_12_MEDIUM =
            TextStyle(
                fontFamily = Pretendard,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                lineHeight = 18.sp,
                letterSpacing =
                    letterSpacingToSp(
                        fontSize = 12.sp,
                        letterSpacingPercent = -0.25f,
                    ),
                lineHeightStyle =
                    LineHeightStyle(
                        alignment = LineHeightStyle.Alignment.Center,
                        trim = LineHeightStyle.Trim.None,
                    ),
            ),
    )

@Preview(showBackground = true)
@Composable
private fun TypographyPreview() {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp)) {
        Text("소중한 사람들과 더 가까워지는 시간 24", style = Typography.H1_24_BOLD)
        Text("소중한 사람들과 더 가까워지는 시간 24", style = Typography.H1_24_MEDIUM)
        Text("소중한 사람들과 더 가까워지는 시간 24", style = Typography.H1_24_REGULAR)
        Text("소중한 사람들과 더 가까워지는 시간 18", style = Typography.H2_18_BOLD)
        Text("소중한 사람들과 더 가까워지는 시간 16", style = Typography.B1_16_BOLD)
        Text("소중한 사람들과 더 가까워지는 시간 16", style = Typography.B1_16_MEDIUM)
        Text("소중한 사람들과 더 가까워지는 시간 14", style = Typography.B2_14_BOLD)
        Text("소중한 사람들과 더 가까워지는 시간 14", style = Typography.B2_14_MEDIUM)
        Text("소중한 사람들과 더 가까워지는 시간 12", style = Typography.FC_12_BOLD)
        Text("소중한 사람들과 더 가까워지는 시간 12", style = Typography.FC_12_MEDIUM)
    }
}

/**
 * Converts Figma letterSpacing (%) to Android Compose TextUnit (sp).
 *
 * @param fontSize Font size in sp.
 * @param letterSpacingPercent Letter spacing in percent (e.g., -0.25 for -0.25%)
 * @return Converted letterSpacing in sp
 */
private fun letterSpacingToSp(
    fontSize: TextUnit,
    letterSpacingPercent: Float,
): TextUnit = (fontSize.value * (letterSpacingPercent / 100f)).sp
