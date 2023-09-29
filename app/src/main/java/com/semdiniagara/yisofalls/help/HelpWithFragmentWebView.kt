package com.semdiniagara.yisofalls.help

import android.webkit.CookieManager
import android.webkit.WebSettings
import android.webkit.WebView

class HelpWithFragmentWebView1(private val fragmentWebViewSettings: WebSettings) {
    operator fun get(key: Boolean) {
        fragmentWebViewSettings.apply {
            fragmentWebViewSettings.allowFileAccessFromFileURLs = key
            fragmentWebViewSettings.userAgentString = fragmentWebViewSettings.userAgentString.replace("; wv", "")
            fragmentWebViewSettings.allowContentAccess = key
            fragmentWebViewSettings.allowUniversalAccessFromFileURLs = key
        }
    }
}

class HelpWithFragmentWebView2(private val fragmentWebViewSettings: WebSettings) {
    operator fun get(key: Boolean) {
        fragmentWebViewSettings.apply {
            fragmentWebViewSettings.javaScriptCanOpenWindowsAutomatically = key
            fragmentWebViewSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            fragmentWebViewSettings.allowFileAccess = true
            fragmentWebViewSettings.cacheMode = WebSettings.LOAD_DEFAULT
        }
    }
}

class HelpWithFragmentWebView3(
    private val fragmentWebViewSettings: WebSettings,
    private val fragmentWebView: WebView
) {
    operator fun get(key: Boolean) {
        fragmentWebViewSettings.loadWithOverviewMode = key
        val cookie = CookieManager.getInstance()
        cookie.setAcceptThirdPartyCookies(fragmentWebView, key)
        fragmentWebViewSettings.useWideViewPort = key
        cookie.setAcceptCookie(key)
    }
}

class HelpWithFragmentWebView4(
    private val fragmentWebViewSettings: WebSettings
) {
    operator fun get(key: Boolean) {
        fragmentWebViewSettings.apply {
            domStorageEnabled = key
            javaScriptEnabled = domStorageEnabled
            databaseEnabled = javaScriptEnabled
        }
    }
}