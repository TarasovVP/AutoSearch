package com.vptarasov.autosearch.ui.fragments

import com.vptarasov.autosearch.model.Car

class CarContract {
    interface View {
        fun getFavouriteCars(): ArrayList<Car>
    }
}