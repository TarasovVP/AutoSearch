package com.vptarasov.autosearch.ui.fragments

import com.vptarasov.autosearch.App
import com.vptarasov.autosearch.model.Car
import com.vptarasov.autosearch.ui.AppContract
import com.vptarasov.autosearch.ui.activity.splash_screen.SplashScreenActivity
import java.util.logging.Logger


abstract class BasePresenter<T> : AppContract.Presenter<T> {

    private var view: T? = null

    override fun attach(view: T) {
        this.view = view
    }

    fun getView(): T? = view

    fun loadFavouriteCars() {

        App.instance?.firebaseFirestore?.collection("user")
            ?.document(App.instance!!.user.id)?.collection(("cars"))
            ?.get()
            ?.addOnSuccessListener { result ->
                val cars: ArrayList<Car> = ArrayList()
                for (document in result) {
                    val car = document.toObject(Car::class.java)
                    cars.add(car)
                }
                getFavouriteCars()
            }
            ?.addOnFailureListener {
                Logger.getLogger(SplashScreenActivity::class.java.name)
                    .warning("Exception in loadFavouriteCars")
            }
    }

    fun getFavouriteCars(): ArrayList<Car> = ArrayList()

}