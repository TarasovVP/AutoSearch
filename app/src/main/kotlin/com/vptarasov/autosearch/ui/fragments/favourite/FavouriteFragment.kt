package com.vptarasov.autosearch.ui.fragments.favourite

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vptarasov.autosearch.R
import com.vptarasov.autosearch.di.component.DaggerFragmentComponent
import com.vptarasov.autosearch.di.module.FragmentModule
import com.vptarasov.autosearch.model.Car
import com.vptarasov.autosearch.ui.fragments.BaseCarFragment
import com.vptarasov.autosearch.ui.fragments.car.CarFragment
import com.vptarasov.autosearch.util.FragmentUtil
import kotlinx.android.synthetic.main.fragment_favourite_list.view.*
import javax.inject.Inject


class FavouriteFragment : BaseCarFragment<FavouriteFragment, FavouriteFragment>(),
    FavouriteContract.View {


    private var adapter: FavouriteAdapter? = null
    private var recyclerView: RecyclerView? = null
    private var noFoundText: TextView? = null
    private var progressBar: ProgressBar? = null

    @Inject
    lateinit var presenter: FavouriteContract.Presenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_favourite_list, container, false)
        return view
    }

    override fun initView(view: View) {
        recyclerView = view.recyclerViewFavourite
        noFoundText = view.noFoundText
        progressBar = view.progressBarFavourite
        val cars = presenter.getFavouriteCars()
        initAdapter(cars)
    }

    @SuppressLint("SetTextI18n")
    override fun initAdapter(cars: ArrayList<Car>) {
        if (cars.isNullOrEmpty()) {
            noFoundText?.visibility = View.VISIBLE
        } else {
            adapter = FavouriteAdapter(cars)
            adapter?.setListener(this)
            recyclerView?.layoutManager =
                LinearLayoutManager(context)
            recyclerView?.adapter = adapter
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
        notifyAdapter(car)

    }

    override fun notifyAdapter(car: Car) {
        val carsFromAdapter = adapter!!.cars
        for (i in carsFromAdapter.indices) {
            if (carsFromAdapter[i] == car) {
                adapter?.notifyItemRemoved(i)
                adapter?.notifyItemRangeChanged(i, adapter!!.itemCount)
                adapter?.cars?.removeAt(i)
                break
            }
        }
    }


    override fun onLastFavoriteRemoved() {
        noFoundText?.visibility = View.VISIBLE
    }

    override fun showProgress() {
        progressBar?.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progressBar?.visibility = View.GONE
    }

    override fun injectDependency() {
        val fragmentComponent = DaggerFragmentComponent.builder()
            .fragmentModule(FragmentModule())
            .build()

        fragmentComponent.inject(this)
        presenter.attach(this)
        presenter.loadFavouriteCars()
    }

}
