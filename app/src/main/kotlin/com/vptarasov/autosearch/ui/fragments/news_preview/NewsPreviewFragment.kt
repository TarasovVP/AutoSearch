package com.vptarasov.autosearch.ui.fragments.news_preview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vptarasov.autosearch.R
import com.vptarasov.autosearch.di.component.DaggerFragmentComponent
import com.vptarasov.autosearch.di.module.FragmentModule
import com.vptarasov.autosearch.model.News
import com.vptarasov.autosearch.ui.fragments.news.NewsFragment
import com.vptarasov.autosearch.util.FragmentUtil
import kotlinx.android.synthetic.main.fragment_news_preview.view.*
import javax.inject.Inject

class NewsPreviewFragment : Fragment(), NewsPreviewContract.View {

    private lateinit var adapter: NewsPreviewAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar

    @Inject
    lateinit var presenter: NewsPreviewContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependency()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_news_preview, container, false)
        recyclerView = view.recyclerViewNews
        progressBar = view.progressBarNews
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter.getNewsFromFirebase()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attach(this)
    }

    override fun initAdapter(news: ArrayList<News>) {
        adapter = NewsPreviewAdapter(news)
        adapter.setListener(this)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }

    override fun onItemClick(news: News) {
        val bundle = Bundle()
        bundle.putString("newsTitle", news.title)
        bundle.putString("newsFullText", news.fullText)
        val newsFragment = NewsFragment()
        newsFragment.arguments = bundle
        fragmentManager.let { FragmentUtil.replaceFragment(it, newsFragment, true) }
    }
    override fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progressBar.visibility = View.GONE
    }


    private fun injectDependency() {
        val fragmentComponent = DaggerFragmentComponent.builder()
            .fragmentModule(FragmentModule())
            .build()
        fragmentComponent.inject(this)
    }


    }



