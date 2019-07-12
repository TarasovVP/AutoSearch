package com.vptarasov.autosearch.ui.fragments


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vptarasov.autosearch.R
import com.vptarasov.autosearch.api.Api
import com.vptarasov.autosearch.interfaces.NewsInteractionListener
import com.vptarasov.autosearch.model.News
import com.vptarasov.autosearch.parser.HTMLParser
import com.vptarasov.autosearch.ui.adapters.NewsPreviewAdapter
import com.vptarasov.autosearch.util.FragmentUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_news_preview.view.*
import java.util.*


class NewsPreviewFragment : Fragment(), NewsInteractionListener {


    private lateinit var adapter: NewsPreviewAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var news: ArrayList<News>


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_news_preview, container, false)
        recyclerView = rootView.recyclerViewNews
        return rootView


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        loadNewsPreview()

    }

    @SuppressLint("CheckResult")
    private fun loadNewsPreview() {

        Api
            .service
            .loadUrl(Api.NEWS_URL + Api.ARTICLES)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ responseBody ->
                val html = responseBody.string()
                val htmlParser = HTMLParser()
                news = htmlParser.getNewsPreviews(html)
                initAdapter()

            }, { throwable -> throwable.printStackTrace() })
    }

    private fun initAdapter() {
        adapter = NewsPreviewAdapter(news)
        adapter.setListener(this)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }

    override fun onItemClick(news: News) {
        val bundle = Bundle()
        bundle.putString("newsUrl", news.url)
        val newsFragment = NewsFragment()
        newsFragment.arguments = bundle
        fragmentManager?.let { FragmentUtil.replaceFragment(it, newsFragment, true) }

    }
}
