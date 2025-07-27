package com.alarmy.near.presentation.ui.component.button

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alarmy.near.presentation.ui.theme.NearTheme

@Composable
fun NearBasicButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit,
    contentPadding: PaddingValues,
) {
    Button(
        modifier =
            modifier
                .heightIn(min = 56.dp)
                .wrapContentHeight(),
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        content = content,
        contentPadding = contentPadding,
    )
}

@Preview(widthDp = 360, heightDp = 70, showBackground = true)
@Composable
fun NearBasicButtonPreview() {
    Surface {
        NearBasicButton(
            modifier = Modifier.padding(horizontal = 20.dp),
            content = { Text("Button", style = NearTheme.typography.B1_16_BOLD) },
            onClick = {},
            contentPadding = PaddingValues(vertical = 17.dp),
        )
    }
}
