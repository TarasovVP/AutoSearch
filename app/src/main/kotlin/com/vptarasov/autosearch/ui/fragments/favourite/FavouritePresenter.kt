package com.vptarasov.autosearch.ui.fragments.favourite

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.vptarasov.autosearch.model.Car
import io.reactivex.disposables.CompositeDisposable

class FavouritePresenter : FavouriteContract.Presenter {

    private val subscriptions = CompositeDisposable()
    private lateinit var view: FavouriteContract.View

    override fun attach(view: FavouriteContract.View) {
        this.view = view
    }

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun loadFavouriteCars() {
        view.showProgress()
        val userId = FirebaseAuth.getInstance()
            .currentUser?.uid

        FirebaseFirestore.getInstance().collection("car").whereEqualTo("user", userId)
            .get()
            .addOnSuccessListener { result ->
                val cars: ArrayList<Car> = ArrayList()
                for (document in result) {
                    val car = document.toObject(Car::class.java)
                    cars.add(car)

                }
                view.initAdapter(cars)
                view.hideProgress()
            }
            .addOnFailureListener {
                view.hideProgress()
            }
    }


}