package com.vptarasov.autosearch.ui.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.vptarasov.autosearch.ui.fragments.PhotoFullSizePageFragment
import java.util.*

class PhotoFullSizeAdapter(fragmentManager: FragmentManager, private val photoList: ArrayList<String>?) :
    FragmentPagerAdapter(fragmentManager) {

    override fun getCount(): Int {
        return photoList!!.size
    }

    override fun getItem(i: Int): Fragment {
        val photoPageFragment = PhotoFullSizePageFragment()
        val bundle = Bundle()
        bundle.putString("photo", photoList?.get(i))
        photoPageFragment.arguments = bundle


        return photoPageFragment
    }
}
