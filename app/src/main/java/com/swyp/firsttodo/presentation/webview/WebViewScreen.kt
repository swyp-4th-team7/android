@file:Suppress("COMPOSE_APPLIER_CALL_MISMATCH")

package com.swyp.firsttodo.presentation.webview

import android.annotation.SuppressLint
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.swyp.firsttodo.core.common.component.HaebomTopBar

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewScreen(
    title: String,
    url: String,
    onPopBackStack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        HaebomTopBar(
            title = title,
            onBackClick = onPopBackStack,
        )
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    webViewClient = object : WebViewClient() {
                        override fun shouldOverrideUrlLoading(
                            view: WebView,
                            request: WebResourceRequest,
                        ): Boolean = false
                    }
                    settings.javaScriptEnabled = true
                    loadUrl(url)
                }
            },
            modifier = Modifier.fillMaxSize(),
        )
    }
}
