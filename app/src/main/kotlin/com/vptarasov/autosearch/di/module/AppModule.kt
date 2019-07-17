package com.vptarasov.autosearch.di.module

import android.app.Application
import com.vptarasov.autosearch.App
import com.vptarasov.autosearch.di.scope.PerApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val app: App) {
    @Provides
    @Singleton
    @PerApplication
    fun provideApplication(): Application {
        return app
    }
}