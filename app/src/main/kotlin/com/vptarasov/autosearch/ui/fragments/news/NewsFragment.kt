package com.vptarasov.autosearch.ui.fragments.news


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.vptarasov.autosearch.R
import com.vptarasov.autosearch.api.Api
import com.vptarasov.autosearch.di.component.DaggerFragmentComponent
import com.vptarasov.autosearch.di.module.FragmentModule
import com.vptarasov.autosearch.util.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_news.view.*
import org.jsoup.Jsoup
import java.util.*
import javax.inject.Inject


class NewsFragment : Fragment(), NewsContract.View {

    private var url: String? = null
    private lateinit var newsTitle: TextView
    private lateinit var webView: WebView
    private lateinit var buttonBackNews: Button

    @Inject
    lateinit var presenter: NewsContract.Presenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_news, container, false)
        injectDependency()
        newsTitle = view.newsTitle
        webView = view.webView
        buttonBackNews = view.buttonBackNews
        buttonBackNews.setOnClickListener { Objects.requireNonNull(fragmentManager)?.popBackStack() }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val args = arguments
        if (args != null) {
            url = args.getString("newsUrl")

        }
        loadNews(url!!)

    }

    @SuppressLint("CheckResult")
    private fun loadNews(url: String) {
        Api
            .service
            .loadUrl(Constants.NEWS_URL + url.substring(1))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ responseBody ->

                val elements = Jsoup.parse(responseBody.string()).select("td.content")
                newsTitle.text = elements.select("h1").text()
                val content = elements.select("div.green_content")
                val newsText = content.toString().replace("href".toRegex(), "").replace("Источник: ".toRegex(), "")
                    .replace("http://www.autonews.ru".toRegex(), "")
                webView.loadDataWithBaseURL(Constants.NEWS_URL, newsText, "text/html", "UTF-8", null)
            }, { throwable -> throwable.printStackTrace() })
    }
    private fun injectDependency() {
        val fragmentComponent = DaggerFragmentComponent.builder()
            .fragmentModule(FragmentModule())
            .build()

        fragmentComponent.inject(this)
    }
}
