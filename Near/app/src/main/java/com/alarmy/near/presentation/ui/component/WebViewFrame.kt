package com.alarmy.near.presentation.ui.component

import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    var canGoBack by remember { mutableStateOf(false) }
    var webView: WebView? by remember { mutableStateOf(null) }

    BackHandler(enabled = canGoBack) {
        webView?.goBack()
    }

    NearFrame {
        NearCancelTopAppBar(
            modifier = Modifier.padding(horizontal = 24.dp),
            title = title,
            onCancelClick = onNavigateBack,
        )

        AndroidView(
            modifier = modifier.fillMaxSize(),
            factory = { context ->
                WebView(context).apply {
                    webView = this
                    webViewClient = object : WebViewClient() {
                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            // 페이지 로드 완료 시 뒤로가기 가능 상태 업데이트
                            canGoBack = view?.canGoBack() ?: false
                        }
                    }
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
            update = { view ->
                // WebView 업데이트 시 뒤로가기 가능 상태 동기화
                canGoBack = view.canGoBack()
            }
        )
    }
}
