package com.vptarasov.autosearch.ui.activity.main

import com.vptarasov.autosearch.ui.AppContract

class MainContract {

    interface View: AppContract.View {
        fun showCarsListFragment()
        fun showSearchFragment()
        fun showFavouriteListFragment()
        fun showNewsPreviewFragment()
    }

    interface Presenter: AppContract.Presenter<View>
}