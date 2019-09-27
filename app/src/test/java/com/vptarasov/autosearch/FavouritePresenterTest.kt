package com.vptarasov.autosearch

import com.nhaarman.mockito_kotlin.mock
import com.vptarasov.autosearch.ui.fragments.favourite.FavouriteContract
import com.vptarasov.autosearch.ui.fragments.favourite.FavouritePresenter
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class FavouritePresenterTest {
   private lateinit var view: FavouriteContract.View
    private var favouritePresenter = FavouritePresenter()

    @Before
    fun setUp() {
        view = mock()
        favouritePresenter.attach(view)
    }

    @Test
    fun loadFavouriteCars() {
        favouritePresenter.loadFavouriteCars()
        Mockito.verify(view).showProgress()

    }

}