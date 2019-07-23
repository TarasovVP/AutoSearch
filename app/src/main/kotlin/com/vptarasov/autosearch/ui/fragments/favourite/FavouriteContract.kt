package com.vptarasov.autosearch.ui.fragments.favourite

import com.vptarasov.autosearch.model.Car
import com.vptarasov.autosearch.ui.AppContract

class FavouriteContract {

    interface View : AppContract.View {
        fun initView(view: android.view.View)
        fun initAdapter()
        fun onItemClick(car: Car)
        fun onFavoriteClick(car: Car)
        fun onLastFavoriteRemoved()
    }

    interface Presenter : AppContract.Presenter<View>{
        fun loadFavouriteCars(): List<Car>?
    }
}