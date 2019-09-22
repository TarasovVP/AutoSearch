package com.vptarasov.autosearch.ui.fragments.news_preview

import com.vptarasov.autosearch.App
import com.vptarasov.autosearch.model.News
import io.reactivex.disposables.CompositeDisposable

class NewsPreviewPresenter : NewsPreviewContract.Presenter {

    private val subscriptions = CompositeDisposable()
    private lateinit var view: NewsPreviewContract.View
    override fun attach(view: NewsPreviewContract.View) {
        this.view = view
    }

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun getNewsFromFirebase(){
        view.showProgress()
        App.instance?.firebaseFirestore?.collection("news")
            ?.get()
            ?.addOnSuccessListener { result ->
                val news: ArrayList<News> = ArrayList()
                for (document in result) {
                    val newsSingle = document.toObject(News::class.java)
                    news.add(newsSingle)
                }
                view.initAdapter(news)
                view.hideProgress()
            }
            ?.addOnFailureListener {
                view.hideProgress()
            }
    }
}