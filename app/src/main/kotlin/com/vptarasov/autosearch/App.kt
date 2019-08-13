package com.vptarasov.autosearch

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import com.vptarasov.autosearch.di.component.AppComponent
import com.vptarasov.autosearch.di.component.DaggerAppComponent
import com.vptarasov.autosearch.di.module.AppModule

class App : Application() {

    lateinit var component: AppComponent

    override fun onCreate() {
        super.onCreate()
        instance = this
        setup()

    }

    private fun setup() {
        component = DaggerAppComponent.builder()
            .appModule(AppModule(this)).build()
        component.inject(this)
    }


    companion object {
        var instance: App? = null
            private set
    }

    fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }


}