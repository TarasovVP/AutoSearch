package com.vptarasov.autosearch.ui.fragments.car

import com.google.firebase.firestore.FirebaseFirestore
import com.vptarasov.autosearch.App
import com.vptarasov.autosearch.api.GetResponseBody
import com.vptarasov.autosearch.api.HTMLParser
import com.vptarasov.autosearch.model.Car
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.*

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

    override fun loadCar(urlCar: String?) {
        val viewModelJob = Job()
        val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)
        val getResponseBody = GetResponseBody()

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val result = getResponseBody.loadCar(
                    GetResponseBody.Params(), urlCar.toString()
                )

                val htmlParser = HTMLParser()
                val car = htmlParser.getCar(result.responseBody.toString())
                car.url = urlCar

                loadFavouriteCars(car)

            }
        }
    }

    override fun loadFavouriteCars(car: Car) {

        val doc = FirebaseFirestore.getInstance().collection("user")
            .document(App.instance!!.user.id).collection(("cars"))

        doc
            .get()
            .addOnSuccessListener { result ->
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
            .addOnFailureListener {

            }
    }


}