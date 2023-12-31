package com.semdiniagara.yisofalls.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.semdiniagara.yisofalls.R

fun Fragment.goTo(fragment: Fragment, isReplace: Boolean = true) {
    val transaction = parentFragmentManager.beginTransaction()
    checkIfReplace(transaction, fragment, isReplace)
    transaction.commitAllowingStateLoss()
}

fun checkIfReplace(transaction: FragmentTransaction, fragment: Fragment, isReplace: Boolean) {
    if(isReplace) {
        transaction.replace(R.id.mainFragmentContainer, fragment)
    }
    else {
        transaction.add(R.id.mainFragmentContainer, fragment)
    }
}