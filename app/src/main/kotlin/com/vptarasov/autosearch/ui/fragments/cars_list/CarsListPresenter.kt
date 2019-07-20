package com.vptarasov.autosearch.ui.fragments.cars_list

import android.annotation.SuppressLint
import com.vptarasov.autosearch.R
import com.vptarasov.autosearch.api.Api
import com.vptarasov.autosearch.api.HTMLParser
import com.vptarasov.autosearch.model.QueryDetails
import com.vptarasov.autosearch.ui.fragments.base.BaseFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CarsListPresenter : CarsListContract.Presenter, BaseFragment() {

    private val subscriptions = CompositeDisposable()
    private lateinit var view: CarsListContract.View

    override fun attach(view: CarsListContract.View) {
        this.view = view
    }

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    @SuppressLint("CheckResult")
    override fun loadCars(queryDetails: QueryDetails?, page: Int) {
        setLoading(true)
        val subscribe = Api
            .service
            .loadCars(
                queryDetails?.mark,
                queryDetails?.model,
                queryDetails?.region,
                queryDetails?.city,
                queryDetails?.body,
                queryDetails?.color,
                queryDetails?.engineFrom,
                queryDetails?.engineUnit,
                queryDetails?.yearFrom,
                queryDetails?.engineTo,
                queryDetails?.yearTo,
                queryDetails?.priceFrom,
                queryDetails?.priceTo,
                queryDetails?.petrolElectro,
                queryDetails?.diesel,
                queryDetails?.electro,
                queryDetails?.gas,
                queryDetails?.gasPetrol,
                queryDetails?.petrol,
                queryDetails?.gearboxAutom,
                queryDetails?.gearboxMech,
                page.toString()
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ responseBody ->
                val htmlParser = HTMLParser()
                val html = responseBody.string()
                val cars = htmlParser.getCarList(html)
                val lastPage = htmlParser.getLastPage(html)
                view.getLastPage(lastPage)
                if (cars.size > 0) {
                    view.initAdapter(cars, lastPage)
                } else {
                    showErrorMessage(R.string.nothing_found)
                }
                setLoading(false)

            }, { throwable ->
                throwable.printStackTrace()
                setLoading(true)
            })
        subscriptions.add(subscribe)
    }

}