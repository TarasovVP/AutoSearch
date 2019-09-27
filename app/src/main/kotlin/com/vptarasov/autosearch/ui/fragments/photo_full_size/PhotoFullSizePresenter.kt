package com.vptarasov.autosearch.ui.fragments.photo_full_size

class PhotoFullSizePresenter : PhotoFullSizeContract.Presenter {

    private lateinit var view: PhotoFullSizeContract.View

    override fun attach(view: PhotoFullSizeContract.View) {
        this.view = view
    }

}