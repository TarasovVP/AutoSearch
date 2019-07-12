package com.vptarasov.autosearch.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vptarasov.autosearch.App
import com.vptarasov.autosearch.R
import com.vptarasov.autosearch.api.Api
import com.vptarasov.autosearch.database.HelperFactory
import com.vptarasov.autosearch.interfaces.CarsInteractionListener
import com.vptarasov.autosearch.model.Car
import com.vptarasov.autosearch.model.QueryDetails
import com.vptarasov.autosearch.parser.HTMLParser
import com.vptarasov.autosearch.ui.adapters.CarsListAdapter
import com.vptarasov.autosearch.util.FragmentUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_cars_list.view.*
import kotlinx.android.synthetic.main.list_pages.view.*
import org.jsoup.Jsoup
import java.sql.SQLException
import java.util.*

class CarsListFragment : BaseFragment(), CarsInteractionListener {


    private var adapter: CarsListAdapter? = null
    private var page: Int = 1
    private var lastPage: Int = 0
    private var queryDetails: QueryDetails? = null
    private var cars: ArrayList<Car>? = null
    private lateinit var recyclerViewCar: RecyclerView
    private lateinit var btnRight: Button
    private lateinit var btnLeft: Button
    private lateinit var tvPage: TextView
    private lateinit var searchPaging: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }


    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater,  container: ViewGroup?,  savedInstanceState: Bundle?): View? {
        val view = inflateWithLoadingIndicator(R.layout.fragment_cars_list, container)
        recyclerViewCar = view.recyclerViewCar
        btnRight = view.btnRight
        btnLeft = view.btnLeft
        tvPage = view.tvPage
        searchPaging = view.searchPaging as RelativeLayout
        val args = arguments
        if (cars == null) {
            if (args?.getSerializable("queryDetails") != null) {
                queryDetails = args.getSerializable("queryDetails") as QueryDetails
                searchCars()

            } else {
                queryDetails = QueryDetails()
                searchCars()
            }
        } else {
            initAdapter()
        }
        btnRight.setOnClickListener {
            if (page < lastPage) {
                page++
                preformSearch()
            }

        }
        btnLeft.setOnClickListener {
            if (page > 1) {
                page--
                preformSearch()
            }

        }


        return view
    }

    @SuppressLint("SetTextI18n")
    private fun preformSearch() {
        searchCars()
        tvPage.text = App.instance?.getString(R.string.page) + " " + page + " из " + lastPage
        recyclerViewCar.smoothScrollToPosition(0)
    }

    @SuppressLint("CheckResult")
    private fun searchCars() {
        setLoading(true)
        Api
            .service
            .searchCars(
                queryDetails?.mark,
                queryDetails?.model,
                queryDetails?.region,
                queryDetails?.city,
                queryDetails?.body,
                queryDetails?.color,
                queryDetails?.engineFrom,
                queryDetails?.engineUnit,
                queryDetails?.yearFrom,
                queryDetails?.engineTo,
                queryDetails?.yearTo,
                queryDetails?.priceFrom,
                queryDetails?.priceTo,
                queryDetails?.petrolElectro,
                queryDetails?.diesel,
                queryDetails?.electro,
                queryDetails?.gas,
                queryDetails?.gasPetrol,
                queryDetails?.petrol,
                queryDetails?.gearboxAutom,
                queryDetails?.gearboxMech,
                page.toString()
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ responseBody ->
                val htmlParser = HTMLParser()
                val html = responseBody.string()
                cars = htmlParser.getCarList(html)
                val el = Jsoup.parse(html).select("div[id=paging]")
                lastPage = if (el.size > 0) {
                    Integer.valueOf(el.select("a").last().text())
                } else
                    1
                if (cars!!.size > 0) {
                    initAdapter()
                } else {
                    showErrorMessage(R.string.nothing_found)
                }
                setLoading(false)

            }, { throwable ->
                throwable.printStackTrace()
                setLoading(true)
            })
    }


    @SuppressLint("SetTextI18n")
    fun initAdapter() {

        for (car in cars!!) {
            car.setBookmarked(isCarBookmarked(car))
        }

        adapter = CarsListAdapter(cars!!)
        adapter?.setListener(this)
        recyclerViewCar.layoutManager = LinearLayoutManager(context)
        recyclerViewCar.adapter = adapter
        tvPage.text = App.instance?.getString(R.string.page) + " " + page + " из " + lastPage
        searchPaging.visibility = View.GONE
    }

    override fun onLastItemReached() {
        if (searchPaging.visibility != View.VISIBLE) {
            searchPaging.visibility = View.VISIBLE
        }
    }

    override fun onLastItemLeft() {
        if (searchPaging.visibility != View.GONE) {
            searchPaging.visibility = View.GONE
        }
    }

    override fun onItemClick(car: Car) {
        val bundle = Bundle()
        bundle.putString("carUrl", car.url)
        bundle.putSerializable("car", car)
        val carFragment = CarFragment()
        carFragment.arguments = bundle
        fragmentManager?.let { FragmentUtil.replaceFragment(it, carFragment, true) }

    }

    override fun onFavoriteClick(car: Car) {
        try {
            HelperFactory.helper?.getFavoritesDao()?.switchBookmarkedStatus(car)
            car.setBookmarked(isCarBookmarked(car))
            Toast.makeText(
                context, App.instance?.getString(
                    if (car.isBookmarked())
                        R.string.add_favor
                    else
                        R.string.delete_favor
                ), Toast.LENGTH_SHORT
            ).show()
            adapter?.updateFavIcon(car)

        } catch (e: SQLException) {
            e.printStackTrace()
        }

    }

    private fun isCarBookmarked(car: Car): Boolean {
        var bookmarked = false
        try {
            bookmarked = HelperFactory.helper?.getFavoritesDao()!!.exists(car)
        } catch (e: SQLException) {
            e.printStackTrace()
        }

        return bookmarked
    }
}
