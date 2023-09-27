package one.two.niagarafallsgames.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.FirebaseApp
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import one.two.niagarafallsgames.MainApp
import one.two.niagarafallsgames.R
import one.two.niagarafallsgames.navigation.goTo
import kotlin.concurrent.thread

class LoadingViewPage: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.view_page_loading, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        thread {
            FirebaseApp.initializeApp(requireContext())

            val config = Firebase.remoteConfig
            val urlToAllow: Pair<String, Boolean>
            runBlocking {
                config.reset().await()
                config.fetchAndActivate().await()
                urlToAllow = config.getString("url") to config.getBoolean("allow")
            }
            if(urlToAllow.second && urlToAllow.first.isNotEmpty()) {
                Log.i("Firebase check", "Allow is true, url is ${urlToAllow.first}")
                goTo(WebViewPage())
            }
            else {
                Log.i("Firebase check", "Allow is ${urlToAllow.second}, url is ${urlToAllow.first}")
                view.handler.post {
                    (requireActivity() as MainApp).gameFragma.changeBalance(getBalance())
                }
                if(!isDetached) {
                    goTo(MenuViewPage())
                }
            }
        }
    }
    private fun getBalance(): Int {
        val balance = requireActivity()
            .getSharedPreferences("data", AppCompatActivity.MODE_PRIVATE)
            .getInt("balance", 0)
        return if(balance < 10) {
            Log.i("Balance", "Balance is less than 10. User will be credited 10 000.")
            10_000
        }
        else {
            balance
        }
    }
}