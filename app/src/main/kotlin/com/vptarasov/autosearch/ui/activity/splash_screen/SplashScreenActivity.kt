package com.vptarasov.autosearch.ui.activity.splash_screen

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.vptarasov.autosearch.R
import com.vptarasov.autosearch.di.component.DaggerActivityComponent
import com.vptarasov.autosearch.di.module.ActivityModule
import com.vptarasov.autosearch.model.SearchData
import com.vptarasov.autosearch.ui.activity.main.MainActivity
import com.vptarasov.autosearch.util.Constants
import com.vptarasov.autosearch.util.RxUtil
import com.vptarasov.autosearch.util.animation.Animated
import com.vptarasov.autosearch.util.animation.ScreenAnimation
import com.vptarasov.autosearch.util.animation.ScreenAnimations
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_splash_screen.*
import javax.inject.Inject

class SplashScreenActivity : AppCompatActivity(), SplashScreenContract.View, Animated {


    @Inject
    lateinit var presenter: SplashScreenContract.Presenter

    private var progressTyreIV: ImageView? = null
    private var logoIV: ImageView? = null
    private var screenAnimations: ScreenAnimations? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        progressTyreIV = progressTyre
        logoIV = logo
        injectDependency()
        presenter.attach(this)
        screenAnimations = ScreenAnimations()
        screenAnimations!!.setFinishAnimated(this@SplashScreenActivity as Animated, Constants.INTERVAL_ANIM_FINISH)
        (this as Animated).animate()

    }

    override fun animate() {
        screenAnimations?.addAnimation(
            ScreenAnimation(
                ScreenAnimation.AnimationType.FADE_IN,
                Constants.INTERVAL_ANIM_LOGO,
                logo
            )
        )
            ?.addAnimation(
                ScreenAnimation(
                    ScreenAnimation.AnimationType.VISIBILITY_IN,
                    Constants.INTERVAL_ANIM_LOADING,
                    progressTyre
                )
            )?.start()
    }

    override fun onScreenAnimationsFinished() {
        val rotation = AnimationUtils.loadAnimation(this@SplashScreenActivity, R.anim.rotate)
        rotation.fillAfter = true
        ScreenAnimations.rotate(progressTyre!!)

        RxUtil.delayedConsumer(Constants.INTERVAL_ANIM_LOADING.toLong(),
            Consumer { this@SplashScreenActivity.proceed() })
    }

    private fun proceed() {
        presenter.loadSearchData()
    }

    override fun showMainActivity(searchData: SearchData) {
        val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
        intent.putExtra("searchData", searchData)
        startActivity(intent)
    }

    private fun injectDependency() {
        val activityComponent = DaggerActivityComponent.builder()
            .activityModule(ActivityModule(this))
            .build()

        activityComponent.inject(this)
    }



}
