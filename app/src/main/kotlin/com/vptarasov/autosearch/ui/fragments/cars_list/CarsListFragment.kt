package com.vptarasov.autosearch.ui.fragments.cars_list

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.vptarasov.autosearch.App
import com.vptarasov.autosearch.R
import com.vptarasov.autosearch.di.component.DaggerFragmentComponent
import com.vptarasov.autosearch.di.module.FragmentModule
import com.vptarasov.autosearch.model.Car
import com.vptarasov.autosearch.model.QueryDetails
import com.vptarasov.autosearch.ui.fragments.BaseCarFragment
import com.vptarasov.autosearch.ui.fragments.car.CarFragment
import com.vptarasov.autosearch.util.FragmentUtil
import kotlinx.android.synthetic.main.fragment_cars_list.view.*
import kotlinx.android.synthetic.main.list_pages.view.*
import java.util.*
import javax.inject.Inject

class CarsListFragment : BaseCarFragment<CarsListFragment, CarsListFragment>(),
    CarsListContract.View {

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
    private lateinit var progressBar: ProgressBar
    private lateinit var nothingFoundText: TextView
    private lateinit var swiperefresh: SwipeRefreshLayout

    @Inject
    lateinit var presenter: CarsListContract.Presenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_cars_list, container, false)
        retainInstance = true
        val args = arguments
        if (cars == null) {
            if (args?.getSerializable("queryDetails") != null) {
                queryDetails = args.getSerializable("queryDetails") as QueryDetails
                presenter.loadCars(queryDetails, page)

            } else {
                queryDetails = QueryDetails()
                presenter.loadCars(queryDetails, page)
            }
        } else {
            initAdapter(cars!!, lastPage)
        }
        return view
    }

    override fun getLastPage(lastPage: Int) {
        this.lastPage = lastPage
    }

    @SuppressLint("SetTextI18n")
    private fun preformSearch() {
        presenter.loadCars(queryDetails, page)
        tvPage.text = App.instance?.getString(R.string.page) + " " + page + " из " + lastPage
        recyclerViewCar.smoothScrollToPosition(0)
    }

    override fun initView(view: View) {
        recyclerViewCar = view.recyclerViewCar
        btnRight = view.btnRight
        btnLeft = view.btnLeft
        tvPage = view.tvPage
        searchPaging = view.searchPaging as RelativeLayout
        progressBar = view.progressBarCarsList
        nothingFoundText = view.nothingFoundText
        swiperefresh = view.swiperefresh

        swiperefresh.setOnRefreshListener {
            presenter.loadCars(queryDetails, page)
            swiperefresh.isRefreshing = false
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
    }

    @SuppressLint("SetTextI18n")
    override fun initAdapter(cars: ArrayList<Car>, lastPage: Int) {
        adapter = CarsListAdapter(cars)
        adapter?.setListener(this)
        recyclerViewCar.layoutManager = LinearLayoutManager(context)
        recyclerViewCar.adapter = adapter
        tvPage.text = App.instance?.getString(R.string.page) + " " + page + " из " + lastPage
        searchPaging.visibility = View.GONE
        hideProgress()
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
        val carFragment = CarFragment()
        carFragment.arguments = bundle
        fragmentManager?.let { FragmentUtil.replaceFragment(it, carFragment, true) }

    }

    override fun onFavoriteClick(car: Car) {

        addDeleCarWithFirebase(car)
        if (car.isBookmarked()) {
            car.setBookmarked(false)
        } else {
            car.setBookmarked(true)
        }
        adapter?.updateFavIcon(car)

    }


    override fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progressBar.visibility = View.GONE
    }

    override fun showNothingFoundText() {
        nothingFoundText.visibility = View.VISIBLE
    }


    override fun injectDependency() {
        val fragmentComponent = DaggerFragmentComponent.builder()
            .fragmentModule(FragmentModule())
            .build()

        fragmentComponent.inject(this)
        presenter.attach(this)
    }
}
