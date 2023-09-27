package one.two.niagarafallsgames.help

import android.net.Uri
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.activity.result.ActivityResultLauncher
import android.Manifest
import android.util.Log
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
                Log.e("Load uri", uri)
                return !(uri.contains("/") && uri.contains("http"))
            }
        }
        fragmentWebView.webViewClient = webViewClient
    }

    fun setWebChromeClient() {
        fragmentWebView.webChromeClient = FragmentWebChromeClient()
    }

    inner class FragmentWebChromeClient(): WebChromeClient() {
        override fun onShowFileChooser(
            webView: WebView?,
            filePathCallback: ValueCallback<Array<Uri>>?,
            fileChooserParams: FileChooserParams?
        ): Boolean {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            setFileWayCallback(filePathCallback)
            return true
        }
    }
}

/*
        fragmentWebView.webChromeClient = object : WebChromeClient() {
            override fun onShowFileChooser(
                webView: WebView,
                filePathCallback: ValueCallback<Array<Uri>>,
                fileChooserParams: FileChooserParams
            ): Boolean {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                fileWayCallback = filePathCallback
                return true
            }
        }
        fragmentWebView.webViewClient = Client()


WebViewClient:
    inner class Client : WebViewClient() {
        var content: Boolean? = null
        var method: String? = null
        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
            val uri = request.url.toString()
            return if (uri.contains("/")) {
                Log.e("Uri", uri)
                if (uri.contains("http")) {
                    content = false
                    content!!
                } else {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(uri)))
                    true
                }
            } else true
        }
    }
 */