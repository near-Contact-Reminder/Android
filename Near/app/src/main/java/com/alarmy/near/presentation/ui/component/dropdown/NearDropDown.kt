package com.alarmy.near.presentation.ui.component.dropdown

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alarmy.near.presentation.ui.theme.NearTheme

@Composable
fun NearDropdownMenu(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    DropdownMenu(
        modifier = modifier.background(NearTheme.colors.WHITE_FFFFFF),
        expanded = expanded,
        shape = RoundedCornerShape(12.dp),
        onDismissRequest = onDismissRequest,
        content = content,
    )
}

@Composable
fun NearDropdownMenuItem(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    DropdownMenuItem(
        modifier = modifier,
        onClick = onClick,
        text = {
            Text(
                text = text,
                style = NearTheme.typography.B2_14_MEDIUM,
                color = NearTheme.colors.BLACK_1A1A1A,
            )
        },
    )
}

@Preview(showBackground = true)
@Composable
fun NearDropdownMenuPreview() {
    var expanded by remember { mutableStateOf(true) }

    NearTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
        ) {
            NearDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                NearDropdownMenuItem(
                    text = "친구 정보 수정",
                    onClick = { expanded = false }
                )
                NearDropdownMenuItem(
                    text = "친구 삭제",
                    onClick = { expanded = false }
                )
            }
        }
    }
}
