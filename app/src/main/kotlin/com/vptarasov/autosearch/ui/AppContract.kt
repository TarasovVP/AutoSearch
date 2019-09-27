package com.vptarasov.autosearch.ui

class AppContract {

    interface Presenter<in T> {
        fun attach(view: T)
    }

    interface View
}