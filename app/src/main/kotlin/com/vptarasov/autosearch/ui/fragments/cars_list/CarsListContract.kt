package com.vptarasov.autosearch.ui.fragments.cars_list

import com.vptarasov.autosearch.ui.AppContract

class CarsListContract {

    interface View: AppContract.View

    interface Presenter: AppContract.Presenter<View>
}