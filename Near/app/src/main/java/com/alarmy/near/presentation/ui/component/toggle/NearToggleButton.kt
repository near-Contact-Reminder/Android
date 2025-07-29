package com.alarmy.near.presentation.ui.component.toggle

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.alarmy.near.presentation.ui.theme.NearTheme

@Composable
fun NearToggleButton(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    NearCustomSwitch(
        isChecked = checked,
        onCheckedChange,
        scale = 1f,
        width = 48.dp,
        height = 26.dp,
        strokeWidth = 0.dp,
        checkedTrackColor = NearTheme.colors.BLUE02_8ACCFF,
        uncheckedTrackColor = Color(0xffDEDEDE),
    )
}

@Composable
private fun NearCustomSwitch(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    scale: Float = 2f,
    width: Dp = 36.dp,
    height: Dp = 20.dp,
    strokeWidth: Dp = 2.dp,
    checkedTrackColor: Color = Color(0xFF35898F),
    uncheckedTrackColor: Color = Color(0xFFe0e0e0),
    gapBetweenThumbAndTrackEdge: Dp = 4.dp,
) {
    val thumbRadius = (height / 2) - gapBetweenThumbAndTrackEdge

    // To move thumb, we need to calculate the position (along x axis)
    val animatePosition =
        animateFloatAsState(
            targetValue =
                if (isChecked) {
                    with(LocalDensity.current) { (width - thumbRadius - gapBetweenThumbAndTrackEdge).toPx() }
                } else {
                    with(LocalDensity.current) { (thumbRadius + gapBetweenThumbAndTrackEdge).toPx() }
                },
        )

    Canvas(
        modifier =
            Modifier
                .size(width = width, height = height)
                .scale(scale = scale)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            // This is called when the user taps on the canvas
                            onCheckedChange(!isChecked)
                        },
                    )
                },
    ) {
        // Track
        drawRoundRect(
            color = if (isChecked) checkedTrackColor else uncheckedTrackColor,
            cornerRadius = CornerRadius(x = 13.dp.toPx(), y = 13.dp.toPx()),
        )

        // Thumb
        drawCircle(
            color = Color(0xffffffff),
            radius = thumbRadius.toPx(),
            center =
                Offset(
                    x = animatePosition.value,
                    y = size.height / 2,
                ),
        )
    }
}

@Preview(widthDp = 360, heightDp = 70, showBackground = true)
@Composable
fun NearToggleButtonPreview() {
    Surface {
        Row {
            NearToggleButton(
                checked = true,
                onCheckedChange = {},
            )
            Spacer(modifier = Modifier.width(50.dp))
            NearToggleButton(
                checked = false,
                onCheckedChange = {},
            )
        }
    }
}
