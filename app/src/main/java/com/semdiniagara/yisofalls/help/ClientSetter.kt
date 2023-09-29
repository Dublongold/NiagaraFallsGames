package com.semdiniagara.yisofalls.help

import android.net.Uri
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.activity.result.ActivityResultLauncher
import android.Manifest
import android.util.Log
import android.view.KeyEvent
import android.webkit.JsResult
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebViewClient

class ClientSetter(private val fragmentWebView: WebView) {

    lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    lateinit var setFileWayCallback: (ValueCallback<Array<Uri>>?) -> Unit

    fun setWebViewClient() {
        val webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest
            ): Boolean {
                val uri = request.url.toString()
                Log.i("Uri loading", "I go right now to \"$uri\".")
                return !(uri.contains("/") && uri.contains("http"))
            }

            override fun onUnhandledKeyEvent(view: WebView?, event: KeyEvent?) {
                super.onUnhandledKeyEvent(view, event)
                val tag = "Unhandled key event"
                if(event != null) {
                    if(event.isCtrlPressed) {
                        Log.w(tag, "Unhandled ctrl pressed.")
                    }
                    else {
                        Log.w(tag, "Unhandled ${event.keyCode} key code event.")
                    }
                }
                else {
                    Log.wtf(tag, "Unhandled null key event?")
                }
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                Log.w("Received error", "Something wrong. view is null? " +
                        "${view == null}. request is null? ${request == null}. " +
                        "Error: ${error?.errorCode}")
            }
        }
        fragmentWebView.webViewClient = webViewClient
    }

    fun setWebChromeClient() {
        fragmentWebView.webChromeClient = FragmentWebChromeClient()
    }

    inner class FragmentWebChromeClient(): WebChromeClient() {
        private var jsAlertedTimes = 0
        override fun onShowFileChooser(
            webView: WebView?,
            filePathCallback: ValueCallback<Array<Uri>>?,
            fileChooserParams: FileChooserParams?
        ): Boolean {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            setFileWayCallback(filePathCallback)
            return true
        }

        override fun onJsAlert(
            view: WebView?,
            url: String?,
            message: String?,
            result: JsResult?
        ): Boolean {
            Log.i("Js alert", "Js alerted ${++jsAlertedTimes} times.")
            return super.onJsAlert(view, url, message, result)
        }


        override fun onHideCustomView() {
            OnCustomViewHide()
            super.onHideCustomView()
        }
    }

    class OnCustomViewHide {
        init {
            val tag = "Custom view"
            val msg = "Hide."
            Log.i(tag, msg)

        }
    }
}