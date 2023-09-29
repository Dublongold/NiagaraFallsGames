package com.semdiniagara.yisofalls.fragments

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.semdiniagara.yisofalls.MainApp
import com.semdiniagara.yisofalls.R
import com.semdiniagara.yisofalls.fragments.fragmas.GameFragma
import com.semdiniagara.yisofalls.navigation.goTo

abstract class GameViewPage: Fragment() {
    protected lateinit var gameFragma: GameFragma

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        gameFragma = (requireActivity() as MainApp).gameFragma
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED

        super.onViewCreated(view, savedInstanceState)
        view.findViewById<ImageButton>(R.id.backButton).setOnClickListener {
            goTo(MenuViewPage())
        }

        gameFragma.bet.observe(viewLifecycleOwner) {
            view.findViewById<TextView?>(R.id.betInfo)?.text = getString(R.string.bet_info_bar, it)
        }
        gameFragma.balance.observe(viewLifecycleOwner) {
            view.findViewById<TextView?>(R.id.balanceInfo)?.text = getString(R.string.balance_info_bar, it)
        }
        val autoSpin = view.findViewById<AppCompatButton>(R.id.autoSpinButton)
        val stop = view.findViewById<AppCompatButton>(R.id.stopButton)
        gameFragma.isAutoSpinMode.observe(viewLifecycleOwner) {
            autoSpin.isEnabled = !it
            stop.isEnabled = it
        }
        autoSpin.setOnClickListener {
            gameFragma.onAutoSpinMode()
        }
        stop.setOnClickListener {
            gameFragma.offAutoSpinMode()
        }
        view.findViewById<ImageButton>(R.id.plusButton).setOnClickListener {
            gameFragma.increaseBet()
        }
        view.findViewById<ImageButton>(R.id.minusButton).setOnClickListener {
            gameFragma.decreaseBet()
        }
    }

    override fun onDetach() {
        super.onDetach()
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED

    }

    protected fun saveBalance(value: Int) {
        requireActivity()
            .getSharedPreferences("data", AppCompatActivity.MODE_PRIVATE)
            .edit()
            .putInt("balance", if(value < 10) 10_000 else value)
            .apply()
    }
}