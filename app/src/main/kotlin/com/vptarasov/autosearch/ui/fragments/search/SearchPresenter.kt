package com.vptarasov.autosearch.ui.fragments.search

import android.annotation.SuppressLint
import android.widget.Spinner
import com.vptarasov.autosearch.api.Api
import com.vptarasov.autosearch.api.HTMLParser
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*

class SearchPresenter : SearchContract.Presenter {


    private val subscriptions = CompositeDisposable()
    private lateinit var view: SearchContract.View

    override fun attach(view: SearchContract.View) {
        this.view = view
    }

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    @SuppressLint("CheckResult")
    override fun getModel(mark: String?, searchModel: Spinner?) {
        Api
            .service
            .getModel(mark)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ responseBody ->
                val htmlParser = HTMLParser()
                val model = htmlParser.getModel(responseBody.string())
                view.fulfillSpinner(ArrayList(model.model!!.values), searchModel)

            }, { throwable -> throwable.printStackTrace() })
    }

    @SuppressLint("CheckResult")
    override fun getCity(region: String?, searchCity: Spinner?) {
        Api
            .service
            .getCity(region)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ responseBody ->
                val htmlParser = HTMLParser()
                val city = htmlParser.getCity(responseBody.string())
                view.fulfillSpinner(ArrayList(city.city!!.values), searchCity)

            }, { throwable -> throwable.printStackTrace() })
    }


}