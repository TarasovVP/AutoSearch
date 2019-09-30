package com.vptarasov.autosearch.ui.fragments.search

import android.widget.Spinner
import com.vptarasov.autosearch.api.GetResponseBody
import com.vptarasov.autosearch.api.HTMLParser
import com.vptarasov.autosearch.ui.activity.splash_screen.SplashScreenActivity
import com.vptarasov.autosearch.ui.fragments.BasePresenter
import kotlinx.coroutines.*
import java.util.*
import java.util.logging.Logger

class SearchPresenter : BasePresenter<SearchContract.View>(), SearchContract.Presenter {

    override fun getModel(mark: String?, searchModel: Spinner?) {
        val viewModelJob = Job()
        val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)
        val getResponseBody = GetResponseBody()

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                val result = getResponseBody.getModel(mark.toString())

                val htmlParser = HTMLParser()
                val model = htmlParser.getModel(result.responseBody.toString())
                withContext(Dispatchers.Main) {
                    getView()?.fulfillSpinner(ArrayList(model.model!!.values), searchModel)
                }
                } catch (e: Exception) {
                    Logger.getLogger(SplashScreenActivity::class.java.name).warning("Exception in getModel in SearchFragment")
                }

            }
        }
    }

    override fun getCity(region: String?, searchCity: Spinner?) {
        val viewModelJob = Job()
        val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)
        val getResponseBody = GetResponseBody()

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                val result = getResponseBody.getCity(region.toString())
                val htmlParser = HTMLParser()
                val city = htmlParser.getCity(result.responseBody.toString())

                withContext(Dispatchers.Main) {
                    getView()?.fulfillSpinner(ArrayList(city.city!!.values), searchCity)
                }
                } catch (e: Exception) {
                    Logger.getLogger(SplashScreenActivity::class.java.name).warning("Exception in getCity in SearchFragment")
                }
            }
        }
    }


}