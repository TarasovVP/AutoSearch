package com.vptarasov.autosearch.ui.fragments.cars_list

import com.vptarasov.autosearch.model.Car
import com.vptarasov.autosearch.model.QueryDetails
import com.vptarasov.autosearch.ui.AppContract

class CarsListContract {

    interface View : AppContract.View {
        fun initView(view: android.view.View)
        fun initAdapter(cars: ArrayList<Car>, lastPage: Int)
        fun onLastItemReached()
        fun onLastItemLeft()
        fun onItemClick(car: Car)
        fun onFavoriteClick(car: Car)
        fun getLastPage(lastPage: Int)
        fun showProgress()
        fun hideProgress()
        fun showErrorMessage(error: String)
        fun showNothingFoundText()
    }

    interface Presenter : AppContract.Presenter<View>{
        fun loadCars(queryDetails: QueryDetails?, page: Int)
        fun loadFavouriteCars()
    }
}