package com.vptarasov.autosearch.ui.fragments.cars_list

import android.annotation.SuppressLint
import com.vptarasov.autosearch.App
import com.vptarasov.autosearch.api.GetResponseBody
import com.vptarasov.autosearch.api.HTMLParser
import com.vptarasov.autosearch.model.Car
import com.vptarasov.autosearch.model.QueryDetails
import com.vptarasov.autosearch.ui.activity.splash_screen.SplashScreenActivity
import com.vptarasov.autosearch.ui.fragments.BasePresenter
import kotlinx.coroutines.*
import java.util.logging.Logger

class CarsListPresenter : BasePresenter<CarsListContract.View>(), CarsListContract.Presenter{

    @SuppressLint("CheckResult")
    override fun loadCars(queryDetails: QueryDetails?, page: Int) {
        val viewModelJob = Job()
        val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)
        val getResponseBody = GetResponseBody()

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val result = getResponseBody.loadCars(
                        queryDetails!!.mark,
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
                    val cars = htmlParser.getCarList(result.responseBody.toString())
                    val lastPage = htmlParser.getLastPage(result.responseBody.toString())
                    getView()?.getLastPage(lastPage)
                    if (cars.size > 0) {
                        loadFavouriteCars(cars, lastPage)
                    } else {
                        withContext(Dispatchers.Main) {
                            if (App.instance?.isNetworkAvailable()!!) {
                                getView()?.showNothingFoundText()
                            }
                        }
                    }
                } catch (e: Exception) {
                    Logger.getLogger(SplashScreenActivity::class.java.name).warning("Exception in CarsListFragment")
                    withContext(Dispatchers.Main) {
                        if (App.instance?.isNetworkAvailable()!!) {
                            getView()?.showNothingFoundText()
                        }
                    }
                }
            }
        }
    }

    override fun loadFavouriteCars(cars: ArrayList<Car>, lastPage: Int) {

        App.instance?.firebaseFirestore?.collection("user")
            ?.document(App.instance!!.user.id)?.collection(("cars"))
            ?.get()
            ?.addOnSuccessListener { result ->
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
                getView()?.initAdapter(cars, lastPage)
            }
            ?.addOnFailureListener {
            }
    }

}