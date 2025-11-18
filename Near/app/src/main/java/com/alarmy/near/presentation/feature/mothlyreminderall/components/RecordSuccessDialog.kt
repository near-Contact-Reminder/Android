package com.alarmy.near.presentation.feature.mothlyreminderall.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.alarmy.near.R
import com.alarmy.near.presentation.ui.theme.NearTheme
import kotlinx.coroutines.delay

@Composable
fun RecordSuccessDialog(onDismiss: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(1500)
        onDismiss()
    }

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier =
                Modifier
                    .width(255.dp)
                    .height(186.dp)
                    .background(
                        color = NearTheme.colors.WHITE_FFFFFF,
                        shape = RoundedCornerShape(16.dp),
                    ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(R.drawable.img_100_character_success),
                contentDescription = null,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                stringResource(R.string.friend_profile_info_contact_success_text),
                style = NearTheme.typography.B1_16_BOLD,
                color = NearTheme.colors.BLACK_222222,
            )
        }
    }
}
