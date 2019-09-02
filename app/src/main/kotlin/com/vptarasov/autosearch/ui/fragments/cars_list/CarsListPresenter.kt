package com.vptarasov.autosearch.ui.fragments.cars_list

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import com.vptarasov.autosearch.App
import com.vptarasov.autosearch.api.GetResponseBody
import com.vptarasov.autosearch.api.HTMLParser
import com.vptarasov.autosearch.model.Car
import com.vptarasov.autosearch.model.QueryDetails
import com.vptarasov.autosearch.ui.activity.splash_screen.SplashScreenActivity
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.*
import java.util.logging.Logger

class CarsListPresenter : CarsListContract.Presenter, Fragment() {

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
                try {
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
                        withContext(Dispatchers.Main) {
                            view.hideProgress()
                            view.showNothingFoundText()
                        }
                    }
                } catch (e: Exception) {
                    Logger.getLogger(SplashScreenActivity::class.java.name).warning("Error..")
                    withContext(Dispatchers.Main) {
                        view.hideProgress()
                        view.showNothingFoundText()
                    }
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