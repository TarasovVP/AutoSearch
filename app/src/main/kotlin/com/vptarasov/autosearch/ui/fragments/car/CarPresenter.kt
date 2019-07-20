package com.vptarasov.autosearch.ui.fragments.car

import android.annotation.SuppressLint
import com.vptarasov.autosearch.api.Api
import com.vptarasov.autosearch.api.HTMLParser
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CarPresenter : CarContract.Presenter {


    private val subscriptions = CompositeDisposable()
    private lateinit var view: CarContract.View

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: CarContract.View) {
        this.view = view
    }

    @SuppressLint("CheckResult")
    override fun loadCar(url: String?) {
        val subscribe = Api
            .service
            .loadUrl(url)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ responseBody ->

                val html = responseBody.string()
                val htmlParser = HTMLParser()
                val car = htmlParser.getCar(html)
                car.url = url

                view.setDataToViews(car)

            }, { throwable -> throwable.printStackTrace() })
        subscriptions.add(subscribe)
    }


}