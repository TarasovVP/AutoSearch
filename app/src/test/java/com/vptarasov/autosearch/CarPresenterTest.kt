package com.vptarasov.autosearch

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.vptarasov.autosearch.api.GetResponseBody
import com.vptarasov.autosearch.api.HTMLParser
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
import org.mockito.Mockito

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class CarPresenterTest {

    private val CAR_URL = "/car/odesskaya-oblast/odessa/nissan/micra/cabriolet-2006-785646.html"

    private lateinit var view: CarContract.View
    private var carPresenter = CarPresenter()
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")
    private var getResponseBody = GetResponseBody()
    private var carName: String = ""


    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
        view = mock()
        carPresenter.attach(view)
    }

    @Test
    fun loadCar() = runBlocking<Unit> {

        launch{

            carPresenter.loadCar(CAR_URL)

            Mockito.verify(view, times(0)).onFavoriteClick(car = Car())

            delay(10000)
            //Mockito.verify(view, times(1)).setDataToViews(car= Car())

            Assert.assertEquals("AssertCarName", "Nissan Micra Cabrio", carPresenter.name)
        }


    }

    @Test
    fun getCarfromApi() = runBlocking {

        val htmlParser = HTMLParser()
        val response = getResponseBody.loadCar(CAR_URL).responseBody.toString()

        val car = htmlParser.getCar(response)
        carName = car.name.toString()

        Assert.assertEquals("Assert carName", "Nissan Micra Cabrio", carName)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }
}