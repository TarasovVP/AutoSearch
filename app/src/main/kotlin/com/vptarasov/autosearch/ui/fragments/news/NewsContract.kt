package com.vptarasov.autosearch.ui.fragments.news

import com.vptarasov.autosearch.model.News
import com.vptarasov.autosearch.ui.AppContract

class NewsContract {

    interface View: AppContract.View{
        fun initView(view: android.view.View)
        fun setDataToViews(news: News)
    }

    interface Presenter: AppContract.Presenter<View>{
        fun loadNews(url: String)
    }
}