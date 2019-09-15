package com.vptarasov.autosearch

import android.util.Log
import android.view.View
import com.vptarasov.autosearch.model.Car
import com.vptarasov.autosearch.ui.fragments.car.CarContract
import com.vptarasov.autosearch.ui.fragments.car.CarPresenter
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class CarPresenterTest {

    private val view = TestCarPresenter()
    private var carPresenter = CarPresenter(Dispatchers.Unconfined)
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @Before
    fun setUp() {
        Log.d("Test", "TestSetUp")
        view.reset()
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @Test
    fun loadCar() = runBlocking {

        launch(Dispatchers.IO) {
            Assert.assertEquals("Checking initViewCounter", 0, view.initViewCounter)
            Assert.assertEquals("Checking setDataToViewsCounter", 0, view.setDataToViewsCounter)
            Assert.assertEquals("Checking showViewsCounter", 1, view.showViewsCounter)

        }
        carPresenter.attach(view)
        carPresenter.loadCar("/car/odesskaya-oblast/odessa/nissan/micra/cabriolet-2006-785646.html")

    }

    class TestCarPresenter : CarContract.View {

        var initViewCounter = 0
        var setDataToViewsCounter = 0
        var showViewsCounter = 0

        fun reset() {
            initViewCounter = 0
            setDataToViewsCounter = 0
        }

        override fun initView(view: View) {
            initViewCounter++
        }

        override fun setDataToViews(car: Car) {
            setDataToViewsCounter++
        }

        override fun onFavoriteClick(car: Car) {

        }

        override fun showphotoFullSizeFragment(car: Car) {

        }
        override fun showView() {
            showViewsCounter++
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }
}