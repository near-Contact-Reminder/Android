package com.alarmy.near.presentation.feature.onboarding.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.alarmy.near.presentation.ui.theme.NearTheme

/**
 * 배경 장식 컴포넌트
 * 원형 블러 배경 장식을 제공하는 컴포넌트
 */
@Composable
fun BackgroundArea() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        BackgroundEllipse(
            modifier = Modifier.offset(x = (-78).dp),
        )

        BackgroundEllipse(
            modifier = Modifier.offset(x = 201.dp, y = 155.dp),
            opacity = 0.2f,
            color = Color(0xFF4E3EC7),
        )
    }
}
@Composable
fun BackgroundEllipse(
    modifier: Modifier = Modifier,
    color: Color = Color(0xFF58ABEC),
    blurRadius: Float = 200f,
    size: Dp = 251.dp,
    opacity: Float = 0.3f,
) {
    Box(
        modifier = modifier
            .size(size)
            .graphicsLayer {
                renderEffect = BlurEffect(
                    radiusX = blurRadius,
                    radiusY = blurRadius,
                    edgeTreatment = TileMode.Clamp,
                )
            }
            .alpha(opacity)
            .background(
                color = color,
                shape = CircleShape
            )
    )
}

@Preview(showBackground = true)
@Composable
fun BackgroundAreaPreview() {
    NearTheme {
        BackgroundArea()
    }
}

@Preview()
@Composable
fun BackgroundDecorationPreview() {
    NearTheme {
        BackgroundEllipse()
    }
}

