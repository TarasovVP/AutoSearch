package com.vptarasov.autosearch.ui.fragments.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.vptarasov.autosearch.R
import com.vptarasov.autosearch.di.component.DaggerFragmentComponent
import com.vptarasov.autosearch.di.module.FragmentModule
import com.vptarasov.autosearch.util.Constants
import kotlinx.android.synthetic.main.fragment_news.view.*
import java.util.*
import javax.inject.Inject

class NewsFragment : Fragment(), NewsContract.View {

    private lateinit var newsTitle: TextView
    private lateinit var webView: WebView
    private lateinit var buttonBackNews: Button

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
        val title: String?
        val text: String?
        val args = arguments
        if (args != null) {
            title = args.getString("newsTitle")
            text = args.getString("newsFullText")
            newsTitle.text = title
            webView.loadDataWithBaseURL(Constants.NEWS_URL, text, "text/html", "UTF-8", null)
        }

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

}
