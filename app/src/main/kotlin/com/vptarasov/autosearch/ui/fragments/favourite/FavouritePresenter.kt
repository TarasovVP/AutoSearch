package com.vptarasov.autosearch.ui.fragments.favourite

import com.vptarasov.autosearch.App
import com.vptarasov.autosearch.model.Car
import com.vptarasov.autosearch.ui.fragments.BasePresenter

class FavouritePresenter : BasePresenter<FavouriteContract.View>(), FavouriteContract.Presenter {

     override fun loadFavouriteCars() {

        App.instance?.firebaseFirestore?.collection("user")
            ?.document(App.instance!!.user.id)?.collection(("cars"))
            ?.get()
            ?.addOnSuccessListener { result ->
                val cars: ArrayList<Car> = ArrayList()
                for (document in result) {
                    val car = document.toObject(Car::class.java)
                    cars.add(car)

                }
                getView()?.initAdapter(cars)
                getView()?.hideProgress()
            }
            ?.addOnFailureListener {
                getView()?.hideProgress()
            }
    }
}
