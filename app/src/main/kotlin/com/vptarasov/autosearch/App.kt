package com.vptarasov.autosearch

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import com.vptarasov.autosearch.database.HelperFactory

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        HelperFactory.setHelper(applicationContext)
    }

    override fun onTerminate() {
        HelperFactory.releaseHelper()
        super.onTerminate()
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