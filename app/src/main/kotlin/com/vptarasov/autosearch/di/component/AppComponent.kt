package com.vptarasov.autosearch.di.component

import com.vptarasov.autosearch.App
import com.vptarasov.autosearch.di.module.AppModule
import dagger.Component

@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(application: App)
}
