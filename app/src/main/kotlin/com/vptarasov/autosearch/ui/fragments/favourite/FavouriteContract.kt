package com.vptarasov.autosearch.ui.fragments.favourite

import com.vptarasov.autosearch.ui.AppContract

class FavouriteContract {

    interface View: AppContract.View

    interface Presenter: AppContract.Presenter<View>
}