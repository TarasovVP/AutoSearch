package com.vptarasov.autosearch.ui.activity.splash_screen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.vptarasov.autosearch.R
import com.vptarasov.autosearch.di.component.DaggerActivityComponent
import com.vptarasov.autosearch.di.module.ActivityModule
import com.vptarasov.autosearch.ui.fragments.loading.LoadingFragment
import com.vptarasov.autosearch.util.FragmentUtil
import javax.inject.Inject

class SplashScreenActivity : AppCompatActivity(), SplashScreenContract.View {


    @Inject
    lateinit var presenter: SplashScreenContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        injectDependency()
        presenter.attach(this)
    }

    override fun showLoadingFragment() {
        FragmentUtil.replaceFragment(supportFragmentManager,
            LoadingFragment(), false)
    }

    private fun injectDependency() {
        val activityComponent = DaggerActivityComponent.builder()
            .activityModule(ActivityModule(this))
            .build()

        activityComponent.inject(this)
    }

}
