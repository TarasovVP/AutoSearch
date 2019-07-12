package com.vptarasov.autosearch.util

import android.annotation.SuppressLint
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

@SuppressLint("CheckResult")
object RxUtil {
    fun <S> mainThreadConsumer(`object`: S, consumer: Consumer<S>) {
        mainThreadConsumer(`object`, consumer, null)
    }

    private fun <S> mainThreadConsumer(`object`: S, consumer: Consumer<in S>, errorConsumer: Consumer<Throwable>?) {
        Observable
            .just(`object`)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(consumer, errorConsumer ?: Consumer { it.printStackTrace() })
    }

    fun delayedConsumer(delay: Long, consumer: ScreenAnimations) {
        delayedConsumer(delay, consumer, null)
    }

    private fun delayedConsumer(delay: Long, consumer: Consumer<Long>, errorConsumer: Consumer<Throwable>?) {
        Observable
            .timer(delay, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(consumer, errorConsumer ?: Consumer { it.printStackTrace() })
    }

    private fun <S> networkConsumer(
        observable: Observable<S>,
        consumer: Consumer<S>,
        errorConsumer: Consumer<Throwable>?
    ) {
        observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(consumer, errorConsumer ?: Consumer { it.printStackTrace() })
    }

    fun <S> networkConsumer(observable: Observable<S>, consumer: Consumer<S>) {
        networkConsumer(observable, consumer, null)
    }

    fun delayedConsumer(delay: Long, consumer: () -> Unit) {


    }


}
