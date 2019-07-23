package com.vptarasov.autosearch

import com.vptarasov.autosearch.ui.fragments.favourite.FavouritePresenter
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun loadFavouriteCars() {
        val favouritePresenter = FavouritePresenter()

        val cars = favouritePresenter.loadFavouriteCars()
        assertEquals(3, cars?.size)

    }

}
