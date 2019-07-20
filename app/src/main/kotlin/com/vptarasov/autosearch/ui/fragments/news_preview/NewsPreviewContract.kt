package com.vptarasov.autosearch.ui.fragments.news_preview

import com.vptarasov.autosearch.model.News
import com.vptarasov.autosearch.ui.AppContract
import java.util.*

class NewsPreviewContract {

    interface View: AppContract.View{
        fun initAdapter(news: ArrayList<News>)
        fun onItemClick(news: News)
    }

    interface Presenter: AppContract.Presenter<View>{
        fun loadNewsPreview()
    }
}