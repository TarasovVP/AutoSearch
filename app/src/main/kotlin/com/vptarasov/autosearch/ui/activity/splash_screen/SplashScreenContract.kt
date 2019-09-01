package com.vptarasov.autosearch.ui.activity.splash_screen

import com.vptarasov.autosearch.model.SearchData
import com.vptarasov.autosearch.ui.AppContract

class SplashScreenContract {

    interface View: AppContract.View {
        fun showMainActivity(searchData: SearchData)

    }

    interface Presenter: AppContract.Presenter<View>{
        fun loadSearchData()
    }
}