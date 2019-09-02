package com.vptarasov.autosearch.util

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
import androidx.fragment.app.FragmentTransaction
import com.vptarasov.autosearch.R

object FragmentUtil {

    fun replaceFragment(
        manager: FragmentManager?,
        fragment: Fragment,
        addToBackStack: Boolean
    ) {
        val fTrans: FragmentTransaction = manager!!.beginTransaction()
        fTrans.replace(R.id.container, fragment)
        fTrans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        if (addToBackStack) {
            fTrans.addToBackStack(null)
        } else {
            manager.popBackStack(null, POP_BACK_STACK_INCLUSIVE)
        }
        fTrans.commitAllowingStateLoss()
    }

}

