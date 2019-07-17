package com.vptarasov.autosearch.ui.fragments.news_preview

import com.vptarasov.autosearch.ui.AppContract

class NewsPreviewContract {

    interface View: AppContract.View

    interface Presenter: AppContract.Presenter<View>
}