package com.vptarasov.autosearch.ui.fragments.loading

import android.annotation.SuppressLint
import com.vptarasov.autosearch.api.Api
import com.vptarasov.autosearch.model.SearchData
import com.vptarasov.autosearch.parser.HTMLParser
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class LoadingPresenter : LoadingContract.Presenter {


    private val subscriptions = CompositeDisposable()
    private lateinit var view: LoadingContract.View
    private var searchData: SearchData? = null

    @SuppressLint("CheckResult")
    override fun loadData() {
        Api.service.loadData("")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ responseBody ->
                val html = responseBody.string()
                val htmlParser = HTMLParser()
                searchData = htmlParser.getSearchData(html)

            }, { throwable -> throwable.printStackTrace() })
    }

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: LoadingContract.View) {

    }

    override fun getSearchData(): SearchData? {
        return searchData
    }
}