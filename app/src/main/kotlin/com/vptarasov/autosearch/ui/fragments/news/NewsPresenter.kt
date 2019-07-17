package com.vptarasov.autosearch.ui.fragments.news

import io.reactivex.disposables.CompositeDisposable

class NewsPresenter: NewsContract.Presenter {
    override fun attach(view: NewsContract.View) {

    }


    private val subscriptions = CompositeDisposable()
    private lateinit var view: NewsContract.View

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }


}