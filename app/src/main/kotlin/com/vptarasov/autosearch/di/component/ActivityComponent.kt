package com.vptarasov.autosearch.di.component

import com.vptarasov.autosearch.di.module.ActivityModule
import com.vptarasov.autosearch.ui.activity.main.MainActivity
import com.vptarasov.autosearch.ui.activity.splash_screen.SplashScreenActivity
import dagger.Component

@Component(modules = [ActivityModule::class])
interface ActivityComponent {

    fun inject(mainActivity: MainActivity)
    fun inject(splashScreenActivity: SplashScreenActivity)
}