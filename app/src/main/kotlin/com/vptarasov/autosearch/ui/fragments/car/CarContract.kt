package com.vptarasov.autosearch.ui.fragments.car

import com.vptarasov.autosearch.model.Car
import com.vptarasov.autosearch.ui.AppContract

class CarContract {

    interface View: AppContract.View{
        fun initView(view: android.view.View)
        fun setDataToViews(car: Car)
        fun onFavoriteClick(car: Car)
        fun showphotoFullSizeFragment(car: Car)
    }

    interface Presenter: AppContract.Presenter<View>{
        fun loadCar(urlCar: String?)
        fun loadFavouriteCars(car: Car)
    }
}