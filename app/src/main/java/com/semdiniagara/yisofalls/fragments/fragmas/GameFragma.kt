package com.semdiniagara.yisofalls.fragments.fragmas

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameFragma: ViewModel() {
    private val pBet = MutableLiveData(10)
    private val pBalance = MutableLiveData(0)
    private val pIsAutoSpinMode = MutableLiveData(false)

    val bet: LiveData<Int>
        get() = pBet

    val balance: LiveData<Int>
        get() = pBalance

    val isAutoSpinMode: LiveData<Boolean>
        get() = pIsAutoSpinMode

    fun offAutoSpinMode() {
        pIsAutoSpinMode.value = false
    }

    fun onAutoSpinMode() {
        pIsAutoSpinMode.value = true
    }

    fun changeBalance(value: Int) {
        pBalance.value = value
    }

    fun decreaseBalanceByBet(): Boolean {
        val balance = pBalance.value
        val bet = pBet.value
        return if(balance != null && bet != null && balance - bet > 0) {
            changeBalance(balance - bet)
            true
        }
        else {
            false
        }
    }

    fun decreaseBet() {
        val bet = pBet.value
        if(bet != null && bet > 10) {
            pBet.value = bet - 10
        }
    }

    fun increaseBet() {
        val bet = pBet.value
        if(bet != null) {
            pBet.value = bet + 10
        }
    }
}