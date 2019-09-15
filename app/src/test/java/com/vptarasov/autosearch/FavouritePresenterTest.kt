package com.vptarasov.autosearch

import android.content.Context
import com.vptarasov.autosearch.model.Car
import com.vptarasov.autosearch.ui.fragments.favourite.FavouriteContract
import com.vptarasov.autosearch.ui.fragments.favourite.FavouritePresenter
import com.vptarasov.autosearch.utils.getTestContext
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class FavouritePresenterTest {
    private val view = TestFavouritePresenter()
    private var favouritePresenter = FavouritePresenter()

    @Before
    fun setUp() {
        view.reset()
    }

    @Test
    fun loadFavouriteCars() {
        favouritePresenter.attach(view)
        favouritePresenter.loadFavouriteCars()
        Assert.assertEquals("Checking showProgressCounter", 1, view.showProgressCounter)
        Assert.assertEquals("Checking hideProgressCounter", 0, view.hideProgressCounter)

    }

    class TestFavouritePresenter : FavouriteContract.View {

        var showProgressCounter = 0
        var hideProgressCounter = 0


        fun reset() {

            showProgressCounter = 0
            hideProgressCounter = 0
        }

        override fun initAdapter(cars: ArrayList<Car>) {

        }

        override fun onItemClick(car: Car) {

        }

        override fun onFavoriteClick(car: Car) {

        }

        override fun onLastFavoriteRemoved() {

        }

        override fun showProgress() {
            showProgressCounter++
        }

        override fun hideProgress() {
            hideProgressCounter++
        }

        override fun getContext(): Context? {
            return getTestContext()
        }

    }
}