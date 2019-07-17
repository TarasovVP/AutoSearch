package com.vptarasov.autosearch.ui.fragments.news_preview

import io.reactivex.disposables.CompositeDisposable

class NewsPreviewPresenter: NewsPreviewContract.Presenter {
    override fun attach(view: NewsPreviewContract.View) {

    }


    private val subscriptions = CompositeDisposable()
    private lateinit var view: NewsPreviewContract.View

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }


}