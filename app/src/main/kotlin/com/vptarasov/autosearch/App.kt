package com.vptarasov.autosearch

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import com.vptarasov.autosearch.database.HelperFactory
import com.vptarasov.autosearch.di.component.AppComponent
import com.vptarasov.autosearch.di.component.DaggerAppComponent
import com.vptarasov.autosearch.di.module.AppModule

class App : Application() {

    lateinit var component: AppComponent

    override fun onCreate() {
        super.onCreate()
        instance = this
        setup()
        HelperFactory.setHelper(applicationContext)

    }
    private fun setup() {
        component = DaggerAppComponent.builder()
            .appModule(AppModule(this)).build()
        component.inject(this)
    }

    fun getApplicationComponent(): AppComponent {
        return component
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