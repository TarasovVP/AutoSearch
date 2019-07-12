package com.vptarasov.autosearch.util

import android.os.Build
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.ImageView
import com.vptarasov.autosearch.BuildConfig
import com.vptarasov.autosearch.interfaces.Animated
import com.vptarasov.autosearch.interfaces.AnimationFinished
import com.vptarasov.autosearch.interfaces.NextAnimation
import io.reactivex.functions.Consumer
import java.util.*

class ScreenAnimations : ArrayList<ScreenAnimation>(), Consumer<Long>, Runnable {
    private var currentIndex = -1
    private var nextAnimationIn = -1
    private var finishInterval: Int = 0
    private var animated: Animated? = null


    private val isFinished: Boolean
        get() = currentIndex >= size

    fun start() {
        log("start")
        currentIndex = 0

        if (currentIndex < size) {
            nextAnimationIn = get(0).interval
            log("has first animation, starts in $nextAnimationIn milliseconds")
            run()
        } else {
            log("has no first animation, aborting")
            currentIndex = size + 1
        }
    }

    private fun animateNext() {
        log("starting $currentIndex animation")
        val animation = get(currentIndex)
        currentIndex++

        if (animation.type == ScreenAnimation.AnimationType.FADE_IN || animation.type == ScreenAnimation.AnimationType.FADE_OUT) {
            log("animating fade in/out")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                animation.view?.animate()?.alpha(if (animation.type == ScreenAnimation.AnimationType.FADE_IN) 1f else 0f)
                    ?.withEndAction(this@ScreenAnimations)?.start()
            }
        } else {
            log("animating visibility")
            animation.view!!.visibility = if (animation.type == ScreenAnimation.AnimationType.VISIBILITY_IN)
                View.VISIBLE
            else
                View.GONE
            run()
        }
    }

    @AnimationFinished
    override fun accept(aLong: Long?) {
        if (isFinished) {
            log("finished animations")
            if (animated != null) {
                animated!!.onScreenAnimationsFinished()
            }
        } else {
            animateNext()
        }
    }

    @NextAnimation
    override fun run() {
        if (nextAnimationIn != -1) {
            if (isFinished) {
                log("delaying finish animation for $finishInterval milliseconds")
            } else {
                nextAnimationIn = get(currentIndex).interval
                log("delaying next animation for $nextAnimationIn milliseconds")
            }
            RxUtil.delayedConsumer(
                (if (isFinished) finishInterval else nextAnimationIn).toLong(),
                this@ScreenAnimations
            )
        }
    }

    fun addAnimation(animation: ScreenAnimation): ScreenAnimations {
        add(animation)
        return this
    }

    fun setFinishAnimated(animated: Animated, finishInterval: Int) {
        this.animated = animated
        this.finishInterval = finishInterval
    }

    private fun log(text: String) {
        if (BuildConfig.DEBUG) {
            Log.w(javaClass.simpleName, text)
        }
    }

    companion object {

        fun rotate(imageView: ImageView?) {
            val rotate = RotateAnimation(0f, 180f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
            rotate.duration = 1000
            rotate.repeatCount = Animation.INFINITE
            rotate.interpolator = LinearInterpolator()

            imageView?.startAnimation(rotate)
        }
    }
}
