package com.vptarasov.autosearch.ui.fragments

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
import com.vptarasov.autosearch.R
import com.vptarasov.autosearch.database.HelperFactory
import com.vptarasov.autosearch.interfaces.FavoritesInteractionListener
import com.vptarasov.autosearch.model.Car
import com.vptarasov.autosearch.ui.adapters.FavouriteAdapter
import com.vptarasov.autosearch.util.FragmentUtil
import kotlinx.android.synthetic.main.fragment_favourite_list.view.*
import java.sql.SQLException
import java.util.*

class FavouriteListFragment : Fragment(), FavoritesInteractionListener {

    private var cars: List<Car>? = null
    private var adapter: FavouriteAdapter? = null


    private var recyclerView: RecyclerView? = null
    private var noFoundText: TextView? = null



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_favourite_list, container, false)
        recyclerView = view.recyclerViewFavourite
        noFoundText = view.noFoundText

        try {
            cars = HelperFactory.helper?.getFavoritesDao()?.loadAll()
        } catch (e: SQLException) {
            e.printStackTrace()
        }

        if (cars != null) {
            if (cars!!.isNotEmpty()) {
                initAdapter()

            } else {
                noFoundText?.visibility = View.VISIBLE
            }
        } else {
            noFoundText?.visibility = View.VISIBLE

        }
        return view
    }

    @SuppressLint("SetTextI18n")
    fun initAdapter() {

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

}
