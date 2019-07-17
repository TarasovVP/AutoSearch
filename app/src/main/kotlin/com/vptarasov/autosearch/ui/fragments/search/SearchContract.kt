package com.vptarasov.autosearch.ui.fragments.search

import com.vptarasov.autosearch.ui.AppContract

class SearchContract {

    interface View: AppContract.View

    interface Presenter: AppContract.Presenter<View>
}