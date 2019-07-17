package com.vptarasov.autosearch.ui.fragments.loading

import com.vptarasov.autosearch.model.SearchData
import com.vptarasov.autosearch.ui.AppContract

class LoadingContract {

    interface View: AppContract.View

    interface Presenter: AppContract.Presenter<View>{
        fun loadData()
        fun getSearchData(): SearchData?
    }
}