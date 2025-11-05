package com.alarmy.near.presentation.ui.extension

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

internal enum class ShimmerType(
    val colors: List<Color>,
) {
    PRIMARY(
        listOf(
            Color(0xff5997E5),
            Color(0xffA9D8FF),
        ),
    ),
    WHITE(
        listOf(
            Color(0xffFFFFFF),
            Color(0xffEBEBEB),
        ),
    ),
}

@Composable
fun NearConditionalShimmer(
    enabled: Boolean,
    modifier: Modifier = Modifier,
    shimmerColors: List<Color> = ShimmerType.PRIMARY.colors,
    cornerRadius: Dp = 12.dp,
    content: @Composable () -> Unit,
) {
    Box(modifier = modifier) {
        // 항상 content를 렌더링해서 크기 확보 (투명하게)
        Box(
            modifier = Modifier.alpha(if (enabled) 0f else 1f),
        ) {
            content()
        }

        // shimmer가 enabled일 때만 효과 표시
        if (enabled) {
            NearShimmerEffect(
                modifier = Modifier.matchParentSize(),
                shimmerColors = shimmerColors,
                cornerRadius = cornerRadius,
            )
        }
    }
}

@Composable
private fun NearShimmerEffect(
    modifier: Modifier = Modifier,
    shimmerColors: List<Color> = ShimmerType.PRIMARY.colors,
    cornerRadius: Dp = 12.dp,
) {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim =
        transition.animateFloat(
            initialValue = 0f,
            targetValue = 1000f,
            animationSpec =
                infiniteRepeatable(
                    animation = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse,
                ),
            label = "shimmer_translate",
        )

    val brush =
        remember(translateAnim.value) {
            Brush.linearGradient(
                colors = shimmerColors,
                start = Offset.Zero,
                end = Offset(translateAnim.value, translateAnim.value),
            )
        }

    Box(
        modifier =
            modifier
                .clip(RoundedCornerShape(cornerRadius))
                .background(brush),
    )
}
