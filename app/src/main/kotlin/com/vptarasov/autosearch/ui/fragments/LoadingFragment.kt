package com.vptarasov.autosearch.ui.fragments


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.vptarasov.autosearch.R
import com.vptarasov.autosearch.api.Api
import com.vptarasov.autosearch.interfaces.Animated
import com.vptarasov.autosearch.model.SearchData
import com.vptarasov.autosearch.parser.HTMLParser
import com.vptarasov.autosearch.ui.activity.MainActivity
import com.vptarasov.autosearch.util.RxUtil
import com.vptarasov.autosearch.util.ScreenAnimation
import com.vptarasov.autosearch.util.ScreenAnimations
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LoadingFragment : BaseFragment(), Animated {

    private var searchData: SearchData? = null


    private var progressTyre: ImageView? = null

    private var logo: ImageView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflateWithLoadingIndicator(R.layout.fragment_loading, container)
        animateLoadingView()
        setAnimationFinishInterval(INTERVAL_ANIM_FINISH)
        loadData()
        return animateLayout(view)
    }


    override fun animate() {
        BaseFragment.screenAnimations.addAnimation(ScreenAnimation(ScreenAnimation.AnimationType.FADE_IN, INTERVAL_ANIM_LOGO, logo))
            .addAnimation(ScreenAnimation(ScreenAnimation.AnimationType.VISIBILITY_IN, INTERVAL_ANIM_LOADING, progressTyre))
            .start()
    }

    override fun onScreenAnimationsFinished() {
        val rotation = AnimationUtils.loadAnimation(activity, R.anim.rotate)
        rotation.fillAfter = true
        ScreenAnimations.rotate(progressTyre)

        RxUtil.delayedConsumer(INTERVAL_ANIM_LOADING.toLong()) {proceed()}

    }

    private fun proceed() {
        val intent = Intent(activity, MainActivity::class.java)
        intent.putExtra("searchData", searchData)
        startActivity(intent)

    }

    @SuppressLint("CheckResult")
    private fun loadData() {
        Api.service.loadData("")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ responseBody ->
                val html = responseBody.string()
                val htmlParser = HTMLParser()
                searchData = htmlParser.getSearchData(html)

            }, { throwable -> throwable.printStackTrace() })
    }
    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity).supportActionBar?.show()
    }

    companion object {
        private const val INTERVAL_ANIM_FINISH = 500
        private const val INTERVAL_ANIM_LOGO = 1000
        private const val INTERVAL_ANIM_LOADING = 1500
    }

}