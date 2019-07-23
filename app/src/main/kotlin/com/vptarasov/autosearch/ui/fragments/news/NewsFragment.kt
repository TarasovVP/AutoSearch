package com.vptarasov.autosearch.ui.fragments.news

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.Constraints.TAG
import androidx.fragment.app.Fragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore
import com.vptarasov.autosearch.R
import com.vptarasov.autosearch.di.component.DaggerFragmentComponent
import com.vptarasov.autosearch.di.module.FragmentModule
import com.vptarasov.autosearch.model.News
import com.vptarasov.autosearch.util.Constants
import kotlinx.android.synthetic.main.fragment_news.view.*
import java.util.*
import javax.inject.Inject

class NewsFragment : Fragment(), NewsContract.View {

    private lateinit var newsTitle: TextView
    private lateinit var webView: WebView
    private lateinit var buttonBackNews: Button

    private lateinit var database: DatabaseReference

    @Inject
    lateinit var presenter: NewsContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependency()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_news, container, false)
        initView(view)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val url: String?
        val args = arguments
        if (args != null) {
            url = args.getString("newsUrl")
            presenter.loadNews(url!!)
        }
        writeNewsInFirestore("TitleTest", "TextTest", "https:test", "PhotoTest")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attach(this)
        presenter.subscribe()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.unsubscribe()
    }

    private fun injectDependency() {
        val fragmentComponent = DaggerFragmentComponent.builder()
            .fragmentModule(FragmentModule())
            .build()

        fragmentComponent.inject(this)
    }

    override fun initView(view: View) {
        newsTitle = view.newsTitle
        webView = view.webView
        buttonBackNews = view.buttonBackNews
        buttonBackNews.setOnClickListener { Objects.requireNonNull(fragmentManager)?.popBackStack() }
    }

    override fun setDataToViews(news: News) {
        newsTitle.text = news.title
        webView.loadDataWithBaseURL(Constants.NEWS_URL, news.text, "text/html", "UTF-8", null)
    }

    private fun writeNewsInFirestore(title: String, text: String, url: String, photo: String){
        val db = FirebaseFirestore.getInstance()
        val news = News(title, text, url, photo)
        db.collection("news")
            .add(news)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }
}
