package com.vptarasov.autosearch.ui.fragments.favourite

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vptarasov.autosearch.App
import com.vptarasov.autosearch.database.HelperFactory
import com.vptarasov.autosearch.di.component.DaggerFragmentComponent
import com.vptarasov.autosearch.di.module.FragmentModule
import com.vptarasov.autosearch.model.Car
import com.vptarasov.autosearch.ui.fragments.car.CarFragment
import com.vptarasov.autosearch.util.FragmentUtil
import kotlinx.android.synthetic.main.fragment_favourite_list.view.*
import java.sql.SQLException
import java.util.*
import javax.inject.Inject



class FavouriteFragment : Fragment(), FavouriteContract.View {

    private var cars: List<Car>? = null
    private var adapter: FavouriteAdapter? = null
    private var recyclerView: RecyclerView? = null
    private var noFoundText: TextView? = null

    @Inject
    lateinit var presenter: FavouriteContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependency()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(com.vptarasov.autosearch.R.layout.fragment_favourite_list, container, false)
        initView(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attach(this)
        presenter.subscribe()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.unsubscribe()
    }

    override fun initView(view: View) {
        recyclerView = view.recyclerViewFavourite
        noFoundText = view.noFoundText

        cars = presenter.loadFavouriteCars()

        if (cars != null) {
            if (cars!!.isNotEmpty()) {
                initAdapter()

            } else {
                noFoundText?.visibility = View.VISIBLE
            }
        } else {
            noFoundText?.visibility = View.VISIBLE

        }
    }

    @SuppressLint("SetTextI18n")
    override fun initAdapter() {

        for (car in cars!!) {
            car.setBookmarked(isCarBookmarked(car))
        }

        adapter = FavouriteAdapter(cars as ArrayList<Car>)
        adapter?.setListener(this)
        recyclerView?.layoutManager = LinearLayoutManager(context)
        recyclerView?.adapter = adapter
    }


    override fun onItemClick(car: Car) {
        val bundle = Bundle()
        bundle.putString("carUrl", car.url)
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
                        com.vptarasov.autosearch.R.string.add_favor
                    else
                        com.vptarasov.autosearch.R.string.delete_favor
                ), Toast.LENGTH_SHORT
            ).show()
            adapter?.updateFavIcon(car)

            val carsFromAdapter = adapter!!.cars
            for (i in carsFromAdapter.indices) {
                if (carsFromAdapter[i] == car) {
                    adapter?.notifyItemRemoved(i)
                    adapter?.notifyItemRangeChanged(i, adapter!!.itemCount)
                    adapter?.cars?.removeAt(i)
                }
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }

    }

    override fun onLastFavoriteRemoved() {
        noFoundText?.visibility = View.VISIBLE
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

    private fun injectDependency() {
        val fragmentComponent = DaggerFragmentComponent.builder()
            .fragmentModule(FragmentModule())
            .build()

        fragmentComponent.inject(this)
    }

}
