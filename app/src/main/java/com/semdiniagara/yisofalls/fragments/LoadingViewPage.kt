package com.semdiniagara.yisofalls.fragments

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
import com.onesignal.OneSignal
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import com.semdiniagara.yisofalls.MainApp
import com.semdiniagara.yisofalls.R
import com.semdiniagara.yisofalls.navigation.goTo
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
            val varLinkToVarLet: Pair<String, Boolean>
            runBlocking {

                OneSignal.Notifications.requestPermission(true)

                config.reset().await()
                config.fetchAndActivate().await()
                varLinkToVarLet = config.getString(VAR_LINK) to config.getBoolean(VAR_LET)
            }
            if(varLinkToVarLet.second && varLinkToVarLet.first.isNotEmpty()) {
                Log.i("Firebase check", "Var let is true, var link is ${varLinkToVarLet.first}")
                goTo(WebViewPage())
            }
            else {
                Log.i("Firebase check", "Var let is ${varLinkToVarLet.second}, var link is ${varLinkToVarLet.first}")
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

    companion object {
        const val VAR_LINK = "var_link"
        const val VAR_LET = "var_let"
    }
}