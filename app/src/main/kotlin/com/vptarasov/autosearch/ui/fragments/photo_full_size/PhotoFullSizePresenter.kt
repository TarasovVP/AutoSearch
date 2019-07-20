package com.vptarasov.autosearch.ui.fragments.photo_full_size

import io.reactivex.disposables.CompositeDisposable

class PhotoFullSizePresenter : PhotoFullSizeContract.Presenter {

    private val subscriptions = CompositeDisposable()
    private lateinit var view: PhotoFullSizeContract.View

    override fun attach(view: PhotoFullSizeContract.View) {
        this.view = view
    }

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }


}