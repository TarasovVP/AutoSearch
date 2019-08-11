package com.vptarasov.autosearch.ui.fragments.news

import com.vptarasov.autosearch.ui.AppContract

class NewsContract {

    interface View: AppContract.View{
        fun initView(view: android.view.View)
    }

    interface Presenter: AppContract.Presenter<View>
}