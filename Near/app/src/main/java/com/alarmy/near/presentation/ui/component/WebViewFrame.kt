package com.alarmy.near.presentation.ui.component

import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.alarmy.near.presentation.ui.component.appbar.NearCancelTopAppBar

@Composable
fun WebViewFrame(
    onNavigateBack: () -> Unit,
    title: String,
    url: String,
    modifier: Modifier = Modifier,
) {
    NearFrame {
        NearCancelTopAppBar(
            modifier = Modifier.padding(horizontal = 24.dp),
            title = title,
            onCancelClick = onNavigateBack,
        )

        AndroidView(
            modifier =
                modifier
                    .fillMaxSize(),
            factory = { context ->
                WebView(context).apply {
                    webViewClient = WebViewClient()
                    settings.apply {
                        domStorageEnabled = true
                        mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                        loadWithOverviewMode = true
                        useWideViewPort = true
                        builtInZoomControls = true
                        displayZoomControls = false
                    }
                    loadUrl(url)
                }
            },
        )
    }
}
