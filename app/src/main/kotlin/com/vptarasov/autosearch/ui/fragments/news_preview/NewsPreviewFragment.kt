package com.vptarasov.autosearch.ui.fragments.news_preview


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
import com.vptarasov.autosearch.di.component.DaggerFragmentComponent
import com.vptarasov.autosearch.di.module.FragmentModule
import com.vptarasov.autosearch.interfaces.NewsInteractionListener
import com.vptarasov.autosearch.model.News
import com.vptarasov.autosearch.parser.HTMLParser
import com.vptarasov.autosearch.ui.fragments.news.NewsFragment
import com.vptarasov.autosearch.util.Constants
import com.vptarasov.autosearch.util.FragmentUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_news_preview.view.*
import java.util.*
import javax.inject.Inject


class NewsPreviewFragment : Fragment(), NewsInteractionListener, NewsPreviewContract.View {


    private lateinit var adapter: NewsPreviewAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var news: ArrayList<News>

    @Inject
    lateinit var presenter: NewsPreviewContract.Presenter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_news_preview, container, false)
        injectDependency()
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
            .loadUrl(Constants.NEWS_URL + Constants.ARTICLES)
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
    private fun injectDependency() {
        val fragmentComponent = DaggerFragmentComponent.builder()
            .fragmentModule(FragmentModule())
            .build()

        fragmentComponent.inject(this)
    }
}
