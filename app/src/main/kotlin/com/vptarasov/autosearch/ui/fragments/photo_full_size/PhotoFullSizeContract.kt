package com.vptarasov.autosearch.ui.fragments.photo_full_size

import com.vptarasov.autosearch.ui.AppContract

class PhotoFullSizeContract {

    interface View: AppContract.View{
        fun initView(view: android.view.View)
        fun setDataToViews()
    }

    interface Presenter: AppContract.Presenter<View>
}