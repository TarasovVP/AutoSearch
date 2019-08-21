package com.vptarasov.autosearch.ui.activity.main

import com.google.firebase.auth.FirebaseUser
import com.vptarasov.autosearch.ui.AppContract

class MainContract {

    interface View: AppContract.View {
        fun initUser()
        fun initView()
        fun checkConnectivity()
        fun showUserInfo()
        fun showCarsListFragment()
        fun showSearchFragment()
        fun showFavouriteListFragment()
        fun showNewsPreviewFragment()
        fun showUserActivity()
        fun exitByBackKey()
    }

    interface Presenter: AppContract.Presenter<View>{
        fun checkUserWithFireStore(firebaseUser: FirebaseUser)
    }
}