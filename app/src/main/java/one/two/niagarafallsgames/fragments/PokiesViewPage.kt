package one.two.niagarafallsgames.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.core.view.allViews
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import one.two.niagarafallsgames.R
import kotlin.concurrent.thread
import kotlin.random.Random

class PokiesViewPage: GameViewPage(), CanDoSpin {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.view_page_pokies_landscape, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.allViews.filter {
            it is ImageView && it.tag != null
        }.map{ it as ImageView}.toList().forEachIndexed { index, imageView ->
            imageView.tag = index % 6
        }
        view.findViewById<ImageButton>(R.id.spinButton).setOnClickListener {
            it.isEnabled = false
            CoroutineScope(Dispatchers.Main).launch {
                spin()
                it.isEnabled = true
            }
        }
    }

    fun setRandomPokies(pokies: List<ImageView>, index: Int = 0) {
        for(i in 0..(12 + index) step 3) {
            pokies[i + index].apply {
                tag = Random.nextInt(0, 6)
                setImageResource(R.drawable.pokies_element01 + tag as Int)
            }
        }
    }

    fun setByPrevious(pokies: List<ImageView>, index: Int = 1) {
        for(i in 0..(12 + index) step 3) {
            pokies[index + i].tag = pokies[index + i - 1].tag
            pokies[index + i].setImageResource(R.drawable.pokies_element01 + pokies[index + i].tag as Int)
        }
    }

    fun getColumn(pokies: List<ImageView>, index: Int = 0): List<ImageView> {
        return listOf(
            pokies[0 + index * 3],
            pokies[1 + index * 3],
            pokies[2 + index * 3],
        )
    }


    override suspend fun spin() {
        val pokies = view?.allViews?.filter {
            it is ImageView && it.tag != null
        }?.map{ it as ImageView}?.toList()
        if(!pokies.isNullOrEmpty()) {
            var doAgain: Boolean
            do {
                doAgain = false
                val result = gameFragma.decreaseBalanceByBet()
                if (result) {
                    val bet = gameFragma.bet.value ?: 0
                    val balance = gameFragma.balance.value ?: 0
                    var stop = false
                    thread {
                        Thread.sleep(3000 + Random.nextLong(0, 2000))
                        stop = true
                    }
                    while (!stop) {
                        setRandomPokies(pokies)
                        setByPrevious(pokies, 2)
                        setByPrevious(pokies)
                        delay(100)
                    }
                    repeat(10) {
                        setRandomPokies(pokies)
                        setByPrevious(pokies, 2)
                        setByPrevious(pokies)
                        delay(250)
                    }
                    var win = 0.0
                    val columns = listOf(
                        getColumn(pokies),
                        getColumn(pokies, 1),
                        getColumn(pokies, 2),
                        getColumn(pokies, 3),
                        getColumn(pokies, 4),
                    )
                    for(i in 0..5) {
                        win += if(
                            columns.all { column ->
                                column.any {
                                    it.tag == i
                                }
                            }
                        )
                            (10 + 10 * i).toDouble()
                        else
                            0.0
                    }
                    if (win != 0.0) {
                        gameFragma.changeBalance(balance + (bet * win).toInt())
                        saveBalance(gameFragma.balance.value ?: 0)
                    }
                }
            }
            while (gameFragma.isAutoSpinMode.value == true || doAgain)
        }
    }
}