package one.two.niagarafallsgames.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import one.two.niagarafallsgames.R
import one.two.niagarafallsgames.navigation.goTo

class MenuViewPage: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.view_page_menu, container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<AppCompatButton>(R.id.menuPlayPokies).setOnClickListener {
            goTo(PokiesViewPage())
        }
        view.findViewById<AppCompatButton>(R.id.menuPlayRoulette).setOnClickListener {
            goTo(RouletteViewPage())
        }
        view.findViewById<AppCompatButton>(R.id.menuPrivacyPolicy).setOnClickListener {
            goTo(PrivacyPolicyViewPage())
        }
    }
}