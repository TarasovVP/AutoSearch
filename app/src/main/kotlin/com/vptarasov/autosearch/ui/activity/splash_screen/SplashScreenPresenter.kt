package com.vptarasov.autosearch.ui.activity.splash_screen

import io.reactivex.disposables.CompositeDisposable

class SplashScreenPresenter : SplashScreenContract.Presenter {

    private val subscriptions = CompositeDisposable()
    private lateinit var view: SplashScreenContract.View

    override fun attach(view: SplashScreenContract.View) {
        this.view = view
        view.showLoadingFragment()
    }


    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }
}