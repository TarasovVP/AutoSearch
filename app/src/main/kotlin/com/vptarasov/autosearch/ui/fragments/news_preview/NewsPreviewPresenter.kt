package com.vptarasov.autosearch.ui.fragments.news_preview

import android.annotation.SuppressLint
import com.vptarasov.autosearch.api.Api
import com.vptarasov.autosearch.api.HTMLParser
import com.vptarasov.autosearch.util.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class NewsPreviewPresenter : NewsPreviewContract.Presenter {

    private val subscriptions = CompositeDisposable()
    private lateinit var view: NewsPreviewContract.View

    override fun attach(view: NewsPreviewContract.View) {
        this.view = view
    }

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    @SuppressLint("CheckResult")
    override fun loadNewsPreview() {
        val subscribe = Api
            .service
            .loadUrl(Constants.NEWS_URL + Constants.ARTICLES)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ responseBody ->
                val htmlParser = HTMLParser()
                val news = htmlParser.getNewsPreviews(responseBody.string())
                view.initAdapter(news)

            }, { throwable -> throwable.printStackTrace() })
        subscriptions.add(subscribe)
    }


}