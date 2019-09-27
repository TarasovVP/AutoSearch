package com.vptarasov.autosearch.ui.fragments.car

import com.vptarasov.autosearch.App
import com.vptarasov.autosearch.api.GetResponseBody
import com.vptarasov.autosearch.api.HTMLParser
import com.vptarasov.autosearch.model.Car
import com.vptarasov.autosearch.ui.activity.splash_screen.SplashScreenActivity
import com.vptarasov.autosearch.ui.fragments.BaseCarPresenter
import kotlinx.coroutines.*
import java.util.*
import java.util.logging.Logger
import kotlin.coroutines.CoroutineContext

class CarPresenter(private val uiContext: CoroutineContext = Dispatchers.Main) : BaseCarPresenter<CarContract.View>(),
    CarContract.Presenter, CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = uiContext +  Job()

    override fun loadCar(urlCar: String?) {

        val getResponseBody = GetResponseBody()

        launch {
            withContext(Dispatchers.IO) {
                try {
                    val result = getResponseBody.loadCar(urlCar.toString())

                    val htmlParser = HTMLParser()
                    val car = htmlParser.getCar(result.responseBody.toString())
                    car.url = urlCar
                    loadFavouriteCars(car)
                } catch (e: Exception) {

                    Logger.getLogger(SplashScreenActivity::class.java.name)
                        .warning("Exception in CarPresenter")
                }
            }
        }
    }

    override fun loadFavouriteCars(car: Car) {

        val doc = App.instance?.firebaseFirestore?.collection("user")
            ?.document(App.instance!!.user.id)?.collection(("cars"))

        doc
            ?.get()
            ?.addOnSuccessListener { result ->
                val favouriteCars: ArrayList<Car> = ArrayList()
                for (document in result) {
                    val carFavourite = document.toObject(Car::class.java)
                    favouriteCars.add(carFavourite)

                }
                for (carFavourite in favouriteCars) {
                    if (car.urlToId() == carFavourite.urlToId()) {
                        car.setBookmarked(true)
                    }
                }
                getView()?.setDataToViews(car)
            }
            ?.addOnFailureListener {
                Logger.getLogger(SplashScreenActivity::class.java.name)
                    .warning("Exception in loadFavouriteCars")
            }
    }

}