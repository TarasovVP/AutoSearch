package com.vptarasov.autosearch.ui.fragments.car

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.squareup.picasso.Picasso
import com.vptarasov.autosearch.R
import com.vptarasov.autosearch.di.component.DaggerFragmentComponent
import com.vptarasov.autosearch.di.module.FragmentModule
import com.vptarasov.autosearch.model.Car
import com.vptarasov.autosearch.ui.fragments.BaseCarFragment
import com.vptarasov.autosearch.ui.fragments.photo_full_size.PhotoFullSizeFragment
import com.vptarasov.autosearch.util.FragmentUtil
import com.vptarasov.autosearch.util.OCR
import kotlinx.android.synthetic.main.fragment_car.view.*
import java.io.IOException
import java.util.*
import javax.inject.Inject


class CarFragment : BaseCarFragment<CarFragment, CarFragment>(), CarContract.View {

    private var photoList: ArrayList<String>? = null
    private var viewPager: ViewPager? = null
    private var sellerPhone: String? = null


    private var name: TextView? = null
    private var price: TextView? = null
    private var year: TextView? = null
    private var engine: TextView? = null
    private var mileage: TextView? = null
    private var color: TextView? = null
    private var gearbox: TextView? = null
    private var drive: TextView? = null
    private var body: TextView? = null
    private var city: TextView? = null
    private var date: TextView? = null
    private var buttonBack: ImageButton? = null
    private var zoomOut: ImageView? = null
    private var favouriteCar: ImageView? = null
    private var callButton: Button? = null
    private var writeButton: Button? = null
    private var textCar: TextView? = null

    @Inject
    lateinit var presenter: CarContract.Presenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_car, container, false)
        initView(view)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val args = arguments
        if (args != null) {
            val urlCar = args.getString("carUrl")
            presenter.loadCar(urlCar)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attach(this)
    }

    override fun initView(view: View) {
        viewPager = view.pager
        name = view.nameCar
        price = view.priceCar
        year = view.yearCar
        engine = view.engineCar
        mileage = view.mileageCar
        color = view.colorCar
        gearbox = view.gearboxCar
        drive = view.driveCar
        body = view.bodyCar
        city = view.citySeller
        date = view.dateCar
        buttonBack = view.buttonBackCar
        zoomOut = view.zoom_out
        favouriteCar = view.favouriteCar
        callButton = view.callButton
        writeButton = view.writeButton
        textCar = view.textCar
        val tabLayout = view.findViewById<View>(R.id.tabDots) as TabLayout
        tabLayout.setupWithViewPager(viewPager, true)
    }

    override fun setDataToViews(car: Car) {
        updateFavIcon(car)
        name?.text = car.name
        year?.text = car.year
        price?.text = car.price
        engine?.text = car.engine
        mileage?.text = car.mileage
        color?.text = car.color
        gearbox?.text = car.gearbox
        drive?.text = car.drive
        body?.text = car.body
        textCar?.text = HtmlCompat.fromHtml(car.webMainText!!, HtmlCompat.FROM_HTML_MODE_LEGACY)
        date?.text = car.date
        city?.text = car.city

        val phoneNumber = car.phone
        val thread = Thread {
            try {
                if (phoneNumber != null && "" != car.phone) {
                    val bitmap = Picasso.get().load(phoneNumber).get()
                    val ocr = OCR(bitmap)
                    sellerPhone = ocr.recognizedText()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        thread.start()

        callButton?.setOnClickListener {
            val callIntent = Intent(Intent.ACTION_DIAL)
            callIntent.data = Uri.parse("tel:" + sellerPhone!!)
            startActivity(callIntent)
        }

        writeButton?.setOnClickListener {
            val writeIntent = Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + sellerPhone!!))
            startActivity(writeIntent)
        }

        buttonBack?.setOnClickListener { Objects.requireNonNull(fragmentManager)?.popBackStack() }

        zoomOut?.setOnClickListener {
            showphotoFullSizeFragment(car)
        }

        favouriteCar?.setOnClickListener {
            onFavoriteClick(car)
        }

        photoList = car.photoList
        viewPager?.adapter = PhotoAdapter(childFragmentManager, photoList)
    }

    override fun injectDependency() {
        val fragmentComponent = DaggerFragmentComponent.builder()
            .fragmentModule(FragmentModule())
            .build()

        fragmentComponent.inject(this)
    }

    private fun updateFavIcon(car: Car) {
        favouriteCar?.setBackgroundResource(if (car.isBookmarked()) R.drawable.favouritechecked else R.drawable.favourite)
    }

    override fun onFavoriteClick(car: Car) {

        if (car.isBookmarked()){
            car.setBookmarked(false)
        }else{
            car.setBookmarked(true)
        }
        updateFavIcon(car)
        addDeleCarWithFirebase(car)


    }

    override fun showphotoFullSizeFragment(car: Car){
        val photoFullSizeFragment = PhotoFullSizeFragment()
        val bundle = Bundle()
        bundle.putStringArrayList("photoList", photoList)
        bundle.putInt("viewPagerPosition", viewPager!!.currentItem)
        bundle.putString("name", car.name)
        bundle.putString("price", car.price)
        bundle.putString("year", car.year)
        photoFullSizeFragment.arguments = bundle
        FragmentUtil.replaceFragment(
            (view?.context as AppCompatActivity).supportFragmentManager,
            photoFullSizeFragment,
            true
        )
    }

}
