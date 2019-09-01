package com.vptarasov.autosearch.ui.activity.splash_screen

import com.vptarasov.autosearch.api.GetResponseBody
import com.vptarasov.autosearch.api.HTMLParser
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.*

class SplashScreenPresenter : SplashScreenContract.Presenter {

    private val subscriptions = CompositeDisposable()
    private lateinit var view: SplashScreenContract.View

    override fun attach(view: SplashScreenContract.View) {
        this.view = view
    }


    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun loadSearchData() {
        val viewModelJob = Job()
        val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)
        val getResponseBody = GetResponseBody()

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val result = getResponseBody.loadSearchData(
                    GetResponseBody.Params(), "")

                val htmlParser = HTMLParser()
                val searchData = htmlParser.getSearchData(result.responseBody.toString())
                view.showMainActivity(searchData)
            }
        }
    }
}