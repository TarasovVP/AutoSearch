package com.vptarasov.autosearch.ui.fragments.car

import com.vptarasov.autosearch.App
import com.vptarasov.autosearch.api.GetResponseBody
import com.vptarasov.autosearch.api.HTMLParser
import com.vptarasov.autosearch.model.Car
import com.vptarasov.autosearch.ui.activity.splash_screen.SplashScreenActivity
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.*
import java.util.*
import java.util.logging.Logger
import kotlin.coroutines.CoroutineContext

class CarPresenter(private val uiContext: CoroutineContext = Dispatchers.Main) :
    CarContract.Presenter, CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = uiContext + job


    private val subscriptions = CompositeDisposable()
    private lateinit var view: CarContract.View
    private var job: Job = Job()

    lateinit var name: String

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: CarContract.View) {
        this.view = view
    }

    override fun loadCar(urlCar: String?) {

        val getResponseBody = GetResponseBody()

        name = "no launch entering"
        launch {
            withContext(Dispatchers.IO) {
                try {
                    val result = getResponseBody.loadCar(urlCar.toString())

                    val htmlParser = HTMLParser()
                    val car = htmlParser.getCar(result.responseBody.toString())
                    car.url = urlCar
                    name = car.name.toString()

                    loadFavouriteCars(car)
                } catch (e: Exception) {

                    Logger.getLogger(SplashScreenActivity::class.java.name)
                        .warning("Exception in CarFragment")
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
                view.setDataToViews(car)
            }
            ?.addOnFailureListener {
                Logger.getLogger(SplashScreenActivity::class.java.name)
                    .warning("Exception in loadFavouriteCars")
            }
    }

}