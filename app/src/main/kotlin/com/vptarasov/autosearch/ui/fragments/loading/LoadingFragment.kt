package com.vptarasov.autosearch.ui.fragments.loading


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.vptarasov.autosearch.R
import com.vptarasov.autosearch.di.component.DaggerFragmentComponent
import com.vptarasov.autosearch.di.module.FragmentModule
import com.vptarasov.autosearch.animation.Animated
import com.vptarasov.autosearch.model.SearchData
import com.vptarasov.autosearch.ui.activity.main.MainActivity
import com.vptarasov.autosearch.ui.fragments.base.BaseFragment
import com.vptarasov.autosearch.util.Constants
import com.vptarasov.autosearch.util.RxUtil
import com.vptarasov.autosearch.animation.ScreenAnimation
import com.vptarasov.autosearch.animation.ScreenAnimations
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.fragment_loading.view.*
import javax.inject.Inject

class LoadingFragment : BaseFragment(), Animated, LoadingContract.View {


    @Inject
    lateinit var presenter: LoadingContract.Presenter

    private var searchData: SearchData? = null
    private var progressTyre: ImageView? = null
    private var logo: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependency()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflateWithLoadingIndicator(R.layout.fragment_loading, container)
        progressTyre = view.progressTyre
        logo = view.logo
        animateLoadingView()
        setAnimationFinishInterval(Constants.INTERVAL_ANIM_FINISH)
        return animateLayout(view)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attach(this)
        presenter.subscribe()
        presenter.loadSearchData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.unsubscribe()
    }

    override fun showMainActivity() {
        searchData = presenter.getSearchData()
        val intent = Intent(activity, MainActivity::class.java)
        intent.putExtra("searchData", searchData)
        startActivity(intent)
    }

    override fun animate() {
        screenAnimations.addAnimation(
            ScreenAnimation(
                ScreenAnimation.AnimationType.FADE_IN,
                Constants.INTERVAL_ANIM_LOGO,
                logo
            )
        )
            .addAnimation(
                ScreenAnimation(
                    ScreenAnimation.AnimationType.VISIBILITY_IN,
                    Constants.INTERVAL_ANIM_LOADING,
                    progressTyre
                )
            )
            .start()
    }

    override fun onScreenAnimationsFinished() {
        val rotation = AnimationUtils.loadAnimation(activity, R.anim.rotate)
        rotation.fillAfter = true
        ScreenAnimations.rotate(progressTyre)

        RxUtil.delayedConsumer(Constants.INTERVAL_ANIM_LOADING.toLong(),
            Consumer { this@LoadingFragment.proceed() })

    }

    private fun proceed() {
        showMainActivity()
    }

    private fun injectDependency() {
        val fragmentComponent = DaggerFragmentComponent.builder()
            .fragmentModule(FragmentModule())
            .build()

        fragmentComponent.inject(this)
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity).supportActionBar?.show()
    }

}