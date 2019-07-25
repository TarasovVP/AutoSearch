package com.vptarasov.autosearch.ui.fragments.news_preview

import android.annotation.SuppressLint
import com.google.firebase.firestore.FirebaseFirestore
import com.vptarasov.autosearch.api.Api
import com.vptarasov.autosearch.api.HTMLParser
import com.vptarasov.autosearch.model.News
import com.vptarasov.autosearch.util.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class NewsPreviewPresenter : NewsPreviewContract.Presenter {

    private val subscriptions = CompositeDisposable()
    private lateinit var view: NewsPreviewContract.View
    private var news:ArrayList<News> = ArrayList()
    private var newsWithFullText: ArrayList<News> = ArrayList()
    val db = FirebaseFirestore.getInstance()
    override fun attach(view: NewsPreviewContract.View) {
        this.view = view
    }

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun getNewsFromFirebase(){
        val firestore = FirebaseFirestore.getInstance()
        firestore.collection("news")
            .get()
            .addOnSuccessListener { result ->
                val news: ArrayList<News> = ArrayList()
                for (document in result) {
                    val newsSingle = document.toObject(News::class.java)

                    news.add(newsSingle)

                }
                view.initAdapter(news)
            }
            .addOnFailureListener {

            }
    }

    @SuppressLint("CheckResult")
    override fun loadNewsPreview() {
        val subscribe = Api
            .service
            .loadUrl(Constants.NEWS_URL + Constants.ARTICLES)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ responseBody ->
                val htmlParser = HTMLParser()
                news = htmlParser.getNewsPreviews(responseBody.string())

                for (news in news){
                    news.url?.let { loadFullTextNews(it) }
                }

                view.initAdapter(newsWithFullText)
            }, { throwable -> throwable.printStackTrace() })
        subscriptions.add(subscribe)
for (newsT in news) {
    db.collection("news").document()
        .set(newsT)
        .addOnSuccessListener { }
        .addOnFailureListener { }
}
    }

    @SuppressLint("CheckResult")
    private fun loadFullTextNews(url: String){



            Api
                .service
                .loadUrl(Constants.NEWS_URL + url.substring(1))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({responseBody ->
                    val html = responseBody.string()
                    val htmlParser = HTMLParser()
                    val newsLocal = htmlParser.getNews(html)
                    for (newsI in news){
                        if(newsI.title == newsLocal.title){
                            newsI.fullText = newsLocal.text
                        }
                    }
                    newsWithFullText.add(newsLocal)
                }, { throwable -> throwable.printStackTrace() })


    }



}