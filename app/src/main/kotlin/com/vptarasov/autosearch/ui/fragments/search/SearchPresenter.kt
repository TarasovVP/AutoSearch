package com.vptarasov.autosearch.ui.fragments.search

import io.reactivex.disposables.CompositeDisposable

class SearchPresenter: SearchContract.Presenter {
    override fun attach(view: SearchContract.View) {

    }


    private val subscriptions = CompositeDisposable()
    private lateinit var view: SearchContract.View

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }


}