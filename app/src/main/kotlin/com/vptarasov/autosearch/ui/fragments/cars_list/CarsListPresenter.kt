package com.vptarasov.autosearch.ui.fragments.cars_list

import io.reactivex.disposables.CompositeDisposable

class CarsListPresenter: CarsListContract.Presenter {
    override fun attach(view: CarsListContract.View) {

    }


    private val subscriptions = CompositeDisposable()
    private lateinit var view: CarsListContract.View

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }


}