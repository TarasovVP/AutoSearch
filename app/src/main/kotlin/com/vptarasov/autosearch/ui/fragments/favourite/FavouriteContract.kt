package com.vptarasov.autosearch.ui.fragments.favourite

import com.vptarasov.autosearch.model.Car
import com.vptarasov.autosearch.ui.AppContract

class FavouriteContract {

    interface View : AppContract.View {
        fun initAdapter(cars: ArrayList<Car>)
        fun notifyAdapter(car: Car)
        fun onItemClick(car: Car)
        fun onFavoriteClick(car: Car)
        fun onLastFavoriteRemoved()
        fun hideProgress()
    }

    interface Presenter : AppContract.Presenter<View>{
        fun loadFavouriteCars()
        fun getFavouriteCars(): ArrayList<Car>
    }
}