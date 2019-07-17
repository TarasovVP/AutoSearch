package com.vptarasov.autosearch.ui.fragments.photo_full_size

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.vptarasov.autosearch.R
import com.vptarasov.autosearch.di.component.DaggerFragmentComponent
import com.vptarasov.autosearch.di.module.FragmentModule
import kotlinx.android.synthetic.main.fragment_photo_full_size.view.*
import java.util.*
import javax.inject.Inject

class PhotoFullSizeFragment : Fragment(), PhotoFullSizeContract.View {

    private var photoList: ArrayList<String>? = null
    private var name: String? = null
    private var price: String? = null
    private var year: String? = null
    private var viewPagerPosition: Int = 0

    @Inject
    lateinit var presenter: PhotoFullSizeContract.Presenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_photo_full_size, container, false)
        injectDependency()
        val pagerFullSize: ViewPager = view.pagerFullSize
        val nameCar = view.nameCar
        val priceCar = view.priceCar
        val yearCar = view.yearCar
        val buttonBackFullPhoto = view.buttonBackFullPhoto

        buttonBackFullPhoto?.setOnClickListener { Objects.requireNonNull(fragmentManager)?.popBackStack() }
        val args = arguments
        if (args != null) {
            photoList = args.getStringArrayList("photoList")
            viewPagerPosition = args.getInt("viewPagerPosition")
            name = args.getString("name")
            year = args.getString("year")
            price = args.getString("price")
        }
        pagerFullSize.adapter =
            PhotoFullSizeAdapter(childFragmentManager, photoList)
        pagerFullSize.currentItem = viewPagerPosition
        nameCar?.text = name
        yearCar?.text = year
        priceCar?.text = price
        return view
    }
    private fun injectDependency() {
        val fragmentComponent = DaggerFragmentComponent.builder()
            .fragmentModule(FragmentModule())
            .build()

        fragmentComponent.inject(this)
    }


}