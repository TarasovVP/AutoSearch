package com.vptarasov.autosearch

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.vptarasov.autosearch.di.component.AppComponent
import com.vptarasov.autosearch.di.component.DaggerAppComponent
import com.vptarasov.autosearch.model.User

class App : Application() {

    lateinit var component: AppComponent
    lateinit var user: User
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var firebaseFirestore: FirebaseFirestore

    override fun onCreate() {
        super.onCreate()
        instance = this
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseFirestore = FirebaseFirestore.getInstance()
        user = User(id = "", name = "", email = "", photoUrl = "", cars = ArrayList())
        setup()

    }

    private fun setup() {
        component = DaggerAppComponent.builder().build()
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