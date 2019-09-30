package com.vptarasov.autosearch.ui.activity.splash_screen

import com.vptarasov.autosearch.api.GetResponseBody
import com.vptarasov.autosearch.api.HTMLParser
import com.vptarasov.autosearch.model.SearchData
import com.vptarasov.autosearch.ui.fragments.BasePresenter
import kotlinx.coroutines.*
import java.util.logging.Logger

class SplashScreenPresenter : BasePresenter<SplashScreenContract.View>(), SplashScreenContract.Presenter {

    override fun loadSearchData() {
        val viewModelJob = Job()
        val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)
        val getResponseBody = GetResponseBody()

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                var result: GetResponseBody.Result? = null
                try {
                    result = getResponseBody.loadSearchData("")
                } catch (e: Exception) {
                    Logger.getLogger(SplashScreenActivity::class.java.name).warning("Exception in SplashScreenFragment")
                    getView()?.showMainActivity(SearchData())
                }

                val htmlParser = HTMLParser()
                val searchData = htmlParser.getSearchData(result?.responseBody.toString())
                getView()?.showMainActivity(searchData)
            }
        }
    }
}