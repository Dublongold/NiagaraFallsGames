package one.two.niagarafallsgames

import android.os.Bundle
import android.view.WindowManager
import android.webkit.WebView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import one.two.niagarafallsgames.fragments.LoadingViewPage
import one.two.niagarafallsgames.fragments.MenuViewPage
import one.two.niagarafallsgames.fragments.WebViewPage
import one.two.niagarafallsgames.fragments.fragmas.GameFragma
import one.two.niagarafallsgames.navigation.goTo


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

        supportFragmentManager.beginTransaction()
            .add(R.id.mainFragmentContainer, LoadingViewPage())
            .commit()
    }
}