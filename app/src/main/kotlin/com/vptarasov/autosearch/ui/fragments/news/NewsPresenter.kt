package com.vptarasov.autosearch.ui.fragments.news

import android.annotation.SuppressLint
import com.vptarasov.autosearch.api.Api
import com.vptarasov.autosearch.api.HTMLParser
import com.vptarasov.autosearch.util.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class NewsPresenter : NewsContract.Presenter {

    private val subscriptions = CompositeDisposable()
    private lateinit var view: NewsContract.View

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: NewsContract.View) {
        this.view = view
    }

    @SuppressLint("CheckResult")
    override fun loadNews(url: String){
       val subscribe =  Api
            .service
            .loadUrl(Constants.NEWS_URL + url.substring(1))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({responseBody ->
                val html = responseBody.string()
                val htmlParser = HTMLParser()
                val news = htmlParser.getNews(html)
                view.setDataToViews(news)
            }, { throwable -> throwable.printStackTrace() })
        subscriptions.add(subscribe)
    }


}