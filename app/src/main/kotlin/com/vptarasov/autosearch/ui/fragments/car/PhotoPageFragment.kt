package com.vptarasov.autosearch.ui.fragments.car

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import com.vptarasov.autosearch.R
import kotlinx.android.synthetic.main.car_photo.view.*

class PhotoPageFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.car_photo, container, false)
        val carPhoto = if (arguments != null) arguments?.getString("urlPhoto") else null
        val photo = view.list_Photo


        Picasso.get()
            .load(carPhoto)
            .placeholder(R.drawable.placeholder)
            .into(photo)

        return view


    }
}
