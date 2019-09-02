package com.vptarasov.autosearch.util.animation

import android.os.Build
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.ImageView
import com.vptarasov.autosearch.util.RxUtil
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
        currentIndex = 0

        if (currentIndex < size) {
            nextAnimationIn = get(0).interval
            run()
        } else {
            currentIndex = size + 1
        }
    }

    private fun animateNext() {
        val animation = get(currentIndex)
        currentIndex++

        if (animation.type == ScreenAnimation.AnimationType.FADE_IN || animation.type == ScreenAnimation.AnimationType.FADE_OUT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                animation.view?.animate()?.alpha(if (animation.type == ScreenAnimation.AnimationType.FADE_IN) 1f else 0f)
                    ?.withEndAction(this@ScreenAnimations)?.start()
            }
        } else {
            animation.view!!.visibility = if (animation.type == ScreenAnimation.AnimationType.VISIBILITY_IN)
                View.VISIBLE
            else
                View.GONE
            run()
        }
    }

    override fun accept(aLong: Long?) {
        if (isFinished) {
            if (animated != null) {
                animated!!.onScreenAnimationsFinished()
            }
        } else {
            animateNext()
        }
    }

    override fun run() {
        if (nextAnimationIn != -1) {
            if (isFinished) {
            } else {
                nextAnimationIn = get(currentIndex).interval
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
