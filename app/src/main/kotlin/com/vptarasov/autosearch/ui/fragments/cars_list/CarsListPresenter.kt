package com.vptarasov.autosearch.ui.fragments.cars_list

import android.annotation.SuppressLint
import com.google.firebase.firestore.FirebaseFirestore
import com.vptarasov.autosearch.App
import com.vptarasov.autosearch.R
import com.vptarasov.autosearch.api.GetResponseBody
import com.vptarasov.autosearch.api.HTMLParser
import com.vptarasov.autosearch.model.Car
import com.vptarasov.autosearch.model.QueryDetails
import com.vptarasov.autosearch.ui.fragments.base.BaseFragment
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.*

class CarsListPresenter : CarsListContract.Presenter, BaseFragment() {

    private val subscriptions = CompositeDisposable()
    private lateinit var view: CarsListContract.View
    private lateinit var cars: ArrayList<Car>
    private var lastPage: Int = 1

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
        view.showProgress()
        val viewModelJob = Job()
        val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)
        val getResponseBody = GetResponseBody()

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val result = getResponseBody.loadCars(
                    GetResponseBody.Params(), queryDetails!!.mark,
                    queryDetails.model,
                    queryDetails.region,
                    queryDetails.city,
                    queryDetails.body,
                    queryDetails.color,
                    queryDetails.engineFrom,
                    queryDetails.engineUnit,
                    queryDetails.yearFrom,
                    queryDetails.engineTo,
                    queryDetails.yearTo,
                    queryDetails.priceFrom,
                    queryDetails.priceTo,
                    queryDetails.petrolElectro,
                    queryDetails.diesel,
                    queryDetails.electro,
                    queryDetails.gas,
                    queryDetails.gasPetrol,
                    queryDetails.petrol,
                    queryDetails.gearboxAutom,
                    queryDetails.gearboxMech,
                    page.toString()
                )
                val htmlParser = HTMLParser()
                cars = htmlParser.getCarList(result.responseBody.toString())
                lastPage = htmlParser.getLastPage(result.responseBody.toString())
                view.getLastPage(lastPage)
                if (cars.size > 0) {
                    loadFavouriteCars()
                } else {
                    showErrorMessage(R.string.nothing_found)
                }
            }
        }
    }

    override fun loadFavouriteCars() {

        FirebaseFirestore.getInstance().collection("user")
            .document(App.instance!!.user.id).collection(("cars"))
            .get()
            .addOnSuccessListener { result ->
                val favouriteCars: ArrayList<Car> = ArrayList()
                for (document in result) {
                    val carFavourite = document.toObject(Car::class.java)
                    favouriteCars.add(carFavourite)
                    for (car in cars) {
                        if (car.urlToId() == carFavourite.urlToId()) {
                            car.setBookmarked(true)
                        }
                    }
                }
                view.initAdapter(cars, lastPage)
                view.hideProgress()
            }
            .addOnFailureListener {
                view.hideProgress()
            }
    }

}