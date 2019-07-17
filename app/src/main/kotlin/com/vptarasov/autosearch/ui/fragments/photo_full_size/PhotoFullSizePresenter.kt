package com.vptarasov.autosearch.ui.fragments.photo_full_size

import io.reactivex.disposables.CompositeDisposable

class PhotoFullSizePresenter: PhotoFullSizeContract.Presenter {
    override fun attach(view: PhotoFullSizeContract.View) {

    }


    private val subscriptions = CompositeDisposable()
    private lateinit var view: PhotoFullSizeContract.View

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }


}