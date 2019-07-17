package com.vptarasov.autosearch.ui.fragments.favourite

import io.reactivex.disposables.CompositeDisposable

class FavouritePresenter: FavouriteContract.Presenter {
    override fun attach(view: FavouriteContract.View) {

    }


    private val subscriptions = CompositeDisposable()
    private lateinit var view: FavouriteContract.View

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }


}