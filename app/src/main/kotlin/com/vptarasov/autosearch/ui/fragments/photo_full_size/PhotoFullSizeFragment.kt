package com.vptarasov.autosearch.ui.fragments.photo_full_size

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
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

    private var pagerFullSize: ViewPager? = null
    private var nameCar: TextView? = null
    private var priceCar: TextView? = null
    private var yearCar: TextView? = null
    private var buttonBackFullPhoto: ImageView? = null

    @Inject
    lateinit var presenter: PhotoFullSizeContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependency()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_photo_full_size, container, false)
        initView(view)
        return view
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val args = arguments
        if (args != null) {
            photoList = args.getStringArrayList("photoList")
            viewPagerPosition = args.getInt("viewPagerPosition")
            name = args.getString("name")
            year = args.getString("year")
            price = args.getString("price")
        }
        setDataToViews()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attach(this)
    }

    private fun injectDependency() {
        val fragmentComponent = DaggerFragmentComponent.builder()
            .fragmentModule(FragmentModule())
            .build()

        fragmentComponent.inject(this)
    }

    override fun initView(view: View) {
        pagerFullSize= view.pagerFullSize
        nameCar = view.nameCar
        priceCar = view.priceCar
        yearCar = view.yearCar
        buttonBackFullPhoto = view.buttonBackFullPhoto
        buttonBackFullPhoto?.setOnClickListener { Objects.requireNonNull(fragmentManager)?.popBackStack() }
    }

    override fun setDataToViews() {

        pagerFullSize?.adapter =
            PhotoFullSizeAdapter(childFragmentManager, photoList)
        pagerFullSize!!.currentItem = viewPagerPosition
        nameCar!!.text = name
        yearCar!!.text = year
        priceCar!!.text = price
    }

}
