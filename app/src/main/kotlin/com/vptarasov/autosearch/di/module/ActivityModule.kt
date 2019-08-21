package com.vptarasov.autosearch.di.module

import android.app.Activity
import com.vptarasov.autosearch.ui.activity.main.MainContract
import com.vptarasov.autosearch.ui.activity.main.MainPresenter
import com.vptarasov.autosearch.ui.activity.splash_screen.SplashScreenContract
import com.vptarasov.autosearch.ui.activity.splash_screen.SplashScreenPresenter
import com.vptarasov.autosearch.ui.activity.user.UserContract
import com.vptarasov.autosearch.ui.activity.user.UserPresenter
import dagger.Module
import dagger.Provides

@Module
class ActivityModule(private var activity: Activity) {
    @Provides
    fun provideActivity(): Activity {
        return activity
    }
    @Provides
    fun provideMainPresenter(): MainContract.Presenter {
        return MainPresenter()
    }
    @Provides
    fun provideSplashScreenPresenter(): SplashScreenContract.Presenter {
        return SplashScreenPresenter()
    }
    @Provides
    fun provideUserPresenter(): UserContract.Presenter {
        return UserPresenter()
    }

}