package com.vptarasov.autosearch.ui.fragments.search

import android.widget.Spinner
import com.vptarasov.autosearch.api.GetResponseBody
import com.vptarasov.autosearch.api.HTMLParser
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.*
import java.util.*

class SearchPresenter : SearchContract.Presenter {


    private val subscriptions = CompositeDisposable()
    private lateinit var view: SearchContract.View

    override fun attach(view: SearchContract.View) {
        this.view = view
    }

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun getModel(mark: String?, searchModel: Spinner?) {
        val viewModelJob = Job()
        val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)
        val getResponseBody = GetResponseBody()

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val result = getResponseBody.getModel(
                    GetResponseBody.Params(), mark.toString())

                val htmlParser = HTMLParser()
                val model = htmlParser.getModel(result.responseBody.toString())
                withContext(Dispatchers.Main) {
                    view.fulfillSpinner(ArrayList(model.model!!.values), searchModel)
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
                val result = getResponseBody.getCity(
                    GetResponseBody.Params(), region.toString())
                val htmlParser = HTMLParser()
                val city = htmlParser.getCity(result.responseBody.toString())

                withContext(Dispatchers.Main) {
                    view.fulfillSpinner(ArrayList(city.city!!.values), searchCity)
                }
            }
        }
    }


}