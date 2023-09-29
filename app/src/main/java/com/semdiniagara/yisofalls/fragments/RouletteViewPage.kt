package com.semdiniagara.yisofalls.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.semdiniagara.yisofalls.R
import kotlin.concurrent.thread

class RouletteViewPage: GameViewPage(), CanDoSpin {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.view_page_roulette_landscape, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<ImageButton>(R.id.spinButton).setOnClickListener {
            it.isEnabled = false
            CoroutineScope(Dispatchers.Main).launch {
                spin()
                it.isEnabled = true
            }
        }
    }

    override suspend fun spin() {
        val roulette = view?.findViewById<ImageView>(R.id.rouletteSections)
        if(roulette != null) {
            var doAgain: Boolean
            do {
                doAgain = false
                val result = gameFragma.decreaseBalanceByBet()
                if (result) {
                    val bet = gameFragma.bet.value ?: 0
                    val balance = gameFragma.balance.value ?: 0
                    var stop = false
                    thread {
                        Thread.sleep(3000 + kotlin.random.Random.nextLong(0, 2000))
                        stop = true
                    }
                    while (!stop) {
                        roulette.rotation += 10
                        if (roulette.rotation >= 360) {
                            roulette.rotation -= 360
                        }
                        delay(100)
                    }
                    while (roulette.rotation.toInt() !in rouletteRotations.keys) {
                        roulette.rotation += 1
                        if (roulette.rotation >= 360) {
                            roulette.rotation -= 360
                        }
                        delay(25)
                    }
                    val win = rouletteRotations[(roulette.rotation).toInt()]
                    if (win != null && win != -1.0) {
                        gameFragma.changeBalance(balance + (bet * win).toInt())
                        saveBalance(gameFragma.balance.value ?: 0)
                    } else {
                        doAgain = true
                        gameFragma.changeBalance(balance + bet)
                    }
                }
            }
            while (gameFragma.isAutoSpinMode.value == true || doAgain)
        }
    }
    companion object {
        val rouletteRotations = mapOf(
            4 to 3.4,
            26 to 15.0,
            49 to 0.0,
            71 to 10.0,
            94 to 2.4,
            116 to -1.0,
            139 to 0.0,
            161 to 7.0,
            184 to 0.0,
            207 to 25.0,
            229 to 1.9,
            252 to 10.0,
            274 to 0.0,
            297 to 20.0,
            319 to 1.4,
            342 to 5.0
        )
    }
}