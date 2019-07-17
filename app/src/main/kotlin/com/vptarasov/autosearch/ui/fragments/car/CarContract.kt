package com.vptarasov.autosearch.ui.fragments.car

import com.vptarasov.autosearch.ui.AppContract

class CarContract {

    interface View: AppContract.View

    interface Presenter: AppContract.Presenter<View>
}