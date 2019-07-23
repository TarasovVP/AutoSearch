package com.vptarasov.autosearch.ui.fragments.favourite

import com.vptarasov.autosearch.database.HelperFactory
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

    override fun loadFavouriteCars(): List<Car>? {
        return HelperFactory.helper?.getFavoritesDao()?.loadAll()
    }

}