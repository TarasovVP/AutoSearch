package com.vptarasov.autosearch.ui.fragments.search

import android.widget.Spinner
import com.vptarasov.autosearch.model.SearchData
import com.vptarasov.autosearch.ui.AppContract
import java.util.*

class SearchContract {

    interface View: AppContract.View{
        fun initView(view: android.view.View)
        fun setDataToSpinner(searchData: SearchData?)
        fun fulfillSpinner(arrayList: ArrayList<String>, spinner: Spinner?)
        fun resetViewValues()
    }

    interface Presenter: AppContract.Presenter<View>{
        fun getModel(mark: String?, searchModel: Spinner?)
        fun getCity(region: String?, searchCity: Spinner?)
    }
}