package com.vptarasov.autosearch.ui.fragments.base

import android.animation.LayoutTransition
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tapadoo.alerter.Alerter
import com.vptarasov.autosearch.R
import com.vptarasov.autosearch.animation.Animated
import com.vptarasov.autosearch.animation.ScreenAnimations
import java.util.*

open class BaseFragment : Fragment() {
    private var animationFinishInterval = 1500

    private var swipeRefreshLayout: SwipeRefreshLayout? = null

    private val isAttached: Boolean
        get() = activity == null || activity!!.isFinishing || !isAdded


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (this is Animated) {
            getScreenAnimations().setFinishAnimated(this as Animated, animationFinishInterval)
            (this as Animated).animate()
        }
    }

    protected fun animateLayout(view: View?): View {
        val layout = view as ViewGroup?
        var layoutTransition: LayoutTransition? = layout!!.layoutTransition
        if (layoutTransition == null) {
            layout.layoutTransition = LayoutTransition()
            layoutTransition = layout.layoutTransition
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            layoutTransition!!.enableTransitionType(LayoutTransition.CHANGING)
        }
        return layout
    }

    protected fun inflateWithLoadingIndicator(resId: Int, parent: ViewGroup?): View {
        swipeRefreshLayout = SwipeRefreshLayout(Objects.requireNonNull<FragmentActivity>(activity))
        swipeRefreshLayout?.layoutParams =
            ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        swipeRefreshLayout?.isEnabled = false
        val view = LayoutInflater.from(activity).inflate(resId, parent, false)
        swipeRefreshLayout?.addView(view)
        return swipeRefreshLayout as SwipeRefreshLayout
    }

    private fun showErrorMessage(text: String) {
        if (isAttached) {
            return
        }
        Alerter.create(Objects.requireNonNull<FragmentActivity>(activity))
            .setIconColorFilter(ContextCompat.getColor(activity!!, android.R.color.white))
            .setBackgroundColorRes(R.color.colorError)
            .setIcon(R.drawable.ic_close_circle)
            .setTitle(R.string.error)
            .setText(text)
            .show()
    }

    protected fun showErrorMessage(resId: Int) {
        if (isAttached) {
            return
        }
        showErrorMessage(getString(resId))
    }


    protected fun setAnimationFinishInterval(interval: Int) {
        this.animationFinishInterval = interval
    }

    protected fun animateLoadingView() {
        animateLayout(swipeRefreshLayout)
    }


    private fun getScreenAnimations(): ScreenAnimations {
        return screenAnimations
    }

    companion object {
         var screenAnimations: ScreenAnimations =
             ScreenAnimations()
    }

}
