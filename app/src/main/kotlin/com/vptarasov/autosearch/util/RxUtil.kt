package com.vptarasov.autosearch.util

import android.annotation.SuppressLint
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

@SuppressLint("CheckResult")
object RxUtil {

    fun delayedConsumer(delay: Long, consumer: Consumer<Long>) {
        delayedConsumer(delay, consumer, null)
    }

    private fun delayedConsumer(delay: Long, consumer: Consumer<Long>, errorConsumer: Consumer<Throwable>?) {
        Observable
            .timer(delay, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(consumer, errorConsumer ?: Consumer { it.printStackTrace() })
    }
}



