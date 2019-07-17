package com.vptarasov.autosearch.ui.activity.splash_screen

import com.vptarasov.autosearch.ui.AppContract

class SplashScreenContract {

    interface View: AppContract.View {
        fun showLoadingFragment()

    }

    interface Presenter: AppContract.Presenter<View>
}