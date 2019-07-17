package com.vptarasov.autosearch.ui.fragments.car

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.vptarasov.autosearch.ui.fragments.car.PhotoPageFragment
import java.util.*

class PhotoAdapter(fragmentManager: FragmentManager, private val photoList: ArrayList<String>?) :
    FragmentPagerAdapter(fragmentManager) {

    override fun getCount(): Int {
        return photoList!!.size
    }


    override fun getItem(i: Int): Fragment {
        val photoPageFragment = PhotoPageFragment()
        val bundle = Bundle()
        bundle.putString("urlPhoto", photoList?.get(i))
        photoPageFragment.arguments = bundle


        return photoPageFragment
    }
}
