package com.vptarasov.autosearch.ui.fragments.news

class NewsPresenter : NewsContract.Presenter {

    private lateinit var view: NewsContract.View

    override fun attach(view: NewsContract.View) {
        this.view = view
    }


}