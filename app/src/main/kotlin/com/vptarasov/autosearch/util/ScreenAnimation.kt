package com.vptarasov.autosearch.util

import android.widget.ImageView

class ScreenAnimation(internal val type: Int, internal val interval: Int, val view: ImageView?) {

    interface AnimationType {
        companion object {
            const val FADE_IN = 0
            const val FADE_OUT = 1
            const val VISIBILITY_IN = 2
        }

    }
}