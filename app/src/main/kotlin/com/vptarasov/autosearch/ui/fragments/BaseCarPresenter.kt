package com.vptarasov.autosearch.ui.fragments

import com.vptarasov.autosearch.ui.AppContract



abstract class BaseCarPresenter<T>: AppContract.Presenter<T> {

    private var view: T? = null

    override fun attach(view: T) {
        this.view = view
    }

     fun getView(): T? = view

}