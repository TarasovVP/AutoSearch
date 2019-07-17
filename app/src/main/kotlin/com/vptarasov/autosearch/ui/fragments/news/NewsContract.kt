package com.vptarasov.autosearch.ui.fragments.news

import com.vptarasov.autosearch.ui.AppContract

class NewsContract {

    interface View: AppContract.View

    interface Presenter: AppContract.Presenter<View>
}