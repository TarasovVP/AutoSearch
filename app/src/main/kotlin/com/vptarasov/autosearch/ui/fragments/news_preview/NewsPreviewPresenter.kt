package com.vptarasov.autosearch.ui.fragments.news_preview

import com.vptarasov.autosearch.App
import com.vptarasov.autosearch.model.News
import com.vptarasov.autosearch.ui.fragments.BasePresenter

class NewsPreviewPresenter : BasePresenter<NewsPreviewContract.View>(), NewsPreviewContract.Presenter {

    override fun getNewsFromFirebase(){
        getView()?.showProgress()
        App.instance?.firebaseFirestore?.collection("news")
            ?.get()
            ?.addOnSuccessListener { result ->
                val news: ArrayList<News> = ArrayList()
                for (document in result) {
                    val newsSingle = document.toObject(News::class.java)
                    news.add(newsSingle)
                }
                getView()?.initAdapter(news)
                getView()?.hideProgress()
            }
            ?.addOnFailureListener {
                getView()?.hideProgress()
            }
    }
}