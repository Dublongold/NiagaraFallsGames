package com.semdiniagara.yisofalls

import android.os.Bundle
import android.view.WindowManager
import android.webkit.WebView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.onesignal.OneSignal
import com.semdiniagara.yisofalls.fragments.LoadingViewPage
import com.semdiniagara.yisofalls.fragments.MenuViewPage
import com.semdiniagara.yisofalls.fragments.WebViewPage
import com.semdiniagara.yisofalls.fragments.fragmas.GameFragma
import com.semdiniagara.yisofalls.navigation.goTo


class MainApp : AppCompatActivity() {
    val gameFragma = GameFragma()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
        )
        setContentView(R.layout.app_main)

        onBackPressedDispatcher.addCallback(object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val fragment = supportFragmentManager.fragments.lastOrNull()
                if(fragment != null) {
                    if(fragment is WebViewPage) {
                        val webView = fragment.view?.findViewById<WebView>(R.id.fragmentWebView)
                        if(webView != null && webView.canGoBack()) {
                            webView.goBack()
                        }
                        return
                    }
                    else if(fragment !is MenuViewPage) {
                        fragment.goTo(MenuViewPage())
                        return
                    }
                }
                finish()
            }
        })

        OneSignal.initWithContext(applicationContext, getString(R.string.one_s))

        supportFragmentManager.beginTransaction()
            .add(R.id.mainFragmentContainer, LoadingViewPage())
            .commit()
    }
}