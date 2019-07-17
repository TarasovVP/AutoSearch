package com.vptarasov.autosearch.ui.fragments.car

import io.reactivex.disposables.CompositeDisposable

class CarPresenter: CarContract.Presenter {
    override fun attach(view: CarContract.View) {

    }


    private val subscriptions = CompositeDisposable()
    private lateinit var view: CarContract.View

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }


}